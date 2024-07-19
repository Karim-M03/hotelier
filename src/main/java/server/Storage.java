package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.Client;
import shared.Exceptions;
import shared.Hotel;
import shared.Review;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
    private static final String USER_FILE_PATH = "./src/main/resources/Users.json";
    private static final String HOTEL_FILE_PATH = "./src/main/resources/Hotels.json";
    private static final String REVIEW_FILE_PATH = "./src/main/resources/Reviews.json";

    private static Map<String, Client> users = new HashMap<>();
    private static Map<String, Hotel> hotels = new HashMap<>();
    private static Map<Integer, Review> reviews = new HashMap<>();

    static {
        load();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                saveAll();
            } catch (Exceptions.StorageException e) {
                Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, "Failed to save data on shutdown", e);
            }
        }));
    }

    public static void load() {
        try {
            loadUsers();
            loadHotels();
            //loadReviews();
        } catch (Exceptions.StorageException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized boolean addUser(String username, String password) throws Exceptions.StorageException {
        if (users.containsKey(username)) {
            return false;
        }
        Client newUser = new Client(username, password);
        users.put(username, newUser);
        return true;
    }

    public static boolean userExists(String username) {
        return users.containsKey(username);
    }

    private static void loadUsers() throws Exceptions.StorageException {
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + USER_FILE_PATH);
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Client>>() {}.getType();
            users = new Gson().fromJson(reader, type);
            if (users == null) {
                users = new HashMap<>();
            }
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading user file", e);
        }
    }

    private static void loadHotels() throws Exceptions.StorageException {
        File file = new File(HOTEL_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + HOTEL_FILE_PATH);
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Hotel>>() {}.getType();
            List<Hotel> hotelList = new Gson().fromJson(reader, type);
            if(hotelList != null)
            hotelList.forEach(hotel -> hotels.put(hotel.getName(), hotel));
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading hotel file", e);
        }
    }

    private static void loadReviews() throws Exceptions.StorageException {
        File file = new File(REVIEW_FILE_PATH);
        if (!file.exists()) {
            System.out.println("File does not exist: " + REVIEW_FILE_PATH);
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Review>>() {}.getType();
            List<Review> reviewList = new Gson().fromJson(reader, type);
            if(reviewList != null)
            reviewList.forEach(review -> reviews.put(review.getHotelId(), review));
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading review file", e);
        }
    }

    public static void saveAll() throws Exceptions.StorageException {
        saveUsers();
        saveHotels();
        saveReviews();
    }

    private static void saveUsers() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(USER_FILE_PATH)) {
            System.out.println("Saving users");
            new Gson().toJson(users, writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving user file", e);
        }
    }

    private static void saveHotels() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(HOTEL_FILE_PATH)) {
            System.out.println("Saving hotels");

            new Gson().toJson(hotels.values(), writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving hotel file", e);
        }
    }

    private static void saveReviews() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(REVIEW_FILE_PATH)) {
            System.out.println("Saving revies");
            new Gson().toJson(reviews.values(), writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving review file", e);
        }
    }



    public static Map<String, Hotel> getHotels() throws Exceptions.StorageException {
        return new HashMap<>(hotels);  // Return a copy to prevent external modification
    }

    public static Map<String, Client> getClients() throws Exceptions.StorageException {
        return new HashMap<>(users);  // Return a copy to prevent external modification
    }

    public static Map<Integer, Review> getReviews() throws Exceptions.StorageException {
        return new HashMap<>(reviews);  // Return a copy to prevent external modification
    }

    
}
