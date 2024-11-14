package dev.jdata.db.utils.paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathIOUtil {

    public static List<Path> listPaths(Path directoryPath) throws IOException {

        return listPaths(directoryPath, p -> true);
    }

    public static List<Path> listPaths(Path directoryPath, Predicate<Path> predicate) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(predicate);

        final List<Path> result;

        try (Stream<Path> stream = Files.list(directoryPath)) {

            result = stream
                    .filter(predicate)
                    .collect(Collectors.toList());
        }

        return result;
    }

    public static boolean deleteRecursively(Path path) throws IOException {

        Objects.requireNonNull(path);

        return deleteRecursively(path, path);
    }

    private static boolean deleteRecursively(Path rootPath, Path path) throws IOException {

        if (!rootPath.isAbsolute()) {

            throw new IllegalArgumentException();
        }

        boolean result;

        boolean delete;

        if (Files.isSymbolicLink(path)) {

            final Path realPath;

            try {
                realPath = path.toRealPath();

                if (rootPath.endsWith(realPath)) {

                    result = deleteRecursively(rootPath, realPath);

                    delete = true;
                }
                else {
                    result = false;
                    delete = false;
                }
            }
            catch (NoSuchFileException ex) {

                result = true;
                delete = true;
            }
        }
        else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {

            boolean subDeleted = true;

            for (Path subPath : listPaths(path)) {

                if (!deleteRecursively(rootPath, subPath)) {

                    subDeleted = false;
                }
            }

            result = subDeleted;
            delete = subDeleted;
        }
        else if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

            result = true;
            delete = true;
        }
        else {
            result = false;
            delete = false;
        }

        if (delete) {

            Files.delete(path);
        }

        return result;
    }

    public static void deleteOnExit(Path path) {

        Objects.requireNonNull(path);

        path.toFile().deleteOnExit();
    }
}
