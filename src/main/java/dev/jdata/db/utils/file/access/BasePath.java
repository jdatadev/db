package dev.jdata.db.utils.file.access;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.checks.Checks;

public abstract class BasePath {

    private static final String[] rootParts = new String[0];

    private final String[] parts;

    BasePath() {

        this.parts = rootParts;
    }

    BasePath(Path path) {

        Objects.requireNonNull(path);

        final int nameCount = path.getNameCount();

        if (nameCount == 0) {

            throw new IllegalArgumentException();
        }

        this.parts = new String[nameCount];

        for (int i = 0; i < nameCount; ++ i) {

            parts[i] = path.getName(i).toString();
        }
    }

    BasePath(BasePath path, BasePath additionalPath) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(additionalPath);

        final String[] pathParts = path.parts;
        final String[] additionalParts = additionalPath.parts;

        final int numParts = pathParts.length;
        final int numAdditionalParts = additionalParts.length;

        final int totalNumParts = numParts + numAdditionalParts;

        final String[] parts = Arrays.copyOf(path.parts, totalNumParts);

        System.arraycopy(additionalParts, 0, parts, numParts, numAdditionalParts);

        this.parts = parts;
    }

    BasePath(BasePath path, String additionalPathName) {
        this(path.parts, additionalPathName);
    }

    BasePath(String pathName) {

        Checks.isPathName(pathName);

        this.parts =  new String[] { pathName };
    }

    BasePath(String[] pathNames) {

        Checks.areElements(pathNames, Checks::isPathName);

        this.parts = Array.copyOf(pathNames);
    }

    BasePath(String[] pathNames, String additionalPathName) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::isPathName);
        Checks.isPathName(additionalPathName);

        final int numPathNames = pathNames.length;

        this.parts = Arrays.copyOf(pathNames, numPathNames + 1);

        parts[numPathNames] = additionalPathName;
    }

    final String getLastName() {

        return parts[parts.length - 1];
    }

    final <R extends IPath> R resolvePathName(String pathName, BiFunction<String[], String, R> createPath) {

        Checks.isPathName(pathName);

        return createPath.apply(parts, pathName);
    }

    final String asString(boolean absolute) {

        final char separatorCharacter = File.separatorChar;

        int sbCapacity = Array.sum(parts, p -> p.length());

        final int numParts = parts.length;

        sbCapacity += absolute ? numParts : numParts - 1;

        final StringBuilder sb = new StringBuilder(sbCapacity);

        if (absolute) {

            sb.append(separatorCharacter);
        }

        Strings.join(parts, separatorCharacter, sb);

        return sb.toString();
    }

    @Override
    public final int hashCode() {

        return Arrays.hashCode(parts);
    }

    @Override
    public final boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final BasePath other = (BasePath)object;

            result = Arrays.equals(parts, other.parts);
        }

        return result;
    }
}
