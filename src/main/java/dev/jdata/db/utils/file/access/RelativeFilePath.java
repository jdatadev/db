package dev.jdata.db.utils.file.access;

public final class RelativeFilePath extends RelativePath implements IFilePath {

    static final RelativeFilePath ROOT = new RelativeFilePath();

    RelativeFilePath() {

    }

    @Override
    public final String getFileName() {

        return getLastName();
    }
}
