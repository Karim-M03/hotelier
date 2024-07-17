package shared;

import java.time.LocalDate;

import com.google.gson.Gson;

public class Review {
    private int hotelId;
    private int general;
    private int cleaning;
    private int position;
    private int service;
    private int quality;
    private LocalDate reviewDate; // La data della recensione è importante per l'attualità.

    // Costruttore completo
    public Review(int hotelId, int general, int cleaning, int position, int service, int quality, LocalDate reviewDate) {
        this.hotelId = hotelId;
        this.general = general;
        this.cleaning = cleaning;
        this.position = position;
        this.service = service;
        this.quality = quality;
        this.reviewDate = reviewDate;
    }

    public int getHotelId(){
        return hotelId;
    }

    public void setHotelId(int hotelId){
        this.hotelId = hotelId;
    }

    public int getGeneral(){
        return general;
    }

    public void setGeneral(int general){
        this.general = general;
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

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public static Review fromJson(String json) {
        return new Gson().fromJson(json, Review.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
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
