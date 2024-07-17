package client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import server.Exceptions;

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

    public NetworkClient() throws Exceptions.ClientSocketException {
        try {
            // Attempt to connect to the server on localhost at port 9999
            socket = new Socket("localhost", 9999);
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

    public Response<Client> login(String username, String password){
        try{
            out.println("LOGIN|" + username + "|" + password);
            String responseJson = in.readLine();
            if (responseJson == null || responseJson.isEmpty()) {
                return new Response<>(false, 404, "No response from server or empty response.", null);
            }

            Type responseType = new TypeToken<Response<Client>>(){}.getType();
            Response<Client> response = new Gson().fromJson(responseJson, responseType);
            return response;
        }catch (IOException e) {
            System.err.println("Error during login.");
            e.printStackTrace();
            return new Response<>(false, 500, "Failed to connect or process response.", null);
        }
    }

    public Response<List<Hotel>> searchAllHotels(String city){
        try{
            out.println("SEARCHALLHOTELS|" + city);
            String responseJson = in.readLine();
            if (responseJson == null || responseJson.isEmpty()) {
                return new Response<>(false, 404, "No response from server or empty response.", null);
            }

            Type responseType = new TypeToken<Response<List<Hotel>>>(){}.getType();
            Response<List<Hotel>> response = new Gson().fromJson(responseJson, responseType);
            return response;
        }catch (IOException e) {
            System.err.println("Error during seach all hotels.");
            e.printStackTrace();
            return new Response<>(false, 500, "Failed to connect or process response.", null);
        }
        
    }

}
