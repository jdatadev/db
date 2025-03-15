package dev.jdata.db.engine.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.DBException;
import dev.jdata.db.engine.database.ExecuteException;
import dev.jdata.db.engine.server.NoSuchDatabaseException;
import dev.jdata.db.engine.server.network.DatabaseNetworkServer.LargeObjectState;
import dev.jdata.db.engine.server.network.DatabaseNetworkServer.ProtocolMessageResponseWriter;
import dev.jdata.db.engine.server.network.protocol.ProtocolDecodeException;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.StateObject;
import dev.jdata.db.utils.checks.Checks;

final class NetworkClient extends StateObject<NetworkClient.NetworkClientState> {

    static enum NetworkClientState implements State {

        CREATED(true),
        CONNECTED(false),
        SETUP(false),
        RECEIVING_LARGE_OBJECT(false),
        DISCONNECTED(true);

        private final boolean initializable;

        private NetworkClientState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private final DatabaseNetworkServer server;

    private final LargeObjectState largeObjectState;

    private final ProtocolMessageResponseWriter<RuntimeException> responseWriter;

    private int databaseId;
    private int sessionId;

    NetworkClient(DatabaseNetworkServer server) {
        super(NetworkClientState.CREATED, false);

        this.server = Objects.requireNonNull(server);

        this.largeObjectState = new LargeObjectState();
        this.responseWriter = null;
    }

    int getDatabaseId() {
        return databaseId;
    }

    int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    void initialize(int databaseId, int sessionId) {

        if (getState() != NetworkClientState.CONNECTED) {

            throw new IllegalStateException();
        }

        this.databaseId = Checks.isDatabaseId(databaseId);
        this.sessionId = Checks.isSessionDescriptor(sessionId);
    }

    int isCompleteMessage(ByteBuffer byteBuffer, int offset, int length) {

        return server.isCompleteMessage(byteBuffer, offset, length);
    }

    void onConnect() {

        checkIsInitializable();

        setState(NetworkClientState.CONNECTED);
    }

    void onMessage(ByteBuffer byteBuffer, int offset, int length) {

        try {
            onMessageThrowException(byteBuffer, offset, length);
        }
        catch (DBException | ParserException | IOException ex) {

            throw new IllegalStateException(ex);
        }
    }

    private void onMessageThrowException(ByteBuffer byteBuffer, int offset, int length)
            throws ProtocolDecodeException, NoSuchDatabaseException, ExecuteException, ParserException, IOException {

        switch (getState()) {

        case CONNECTED:

            server.onSetup(byteBuffer, offset, length, this);

            setState(NetworkClientState.SETUP);
            break;

        case SETUP:

            final boolean completeMessage = server.onMessage(databaseId, sessionId, byteBuffer, offset, length, largeObjectState, responseWriter);

            if (!completeMessage) {

                setState(NetworkClientState.RECEIVING_LARGE_OBJECT);
            }
            break;

        case RECEIVING_LARGE_OBJECT:

            final boolean isFinal = server.onMessageContinuation(length, length, byteBuffer, offset, length, largeObjectState);

            if (isFinal) {

                setState(NetworkClientState.SETUP);
            }
            break;

        default:
            throw new IllegalStateException();
        }
    }

    void onDisconnect() {

        server.onDisconnect(this);
    }
}
