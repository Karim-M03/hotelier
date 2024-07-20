package shared;

import java.util.Date;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Review {
    private int hotelId;
    private String username;
    private int general;
    private int cleaning;
    private int position;
    private int service;
    private int quality;
    private Date reviewDate; 
    private String badge;

    // Crea un'istanza di Gson con pretty printing
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Costruttore completo
    public Review(String username, int hotelId, int general, int cleaning, int position, int service, int quality, Date reviewDate) {
        this.username = username;
        this.hotelId = hotelId;
        this.general = general;
        this.cleaning = cleaning;
        this.position = position;
        this.service = service;
        this.quality = quality;
        this.reviewDate = reviewDate;
    }

    public Review(String username, int hotelId, int general, int[] scores, String badge) {
        this.username = username;
        this.hotelId = hotelId;
        this.general = general;
        this.cleaning = scores[0];
        this.position = scores[1];
        this.service = scores[2];
        this.quality = scores[3];
        this.reviewDate = new Date();
        this.badge = badge;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getGeneral() {
        return general;
    }

    public void setGeneral(int general) {
        this.general = general;
    }

    public String getReviewUsername() {
        return username;
    }

    public int getCleaning() {
        return cleaning;
    }

    public void setCleaning(int cleaning) {
        this.cleaning = cleaning;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Couple getCouple(){
        return new Couple(hotelId, username); 
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public static Review fromJson(String json) {
        return gson.fromJson(json, Review.class);
    }

    public String toJson() {
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Review{" +
                "cleaning=" + cleaning +
                ", position=" + position +
                ", service=" + service +
                ", quality=" + quality +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
