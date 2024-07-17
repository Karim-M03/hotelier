package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.Hotel;

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

    private static Map<String, String> users = new HashMap<>();
    private static Map<String, Hotel> hotels = new HashMap<>();

    static {
        try {
            loadUsers();
            loadHotels();
        } catch (Exceptions.StorageException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized boolean addUser(String username, String password) throws Exceptions.StorageException {
        if (users.containsKey(username)) {
            return false;
        }
        System.out.println("Adding user: Username: " + username + ", Password: " + password);
        users.put(username, password);
        saveUsers();
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
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            users = new Gson().fromJson(reader, type);
            if (users == null) {
                users = new HashMap<>();
            }
            System.out.println("Loaded users: " + users);
        } catch (FileNotFoundException e) {
            throw new Exceptions.StorageException("User file not found", e);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading user file", e);
        }
    }

    private static void saveUsers() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(USER_FILE_PATH)) {
            new Gson().toJson(users, writer);
            System.out.println("Saved users: " + users);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving user file", e);
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
            if (hotelList != null) {
                for (Hotel hotel : hotelList) {
                    hotels.put(hotel.getName(), hotel);
                }
            } else {
                hotels = new HashMap<>();  // Ensuring hotels is not null even if the JSON is empty
            }
            System.out.println("Loaded hotels: " + hotels);
        } catch (FileNotFoundException e) {
            throw new Exceptions.StorageException("Hotel file not found", e);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading hotel file", e);
        }
    }

    public static Map<String, Hotel> getHotels() throws Exceptions.StorageException {
        return new HashMap<>(hotels);  // Return a copy to prevent external modification
    }
}
