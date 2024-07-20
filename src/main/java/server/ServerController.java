package server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerController {
    private NetworkServer networkServer;
    private volatile boolean running = true; // Aggiungi una variabile per controllare lo stato del server

    public ServerController() {
        networkServer = new NetworkServer();
    }

    public void start() {
        System.out.println("Waiting for clients to connect...");
        while (running) {
            try {
                Socket clientSocket = networkServer.acceptClient();
                if (!running) {
                    clientSocket.close(); // Chiudi il clientSocket se il server è stato fermato
                    break;
                }
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
                // Handle client connection using the thread pool
                networkServer.handleClient(clientSocket);
            } catch (SocketException e) {
                if (running) {
                    System.err.println("Error accepting client connection");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.println("Error accepting client connection");
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        networkServer.stop();
        System.out.println("Server stopped.");
    }
}
