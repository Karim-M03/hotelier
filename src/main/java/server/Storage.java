package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import shared.Client;
import shared.Couple;
import shared.Exceptions;
import shared.Hotel;
import shared.Ratings;
import shared.Review;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Storage {

    private static final String USER_FILE_PATH = "./src/main/resources/Users.json";
    private static final String HOTEL_FILE_PATH = "./src/main/resources/Hotels.json";
    private static final String REVIEW_FILE_PATH = "./src/main/resources/Reviews.json";

    private static Map<String, Client> users = new HashMap<>();
    private static Map<Integer, Hotel> hotels = new HashMap<>();  // Use hotelId as the key
    private static Map<Couple, Review> reviews = new HashMap<>();

    // Creare un oggetto Gson con pretty printing
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
            loadReviews();
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
            users = gson.fromJson(reader, type);
            if (users == null) {
                users = new HashMap<>();
            }
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading user file", e);
        }

        updateBadge();
       
    }

    private static void updateBadge(){
        Map<String, Integer> reviewCounts = new HashMap<>();
        if(reviews.isEmpty()){
            return;
        }
        for (Review review : reviews.values()) {
            reviewCounts.put(review.getReviewUsername(), reviewCounts.getOrDefault(review.getReviewUsername(), 0) + 1);
        }

        for (Map.Entry<String, Client> entry : users.entrySet()) {
            String username = entry.getKey();
            Client client = entry.getValue();
            int reviewCount = reviewCounts.getOrDefault(username, 0);
            client.updateBadge(reviewCount);
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
            List<Hotel> hotelList = gson.fromJson(reader, type);
            if (hotelList != null) {
                hotelList.forEach(hotel -> hotels.put(hotel.getId(), hotel));
            }
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
            List<Review> reviewList = gson.fromJson(reader, type);
            if (reviewList != null) {
                reviewList.forEach(review -> {
                    Couple couple = new Couple(review.getHotelId(), review.getReviewUsername());
                    reviews.put(couple, review);
                });
            }
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error reading review file", e);
        }
    }

    public static synchronized void addReview(Review review) {
        reviews.put(review.getCouple(), review);
        updateHotelRatings(review.getHotelId());
        updateBadge();
    }

    

    private static void updateHotelRatings(int hotelId) {
        List<Review> hotelReviews = reviews.values().stream()
            .filter(review -> review.getHotelId() == hotelId)
            .collect(Collectors.toList());

        int reviewCount = hotelReviews.size();

        if (reviewCount == 0) {
            return;
        }

        float generalSum = 0;
        float cleaningSum = 0;
        float positionSum = 0;
        float serviceSum = 0;
        float qualitySum = 0;

        for (Review review : hotelReviews) {
            generalSum += review.getGeneral();
            cleaningSum += review.getCleaning();
            positionSum += review.getPosition();
            serviceSum += review.getService();
            qualitySum += review.getQuality();
        }

        float newGeneralSum = generalSum / reviewCount;

        Ratings newRatings = new Ratings(
            cleaningSum / reviewCount,
            positionSum / reviewCount,
            serviceSum / reviewCount,
            qualitySum / reviewCount
        );

        Hotel hotel = hotels.get(hotelId);
        if (hotel != null) {
            hotel.setRatings(newRatings);
            hotel.setRate(newGeneralSum);
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
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving user file", e);
        }
    }

    private static void saveHotels() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(HOTEL_FILE_PATH)) {
            System.out.println("Saving hotels");
            gson.toJson(hotels.values(), writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving hotel file", e);
        }
    }

    private static void saveReviews() throws Exceptions.StorageException {
        try (Writer writer = new FileWriter(REVIEW_FILE_PATH)) {
            System.out.println("Saving reviews");
            gson.toJson(reviews.values(), writer);
        } catch (IOException e) {
            throw new Exceptions.StorageException("Error saving review file", e);
        }
    }

    public static Map<Integer, Hotel> getHotels() throws Exceptions.StorageException {
        return new HashMap<>(hotels);  // Return a copy to prevent external modification
    }

    public static Map<String, Client> getClients() throws Exceptions.StorageException {
        return new HashMap<>(users);  // Return a copy to prevent external modification
    }

    public static Map<Couple, Review> getReviews() throws Exceptions.StorageException {
        return new HashMap<>(reviews);  // Return a copy to prevent external modification
    }

    public static Client getClient(String username){
        return users.get(username);
    }

    public static Hotel getHotelById(int hotelId){
        return hotels.get(hotelId);
    }

    public static Hotel getHotelByName(String hotelName) {
        Hotel hotel = hotels.values().stream()
        .filter(hotell -> hotell.getName().equals(hotelName))
        .findFirst()
        .orElse(null);
        return hotel;
    }
}
