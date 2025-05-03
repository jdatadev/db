package dev.jdata.db.utils.file.access;

import dev.jdata.db.utils.adt.strings.Strings;
import dev.jdata.db.utils.file.access.BasePath.IPathStringsAllocator;

public interface IPathObjectsAllocator extends IPathStringsAllocator, IPathImplAllocator {

    public static final IPathObjectsAllocator HEAP_ALLOCATION = new IPathObjectsAllocator() {

        @Override
        public PathImpl allocatePathImpl() {

            return new PathImpl();
        }

        @Override
        public void freePathImpl(PathImpl pathImpl) {

        }

        @Override
        public String[] allocateArray(int minimumCapacity) {

            return new String[minimumCapacity];
        }

        @Override
        public void freeArray(String[] array) {

        }

        @Override
        public String getString(CharSequence charSequence) {

            return charSequence instanceof String ? (String)charSequence : Strings.of(charSequence);
        }
    };
}
