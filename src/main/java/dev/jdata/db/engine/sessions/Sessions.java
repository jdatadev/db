package dev.jdata.db.engine.sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.engine.descriptorables.BaseMultiTypeDescriptorables;
import dev.jdata.db.network.NetworkClientSocket;
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.checks.Checks;

public final class Sessions extends BaseMultiTypeDescriptorables<BaseSession.SessionState, BaseSession> {

    private final List<BaseSession> sessions;

    public Sessions() {
        super(BaseSession[]::new, NetworkSession.class);

        this.sessions = new ArrayList<>();
    }

    public Session addSession(NetworkClientSocket clientSocket) {

        return addSession(NetworkSession.class, clientSocket, NetworkSession::new);
    }

    public Session getSession(int sessionId) {

        Checks.isSessionDescriptor(sessionId);

        return getDescriptorable(sessionId);
    }

    private <T> BaseSession addSession(Class<? extends BaseSession> typeToAllocate, T factoryParameter, DescriptorableFactory<T, BaseSession> descriptorableFactory) {

        final BaseSession session = addDescriptorable(typeToAllocate, factoryParameter, descriptorableFactory);

        addSessionToList(session);

        return session;
    }

    private void addSessionToList(BaseSession session) {

        Objects.requireNonNull(session);

        if (Lists.containsInstance(sessions, session)) {

            throw new IllegalArgumentException();
        }

        sessions.add(session);
    }
}
