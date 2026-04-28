package dev.jdata.db.utils.debug;

import java.util.function.Consumer;

import dev.jdata.db.utils.debug.PrintDebug.NameValueBuilder;

public interface IPrintDebug {

    default IPrintDebug debug(String message) {

        PrintDebug.debug(getClass(), message);

        return this;
    }

    default IPrintDebug debugFormatln(String format, Object ... parameters) {

        PrintDebug.debugFormatln(getClass(), format, parameters);

        return this;
    }

    default IPrintDebug debug(Consumer<NameValueBuilder> consumer) {

        PrintDebug.debug(getClass(), consumer);

        return this;
    }

    default IPrintDebug debug(String message, Consumer<NameValueBuilder> consumer) {

        PrintDebug.debug(getClass(), message, consumer);

        return this;
    }

    default IPrintDebug println(String message) {

        PrintDebug.println(getClass(), message);

        return this;
    }

    default IPrintDebug formatln(String format, Object ... parameters) {

        PrintDebug.formatln(getClass(), format, parameters);

        return this;
    }
}
