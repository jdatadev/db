package dev.jdata.db.utils.paths;

import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public class PathUtil {

    public static String getFileName(Path path) {

        Objects.requireNonNull(path);

        return path.toFile().getName();
    }

    public static String getNameString(Path path, int index) {

        Objects.requireNonNull(path);
        Checks.checkIndex(index, path.getNameCount());

        return getFileName(path.getName(index));
    }
}
