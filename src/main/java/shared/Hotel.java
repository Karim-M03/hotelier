package shared;

import com.google.gson.Gson;
import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String description;
    private String city;
    private String phone;
    private List<String> services;
    private int rate;
    private Ratings ratings;

    public Hotel(int id, String name, String description, String city, String phone, List<String> services, int rate, Ratings ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.phone = phone;
        this.services = services;
        this.rate = rate;
        this.ratings = ratings;
    }

    // Getters and setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }
    public int getRate() { return rate; }
    public void setRate(int rate) { this.rate = rate; }
    public Ratings getRatings() { return ratings; }
    public void setRatings(Ratings ratings) { this.ratings = ratings; }

    public static Hotel fromJson(String json) {
        return new Gson().fromJson(json, Hotel.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
     public String toString() {
        return "Hotel Details:\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "City: " + city + "\n" +
                "Phone: " + phone + "\n" +
                "Services: " + String.join(", ", services) + "\n" +
                "Rate: " + rate + "\n" +
                "Ratings:\n" +
                "  Cleaning: " + ratings.getCleaning() + "\n" +
                "  Position: " + ratings.getPosition() + "\n" +
                "  Services: " + ratings.getServices() + "\n" +
                "  Quality: " + ratings.getQuality() + "\n";
    }
    
}