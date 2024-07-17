package server;

import java.io.IOException;
import java.net.Socket;

public class ServerController {
    private NetworkServer networkServer;

    public ServerController() {
        networkServer = new NetworkServer();
    }

    public void start() {
        System.out.println("Waiting for clients to connect...");
        while (true) {
            try{
                Socket clientSocket = networkServer.acceptClient();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                // Handle client connection using the thread pool
                networkServer.handleClient(clientSocket);
            } catch (IOException e) {
                System.err.println("Error accepting client connection");
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        networkServer.stop();
    }
}
