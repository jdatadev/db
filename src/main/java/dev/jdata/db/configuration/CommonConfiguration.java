package dev.jdata.db.configuration;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;

public class CommonConfiguration {

    private final StringResolver stringResolver;

    public CommonConfiguration(StringResolver stringResolver) {

        this.stringResolver = Objects.requireNonNull(stringResolver);
    }

    protected CommonConfiguration(CommonConfiguration toCopy) {
        this(toCopy.stringResolver);
    }

    public final StringResolver getStringResolver() {
        return stringResolver;
    }
}
