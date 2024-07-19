package client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.lang.reflect.Type;
import shared.*;

public class NetworkClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ConfigManager configManager = new ConfigManager();
    private final String HOST = configManager.getServerHost();
    private final int PORT = configManager.getServerPort();

    public NetworkClient() throws Exceptions.ClientSocketException {
        try {
            // Attempt to connect to the server on localhost at port 9999
            socket = new Socket(HOST, PORT);
            // Initialize PrintWriter for sending data to the server
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            // Initialize BufferedReader for receiving data from the server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            // Handle unknown host exceptions specifically if you need to
            throw new Exceptions.ClientSocketException("Cannot connect to unknown host: localhost", e);
        } catch (IOException e) {
            // Handle IOException for any IO errors that occur while setting up the streams or socket
            throw new Exceptions.ClientSocketException("Error setting up input and output streams", e);
        }
    }

    public void close() {
        
        System.out.println("Closing");
        if (out != null) {
            out.close();  
        }

        // Close the input stream if it was opened
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                System.err.println("Failed to close the input stream.");
                e.printStackTrace();
            }
        }

        if (out != null) {out.close();}

        // Finally, close the socket
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close the socket.");
                e.printStackTrace();
            }
        }
    }

    public Response<String> register(String username, String password) throws IOException {
        out.println("REGISTER|" + username + "|" + password);
        String responseJson = in.readLine();
        Type responseType = new TypeToken<Response<String>>(){}.getType();
        return new Gson().fromJson(responseJson, responseType);
    }

    public Response<Hotel> searchHotel(String hotelName, String city) throws IOException {
        out.println("SEARCHHOTEL|" + hotelName + "|" + city);
        String responseJson = in.readLine();
        if (responseJson == null || responseJson.isEmpty()) {
            return new Response<>(false, 404, "No response from server or empty response.", null);
        }
        Type responseType = new TypeToken<Response<Hotel>>(){}.getType();
        Response<Hotel> response = new Gson().fromJson(responseJson, responseType);
        return response;
    }

    public Response<Client> login(String username, String password) throws IOException {
        out.println("LOGIN|" + username + "|" + password);
        String responseJson = in.readLine();
        if (responseJson == null || responseJson.isEmpty()) {
            return new Response<>(false, 404, "No response from server or empty response.", null);
        }

        Type responseType = new TypeToken<Response<Client>>(){}.getType();
        Response<Client> response = new Gson().fromJson(responseJson, responseType);
        return response;
    }

    public Response<List<Hotel>> searchAllHotels(String city) throws IOException {
        out.println("SEARCHALLHOTELS|" + city);
        String responseJson = in.readLine();
        if (responseJson == null || responseJson.isEmpty()) {
            return new Response<>(false, 404, "No response from server or empty response.", null);
        }

        Type responseType = new TypeToken<Response<List<Hotel>>>(){}.getType();
        Response<List<Hotel>> response = new Gson().fromJson(responseJson, responseType);
        return response;
    }
}
