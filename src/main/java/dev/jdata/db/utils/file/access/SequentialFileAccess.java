package dev.jdata.db.utils.file.access;

import java.io.DataOutputStream;

public interface SequentialFileAccess extends FileAccess {

    DataOutputStream getDataOutputStream();
}
