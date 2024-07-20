package shared;

public class Couple {
    private int hotelId;
    private String username;

    public Couple(int hotelId, String username) {
        this.hotelId = hotelId;
        this.username = username;
    }

    public int getHotelId() {
        return hotelId;
    }

    public String getUsername() {
        return username;
    }
}