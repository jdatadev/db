package dev.jdata.db.utils.paths;

import java.nio.file.Path;

public class PathUtil {

    public static String getFileName(Path path) {

        return path.toFile().getName();
    }
}
