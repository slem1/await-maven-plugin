package fr.sle.maven.plugins;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Handler for testing availability of a service on TCP. Ensures that a tcp connection can be opened on
 * specific host and port.
 *
 * @author slemoine
 */
public class TCPService implements Service {

    private final String host;

    private final Integer port;

    private final Socket socket;

    private final SocketAddress socketAddress;

    /**
     * Create new instance configured with {@code host} and {@code port}.
     *
     * @param host the host of the tcp service
     * @param port the port
     */
    public TCPService(final String host, final Integer port) {

        if (port == null) {
            throw new IllegalArgumentException("TCP port is mandatory");
        }

        if (host == null) {
            throw new IllegalArgumentException("TCP host is mandatory");
        }

        this.host = host;
        this.port = port;
        this.socket = new Socket();
        this.socketAddress = new InetSocketAddress(host, port);
    }

    @Override
    public String toString() {
        return String.format("%s:%d (TCP)", host, port);
    }


    @Override
    public void execute() throws ServiceUnavailableException {
        try {
            socket.connect(socketAddress);
        } catch (IOException e) {
            throw new ServiceUnavailableException(String.format("%s:%d is unreachable", host, port), e);
        }
    }
}
