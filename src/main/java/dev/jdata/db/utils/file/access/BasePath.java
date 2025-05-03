package dev.jdata.db.utils.file.access;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.allocators.IArrayAllocator;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.paths.PathUtil;

abstract class BasePath {

    public interface IPathStringsAllocator extends IArrayAllocator<String> {

        String getString(CharSequence charSequence);
    }

    private static final String[] rootParts = new String[0];

    private String[] parts;

    BasePath() {

    }

    final void initializeRoot() {

        this.parts = rootParts;
    }

    final void initialize(BasePath path, BasePath additionalPath, IPathStringsAllocator pathArrayAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(additionalPath);
        Objects.requireNonNull(pathArrayAllocator);

        final String[] pathParts = path.parts;
        final String[] additionalParts = additionalPath.parts;

        final int numParts = pathParts.length;
        final int numAdditionalParts = additionalParts.length;

        final int totalNumParts = numParts + numAdditionalParts;

        final String[] parts = pathArrayAllocator.reallocate(path.parts, totalNumParts);

        System.arraycopy(additionalParts, 0, parts, numParts, numAdditionalParts);

        this.parts = parts;
    }

    final void initialize(BasePath path, CharSequence additionalPathName, IPathStringsAllocator pathArrayAllocator) {

        Objects.requireNonNull(path);
        Checks.isPathName(additionalPathName);
        Objects.requireNonNull(pathArrayAllocator);

        initialize(path.parts, additionalPathName, pathArrayAllocator);
    }

    final void initialize(CharSequence pathName, IPathStringsAllocator pathStringsAllocator) {

        Checks.isPathName(pathName);
        Objects.requireNonNull(pathStringsAllocator);

        this.parts = pathStringsAllocator.allocateArray(1);

        parts[0] = pathStringsAllocator.getString(pathName);
    }

    final void initialize(CharSequence[] pathNames, IPathStringsAllocator pathStringsAllocator) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);
        Objects.requireNonNull(pathStringsAllocator);

        this.parts = pathStringsAllocator.allocateArrayCopy(pathNames, pathStringsAllocator, (s, a) -> a.getString(s));
    }

    final void initialize(String[] pathNames, CharSequence additionalPathName, IPathStringsAllocator pathStringsAllocator) {

        Checks.isNotEmpty(pathNames);
        Checks.areElements(pathNames, Checks::checkIsPathName);
        Checks.isPathName(additionalPathName);
        Objects.requireNonNull(pathStringsAllocator);

        final int numPathNames = pathNames.length;

        this.parts = pathStringsAllocator.reallocate(pathNames, numPathNames + 1);

        parts[numPathNames] = pathStringsAllocator.getString(additionalPathName);
    }

    final void initialize(Path path, IPathStringsAllocator pathStringsAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(pathStringsAllocator);

        final int numPathNames = path.getNameCount();

        final String[] parts = this.parts = pathStringsAllocator.allocateArray(numPathNames);

        for (int i = 0; i < numPathNames; ++ i) {

            parts[i] = PathUtil.getFileName(path.getName(i));
        }
    }

    final <P> void relativize(BasePath path, BasePath subPath, IPathStringsAllocator pathArrayAllocator) {

        Objects.requireNonNull(path);
        Objects.requireNonNull(subPath);
        Objects.requireNonNull(pathArrayAllocator);

        final String[] pathParts = path.parts;
        final int numPathParts = pathParts.length;

        final String[] subPathParts = subPath.parts;
        final int numSubPathParts = subPathParts.length;

        final int numResultParts = numSubPathParts - numPathParts;

        if (numResultParts < 0) {

            throw new IllegalArgumentException();
        }

        for (int i = 0; i < numPathParts; ++ i) {

            if (!pathParts[i].equals(subPathParts[i])) {

                throw new IllegalArgumentException();
            }
        }

        this.parts = pathArrayAllocator.allocateArrayCopy(subPathParts, numPathParts, numResultParts);
    }

    final String getLastName() {

        return parts[parts.length - 1];
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
