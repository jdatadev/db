package dev.jdata.db.utils.file.access;

public final class RelativeDirectoryPath extends RelativePath implements IDirectoryPath<RelativeFilePath, RelativeDirectoryPath> {

    public static RelativeDirectoryPath ROOT = new RelativeDirectoryPath();
}
