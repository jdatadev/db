package dev.jdata.db.engine.sessions;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.engine.sessions.DBSession.LargeObjectStorer;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.checks.Checks;

public final class Sessions extends BaseSingleTypeDescriptorables<DBSession.SessionState, DBSession> {

    private final LargeObjectStorer<IOException> largeObjectStorer;

    private final IHeapMutableIndexList<DBSession> sessions;

    public Sessions(LargeObjectStorer<IOException> largeObjectStorer) {
        super(AllocationType.HEAP, DBSession[]::new);

        this.largeObjectStorer = Objects.requireNonNull(largeObjectStorer);

        this.sessions = IHeapMutableIndexList.create(DBSession[]::new, 0);
    }

    public Session addSession(Charset charset) {

        Objects.requireNonNull(charset);

        final DBSession session = addDescriptorable(null, (a, p) -> new DBSession(a));

        session.initialize(charset, largeObjectStorer);

        addSessionToList(session);

        return session;
    }

    public Session getSession(int sessionId) {

        Checks.isSessionDescriptor(sessionId);

        return getDescriptorable(sessionId);
    }

    public void removeSession(int sessionId) {

        Checks.isSessionDescriptor(sessionId);

        final DBSession session = getDescriptorable(sessionId);

        session.clear();

        removeDescriptorable(session);
    }

    private void addSessionToList(DBSession session) {

        Objects.requireNonNull(session);

        if (sessions.containsInstance(session)) {

            throw new IllegalArgumentException();
        }

        sessions.addTail(session);
    }
}
