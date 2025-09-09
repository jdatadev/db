package dev.jdata.db.utils.paths;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dev.jdata.db.utils.checks.Checks;

public class PathIOUtil {

    public static List<Path> closureOrConstantListPaths(Path directoryPath) throws IOException {

        return closureOrConstantListPaths(directoryPath, p -> true);
    }

    public static List<Path> closureOrConstantListPaths(Path directoryPath, Predicate<Path> predicate) throws IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(predicate);

        final List<Path> result = new ArrayList<>();

        listPaths(directoryPath, result, predicate, predicate != null ? (path, delegatePredicate) -> delegatePredicate.test(path) : null, (p, l) -> l.add(p));

        return result;
    }

    public static <P, R, E extends Exception> R closureOrConstantListPaths(Path directoryPath, Predicate<Path> predicate, PathVisitor<P, R, E> pathVisitor)
            throws E, IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(pathVisitor);

        return listPaths(directoryPath, null, predicate, predicate != null ? (path, delegatePredicate) -> delegatePredicate.test(path) : null, pathVisitor);
    }

    @FunctionalInterface
    public interface PathVisitor<P, R, E extends Exception> {

        R visit(Path path, P parameter) throws E;
    }

    public static <P, R, E extends Exception> R listPaths(Path directoryPath, P parameter, BiPredicate<Path, P> predicate, PathVisitor<P, R, E> pathVisitor)
            throws E, IOException {

        Objects.requireNonNull(directoryPath);
        Objects.requireNonNull(pathVisitor);

        return listPaths(directoryPath, parameter, parameter, predicate, pathVisitor);
    }

    private static <P, DELEGATE, R, E extends Exception> R listPaths(Path directoryPath, P parameter, DELEGATE delegate, BiPredicate<Path, DELEGATE> predicate,
            PathVisitor<P, R, E> pathVisitor) throws E, IOException {

        R result = null;

        try (Stream<Path> stream = Files.list(directoryPath)) {

            final Iterator<Path> iterator = stream.iterator();

            while (iterator.hasNext()) {

                if (predicate == null || predicate.test(iterator.next(), delegate)) {

                    result = pathVisitor.visit(iterator.next(), parameter);

                    if (result != null) {

                        break;
                    }
                }
            }
        }

        return result;
    }

    public static void printRecursively(Path path, String prefix) throws IOException {

        printRecursively(path, prefix, System.out);
    }

    public static void printRecursively(Path path, String prefix, PrintStream printStream) throws IOException {

        Objects.requireNonNull(path);
        Objects.requireNonNull(printStream);

        scanRecursively(path, null, null, (pathEntry, parameter) -> {

            if (prefix != null) {

                printStream.append(prefix);
            }

            printStream.println(PathUtil.getFileName(pathEntry));

            return null;
        });
    }

    private static <P, R, E extends Exception> R scanRecursively(Path path, P parameter, BiPredicate<Path, P> predicate, PathVisitor<P, R, E> pathVisitor) throws E, IOException {

        Objects.requireNonNull(path);
        Objects.requireNonNull(pathVisitor);

        return scanRecursively(path, path, parameter, predicate, pathVisitor);
    }

    private static <P, R, E extends Exception> R scanRecursively(Path rootPath, Path path, P parameter, BiPredicate<Path, P> predicate, PathVisitor<P, R, E> pathVisitor)
            throws E, IOException {

        Checks.isAbsolutePath(rootPath);

        R result;

        if (Files.isSymbolicLink(path)) {

            final Path realPath;

            try {
                realPath = path.toRealPath();

                result = rootPath.endsWith(realPath)
                        ? scanRecursively(rootPath, realPath, parameter, predicate, pathVisitor)
                        : null;
            }
            catch (NoSuchFileException ex) {

                result = null;
            }
        }
        else if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {

            try (Stream<Path> stream = Files.list(path)) {

                result = null;

                final Iterator<Path> iterator = stream.iterator();

                while (iterator.hasNext()) {

                    final Path subPath = iterator.next();

                    if (predicate == null || predicate.test(subPath, parameter)) {

                        result = pathVisitor.visit(subPath, parameter);

                        if (result != null) {

                            break;
                        }

                        result = scanRecursively(rootPath, subPath, parameter, predicate, pathVisitor);
                    }
                }
            }
        }
        else if (Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS)) {

            result = pathVisitor.visit(path, parameter);
        }
        else {
            result = null;
        }

        return result;
    }

    public static boolean deleteRecursively(Path path) throws IOException {

        Objects.requireNonNull(path);

        return deleteRecursively(path, path);
    }

    private static boolean deleteRecursively(Path rootPath, Path path) throws IOException {

        Checks.isAbsolutePath(rootPath);

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

            for (Path subPath : closureOrConstantListPaths(path)) {

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
