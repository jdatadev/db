package dev.jdata.db.utils.paths;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.adt.sets.Sets;
import dev.jdata.db.utils.function.CheckedExceptionFunction;

public final class PathIOUtilTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testListFilePaths() throws IOException {

        final String fileNamePrefix = "file";

        checkListFilePaths(p -> PathIOUtil.listPaths(p), fileNamePrefix, p -> true);
    }

    @Test
    @Category(UnitTest.class)
    public void testListFilePathsWithPredicate() throws IOException {

        final String fileNamePrefix = "file";

        final int fileNamePrefixLength = fileNamePrefix.length();

        final Predicate<Path> predicate = p -> Integer.parseInt(PathUtil.getFileName(p).substring(fileNamePrefixLength)) < 50;

        checkListFilePaths(p -> PathIOUtil.listPaths(p, predicate), fileNamePrefix, predicate);
    }

    private void checkListFilePaths(CheckedExceptionFunction<Path, List<Path>, IOException> listFilePaths, String fileNamePrefix, Predicate<Path> predicate) throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        try {
            final int numFiles = 100;

            writeFiles(tempDirectoryPath, numFiles, fileNamePrefix);

            final List<Path> filePaths = listFilePaths.apply(tempDirectoryPath);

            final Set<Path> distinctDirectoryPaths = Sets.distinct(filePaths, Path::getParent);

            assertThat(distinctDirectoryPaths).hasSize(1);
            assertThat(distinctDirectoryPaths.iterator().next()).isEqualTo(tempDirectoryPath);

            final List<String> actualFileNames = Lists.map(filePaths, p -> PathUtil.getFileName(p));
            final List<String> expectedFileNames = Lists.filterAndMap(filePaths, predicate, p -> PathUtil.getFileName(p));

            final int fileNamePrefixLength = fileNamePrefix.length();

            sortFileNames(actualFileNames, fileNamePrefixLength);
            sortFileNames(expectedFileNames, fileNamePrefixLength);

            assertThat(actualFileNames).isEqualTo(expectedFileNames);

            final Path modifiableListPath = tempDirectoryPath.resolve("modifiable_list");

            checkModifiable(filePaths, modifiableListPath);
        }
        finally {

            assertThat(PathIOUtil.deleteRecursively(tempDirectoryPath)).isTrue();
        }
    }

    private static void sortFileNames(List<String> fileNames, int fileNamePrefixLength) {

        fileNames.sort((s1, s2) -> Integer.compare(Integer.parseInt(s1.substring(fileNamePrefixLength)), Integer.parseInt(s2.substring(fileNamePrefixLength))));
    }

    @Test
    @Category(UnitTest.class)
    public void testDeleteRecursively() throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        try {
            createFilesRecursively(tempDirectoryPath, 3, (p, l) -> { });
        }
        finally {

            assertThat(PathIOUtil.deleteRecursively(tempDirectoryPath)).isTrue();
        }

        assertThat(tempDirectoryPath).doesNotExist();
    }

    @Test
    @Category(UnitTest.class)
    public void testDoesNotDeleteRecursivelyIfSymlinksToOutsideOfTree() throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        final Path rootDirectoryPath = tempDirectoryPath.getRoot();

        try {
            createFilesRecursively(tempDirectoryPath, 3, (p, l) -> {

                final Path symlinkPath = Files.createSymbolicLink(p.resolve("test_symlink"), rootDirectoryPath);

                PathIOUtil.deleteOnExit(symlinkPath);
            });
        }
        finally {

            assertThat(PathIOUtil.deleteRecursively(tempDirectoryPath)).isFalse();
        }

        assertThat(tempDirectoryPath).exists();
    }

    @Test
    @Category(UnitTest.class)
    public void testDoesDeleteRecursivelyIfSymlinksToNonExistantFileWithinTree() throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        try {
            createFilesRecursively(tempDirectoryPath, 3, (p, l) -> {

                final Path symlinkPath = Files.createSymbolicLink(p.resolve("test_symlink"), p.resolve("test_symlink_target"));

                PathIOUtil.deleteOnExit(symlinkPath);
            });
        }
        finally {

            assertThat(PathIOUtil.deleteRecursively(tempDirectoryPath)).isTrue();
        }

        assertThat(tempDirectoryPath).doesNotExist();
    }

    @Test
    @Category(UnitTest.class)
    public void testDoesDeleteRecursivelyIfSymlinksToExistantFileWithinTree() throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        try {
            createFilesRecursively(tempDirectoryPath, 3, (p, l) -> {

                final Path targetFilePath = p.resolve("test_symlink_target");

                writeFile(targetFilePath);

                final Path symlinkPath = Files.createSymbolicLink(p.resolve("test_symlink"), targetFilePath);

                PathIOUtil.deleteOnExit(symlinkPath);
            });
        }
        finally {

            assertThat(PathIOUtil.deleteRecursively(tempDirectoryPath)).isTrue();
        }

        assertThat(tempDirectoryPath).doesNotExist();
    }

    @FunctionalInterface
    private interface FileAdder {

        void add(Path directoryPath, int level) throws IOException;
    }

    private void createFilesRecursively(Path directoryPath, int numLevels, FileAdder fileAdder) throws IOException {

        createFilesRecursively(directoryPath, numLevels, 0, fileAdder);
    }

    private void createFilesRecursively(Path directoryPath, int numLevels, int level, FileAdder fileAdder) throws IOException {

        writeFiles(directoryPath, 10, "file");

        fileAdder.add(directoryPath, level);

        if (level < numLevels) {

            for (int i = 0; i < 5; ++ i) {

                final Path subDirectoryPath = directoryPath.resolve("directory_" + level + '_' + i);

                Files.createDirectory(subDirectoryPath);

                PathIOUtil.deleteOnExit(subDirectoryPath);

                createFilesRecursively(subDirectoryPath, numLevels, level + 1, fileAdder);
            }
        }
    }

    private static List<String> writeFiles(Path directoryPath, int numFiles, String fileNamePrefix) throws IOException {

        final List<String> fileNames = Lists.ofIntRange(0, numFiles, i -> fileNamePrefix + i);

        for (String fileName : fileNames) {

            final Path filePath = directoryPath.resolve(fileName);

            writeFile(filePath);
        }

        return fileNames;
    }

    private static void writeFile(Path filePath) throws IOException {

        PathIOUtil.deleteOnExit(filePath);

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {

            final String fileContents = PathUtil.getFileName(filePath) + "_contents";

            outputStream.write(fileContents.getBytes());
        }
    }

    private Path createTempDirectory() throws IOException {

        final Path tempDirectoryPath = Files.createTempDirectory(getClass().getSimpleName());

        PathIOUtil.deleteOnExit(tempDirectoryPath);

        return tempDirectoryPath;
    }
}
