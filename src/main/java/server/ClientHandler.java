package server;

import com.google.gson.Gson;
import shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                try {
                    String[] parts = request.split("\\|", 2);
                    String command = parts[0].trim();
                    String data = parts.length > 1 ? parts[1] : "";

                    switch (command.toUpperCase()) {
                        case "REGISTER":
                            register(data, out);
                            break;
                        case "SEARCHHOTEL":
                            searchHotel(data, out);
                            break;
                        case "LOGIN":
                            login(data, out);
                            break;
                        case "SEARCHALLHOTELS":
                            searchAllHotels(data, out);
                            break;
                        case "INSERTREVIEW":
                            insertReview(data, out);
                            break;
                        case "SHOWMYBADGE": 
                            showMyBadge(data, out);
                            break;
                        
                        default:
                            out.println(new Gson().toJson(new Response<String>(false, 400, "Unknown command", null)));
                            break;
                    }
                } catch (Exception e) {
                    out.println(new Gson().toJson(new Response<String>(false, 500, "Error processing request: " + e.getMessage(), null)));
                }
            }
        } catch (SocketException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error handling client connection");
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Socket closed successfully.");
                }
            } catch (IOException e) {
                System.err.println("Failed to close socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void register(String data, PrintWriter out) {
        String[] parts = data.split("\\|");

        if (parts.length < 2) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Invalid registration data", null)));
            return;
        }

        String username = parts[0].trim();
        String password = parts[1].trim();

        try {
            if (Storage.userExists(username)) {
                out.println(new Gson().toJson(new Response<String>(false, 409, "Username already exists.", null)));
            } else {
                Storage.addUser(username, password);
                out.println(new Gson().toJson(new Response<String>(true, 200, "Registration successful for user: " + username, null)));
            }
        } catch (Exceptions.StorageException e) {
            out.println(new Gson().toJson(new Response<String>(false, 500, "Error accessing storage: " + e.getMessage(), null)));
        }
    }

    private void searchHotel(String data, PrintWriter out) {
        String[] parts = data.split("\\|");

        if (parts.length < 2) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Invalid hotel search data", null)));
            return;
        }

        String hotelName = parts[0].trim();
        String city = parts[1].trim();

        if (hotelName.isEmpty() || city.isEmpty()) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Hotel name or city cannot be empty", null)));
            return;
        }

        Hotel foundHotel = Storage.getHotelByName(hotelName);
        if (foundHotel != null && foundHotel.getCity().equalsIgnoreCase(city)) {
            out.println(new Gson().toJson(new Response<Hotel>(true, 200, "Hotel found", foundHotel)));
        } else {
            out.println(new Gson().toJson(new Response<String>(false, 404, "Hotel not found", null)));
        }
    }


    private void login(String data, PrintWriter out){
        String[] parts = data.split("\\|");

        if (parts.length < 2) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Invalid login search data", null)));
            return;
        }

        String username = parts[0].trim();
        String password = parts[1].trim();

        if (username.isEmpty() || password.isEmpty()) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "username or password cannot be empty", null)));
            return;
        }
        try {
            Map<String, Client> user = Storage.getClients();
            System.out.println(user);
            Client foundUser = user.get(username);
            System.out.println("Login");

            if (foundUser != null && foundUser.getPassword().equals(password)) {
                out.println(new Gson().toJson(new Response<Client>(true, 200, "User found", foundUser)));
            } else {
                System.out.print("User found");
                out.println(new Gson().toJson(new Response<String>(false, 404, "User not found", null)));
            }
        } catch (Exceptions.StorageException e) {
            out.println(new Gson().toJson(new Response<String>(false, 500, "Error accessing storage: " + e.getMessage(), null)));
        }


       

    }

    private void searchAllHotels(String data, PrintWriter out) {
        try {

            Map<Integer, Hotel> hotelMap = Storage.getHotels();
            List<Hotel> filteredHotels = hotelMap.values().stream()
                                            .filter(hotel -> hotel.getCity().equalsIgnoreCase(data))
                                            .collect(Collectors.toList());


    
            if (!filteredHotels.isEmpty()) {
                out.println(new Gson().toJson(new Response<List<Hotel>>(true, 200, "Hotels found", filteredHotels)));
            } else {
                out.println(new Gson().toJson(new Response<String>(false, 404, "No hotels found in " + data, null)));
            }
        } catch (Exceptions.StorageException e) {
            out.println(new Gson().toJson(new Response<String>(false, 500, "Error accessing storage: " + e.getMessage(), null)));
        }
    }

    private void insertReview(String data, PrintWriter out) {
        String[] parts = data.split("\\|");

        if (parts.length < 8) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Invalid review data", null)));
            return;
        }
        String username = parts[0].trim();
        String hotelName = parts[1].trim();
        String city = parts[2].trim();
        int generalReview = Integer.parseInt(parts[3].trim());
        int[] scores = new int[4];
        for (int i = 0; i < 4; i++) {
            scores[i] = Integer.parseInt(parts[i + 4].trim());
        }

        Client user = Storage.getClient(username);
        Hotel hotel = Storage.getHotelByName(hotelName);
        
        if (hotel != null && hotel.getCity().equalsIgnoreCase(city) && user != null) {
            Review review = new Review(username, hotel.getId(), generalReview, scores, user.getBadge().getName());
            Storage.addReview(review);
            out.println(new Gson().toJson(new Response<String>(true, 200, "Review added for hotel: " + hotelName, null)));
        } else if (hotel == null) {
            out.println(new Gson().toJson(new Response<String>(false, 404, "Hotel not found", null)));
        } else if(user == null){
            out.println(new Gson().toJson(new Response<String>(false, 401, "Not authenticated", null)));
        } else {
            out.println(new Gson().toJson(new Response<String>(false, 500, "Error adding review", null)));
        }
       
    }


    private void showMyBadge(String data, PrintWriter out){
        String[] parts = data.split("\\|");

        if (parts.length < 1) {
            out.println(new Gson().toJson(new Response<String>(false, 400, "Invalid badge data", null)));
            return;
        }

        String username = parts[0].trim();

        Client user = Storage.getClient(username);
        if (user != null) {
            out.println(new Gson().toJson(new Response<String>(true, 200, "Badge found", user.getBadge().getName())));
        } else {
            out.println(new Gson().toJson(new Response<String>(false, 401, "Not authenticated", null)));
        }
    }


    

}
