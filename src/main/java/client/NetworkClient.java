package client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.lang.reflect.Type;
import shared.*;

public class NetworkClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public NetworkClient() {
        try {
            socket = new Socket("localhost", 9999);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to the server on " + socket.getInetAddress().getHostName());
        } catch (IOException e) {
            System.err.println("Failed to connect to the server on localhost:9999");
            e.printStackTrace();
        }
    }

    public void close() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                System.err.println("Failed to close the socket.");
                e.printStackTrace();
            }
        }
    }

    public Response<String> register(String username, String password) {
        try {
            out.println("REGISTER|" + username + "|" + password);
            String responseJson = in.readLine();
            Type responseType = new TypeToken<Response<String>>(){}.getType();
            return new Gson().fromJson(responseJson, responseType);
        } catch (IOException e) {
            System.err.println("Error during registration.");
            e.printStackTrace();
            return new Response<String>(false, 404, "Failed to connect or process response.");
        }
    }

     public Response<Hotel> searchHotel(String hotelName, String city) {
        try {
            out.println("SEARCHHOTEL|" + hotelName + "|" + city);
            String responseJson = in.readLine();
            if (responseJson == null || responseJson.isEmpty()) {
                return new Response<>(false, 404, "No response from server or empty response.", null);
            }
            Type responseType = new TypeToken<Response<Hotel>>(){}.getType();
            Response<Hotel> response = new Gson().fromJson(responseJson, responseType);
            return response;
        } catch (IOException e) {
            System.err.println("Error during searching for a hotel.");
            e.printStackTrace();
            return new Response<>(false, 500, "Failed to connect or process response.", null);
        }
    }

}
