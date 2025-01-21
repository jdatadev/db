package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.network.NetworkClientSocket;

final class NetworkSession extends BaseSession {

    private final NetworkClientSocket clientSocket;

    NetworkSession(NetworkClientSocket clientSocket) {

        this.clientSocket = Objects.requireNonNull(clientSocket);
    }
}
