package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkServer {
    
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public NetworkServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            threadPool = Executors.newFixedThreadPool(10);
            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT);
            e.printStackTrace();
        }
    }

    public Socket acceptClient() throws IOException {
        return serverSocket.accept();
    }

    public void handleClient(Socket clientSocket) {
        threadPool.execute(new ClientHandler(clientSocket));
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown(); // Disable new tasks from being submitted
                try {
                    // Wait a while for existing tasks to terminate
                    if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                        threadPool.shutdownNow(); // Cancel currently executing tasks
                        // Wait a while for tasks to respond to being cancelled
                        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                            System.err.println("Thread pool did not terminate");
                        }
                    }
                } catch (InterruptedException ie) {

                    threadPool.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error closing the server socket.");
            e.printStackTrace();
        }
    }
}
