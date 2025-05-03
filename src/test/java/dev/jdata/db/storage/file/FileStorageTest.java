package dev.jdata.db.storage.file;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class FileStorageTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testParseSequenceNo() {

        assertThatThrownBy(() -> FileStorage.parseSequenceNo(null, "abc")).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0", null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0", "")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0", "a")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0", "ab")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0", "abd")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc,0", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc..0", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.01", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.0123", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.123 ", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.123d", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.d123 ", "abc")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> FileStorage.parseSequenceNo("abc.abc123 ", "abc")).isInstanceOf(IllegalArgumentException.class);

        assertThat(FileStorage.parseSequenceNo("abc.0", "abc")).isEqualTo(0);
        assertThat(FileStorage.parseSequenceNo("abc.1", "abc")).isEqualTo(1);
        assertThat(FileStorage.parseSequenceNo("abc.123", "abc")).isEqualTo(123);
    }
}
