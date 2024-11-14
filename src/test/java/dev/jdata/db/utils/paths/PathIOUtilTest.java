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

        checkListFilePaths(p -> PathIOUtil.listFilePaths(p), fileNamePrefix, p -> true);
    }

    @Test
    @Category(UnitTest.class)
    public void testListFilePathsWithPredicate() throws IOException {

        final String fileNamePrefix = "file";

        final int fileNamePrefixLength = fileNamePrefix.length();

        final Predicate<Path> predicate = p -> Integer.parseInt(PathUtil.getFileName(p).substring(fileNamePrefixLength)) < 50;

        checkListFilePaths(p -> PathIOUtil.listFilePaths(p, predicate), fileNamePrefix, predicate);
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

            PathIOUtil.deleteRecursively(tempDirectoryPath);
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
            createFilesRecursively(tempDirectoryPath, 3);
        }
        finally {

            PathIOUtil.deleteRecursively(tempDirectoryPath);
        }

        assertThat(tempDirectoryPath).doesNotExist();
    }

    @Test
    @Category(UnitTest.class)
    public void testDoesNotDeleteRecursivelyIfSymlinkFiles() throws IOException {

        final Path tempDirectoryPath = createTempDirectory();

        try {
            createFilesRecursively(tempDirectoryPath, 3);
        }
        finally {

            PathIOUtil.deleteRecursively(tempDirectoryPath);
        }

        assertThat(tempDirectoryPath).doesNotExist();
    }

    private void createFilesRecursively(Path directoryPath, int levels) throws IOException {

        writeFiles(directoryPath, 10, "file");

        if (levels > 0) {

            for (int i = 0; i < 5; ++ i) {

                final Path subDirectoryPath = directoryPath.resolve("directory" + i);

                Files.createDirectory(subDirectoryPath);

                subDirectoryPath.toFile().deleteOnExit();

                createFilesRecursively(subDirectoryPath, levels - 1);
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

        filePath.toFile().deleteOnExit();

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {

            final String fileContents = PathUtil.getFileName(filePath) + "_contents";

            outputStream.write(fileContents.getBytes());
        }
    }

    private Path createTempDirectory() throws IOException {

        final Path tempDirectoryPath = Files.createTempDirectory(getClass().getSimpleName());

        tempDirectoryPath.toFile().deleteOnExit();

        return tempDirectoryPath;
    }
}
