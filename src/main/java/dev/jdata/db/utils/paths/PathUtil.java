package dev.jdata.db.utils.paths;

import java.nio.file.Path;
import java.util.Objects;

public class PathUtil {

    public static String getFileName(Path path) {

        Objects.requireNonNull(path);

        return path.toFile().getName();
    }

    public static String getNameString(Path path, int index) {

        Objects.requireNonNull(path);
        Objects.checkIndex(index, path.getNameCount());

        return getFileName(path.getName(index));
    }
}
