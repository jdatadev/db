package dev.jdata.db.utils.paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PathIOUtil {

    public static List<Path> listPaths(Path directoryPath) throws IOException {

        return listPaths(directoryPath, p -> true);
    }

    public static List<Path> listPaths(Path directoryPath, Predicate<Path> predicate) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(predicate);

        final List<Path> result = new ArrayList<>();

        listPaths(directoryPath, result, predicate, (p, l) -> l.add(p));

        return result;
    }

    public static <P> void listPaths(Path directoryPath, P parameter, Predicate<Path> predicate, BiConsumer<Path, P> consumer) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(consumer);

        try (Stream<Path> stream = Files.list(directoryPath)) {

            stream
                    .filter(predicate)
                    .forEach(p -> consumer.accept(p, parameter));
        }
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
