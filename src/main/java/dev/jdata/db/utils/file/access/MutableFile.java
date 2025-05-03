package dev.jdata.db.utils.file.access;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import dev.jdata.db.utils.checks.Checks;

@Deprecated // currently not in use
final class MutableFile extends File {

    private static final long serialVersionUID = 1L;

    private String name;

    MutableFile() {
        super("");
    }

    void initialize(String name) {

        if (this.name != null) {

            throw new IllegalStateException();
        }

        this.name = Checks.isPathName(name);
    }

    @Override
    public String getName() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getParent() {

        throw new UnsupportedOperationException();
    }

    @Override
    public File getParentFile() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAbsolute() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getAbsolutePath() {

        throw new UnsupportedOperationException();
    }

    @Override
    public File getAbsoluteFile() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String getCanonicalPath() throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public File getCanonicalFile() throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public URL toURL() throws MalformedURLException {

        throw new UnsupportedOperationException();
    }

    @Override
    public URI toURI() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canRead() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canWrite() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exists() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDirectory() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFile() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHidden() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long lastModified() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long length() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean createNewFile() throws IOException {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteOnExit() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String[] list() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String[] list(FilenameFilter filter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public File[] listFiles() {

        throw new UnsupportedOperationException();
    }

    @Override
    public File[] listFiles(FilenameFilter filter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public File[] listFiles(FileFilter filter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean mkdir() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean mkdirs() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean renameTo(File dest) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setLastModified(long time) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setReadOnly() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setWritable(boolean writable, boolean ownerOnly) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setWritable(boolean writable) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setReadable(boolean readable, boolean ownerOnly) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setReadable(boolean readable) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setExecutable(boolean executable, boolean ownerOnly) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setExecutable(boolean executable) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canExecute() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long getTotalSpace() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long getFreeSpace() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long getUsableSpace() {

        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(File pathname) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {

        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {

        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {

        throw new UnsupportedOperationException();
    }

    @Override
    public Path toPath() {

        throw new UnsupportedOperationException();
    }
}
