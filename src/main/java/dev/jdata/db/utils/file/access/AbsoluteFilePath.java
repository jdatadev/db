package dev.jdata.db.utils.file.access;

public final class AbsoluteFilePath extends AbsolutePath implements IFilePath {

    @Override
    public String getFileName() {

        return getLastName();
    }
}
