package shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents a client with a username, password, and badge.
 */
public class Client {
    private String username;
    private String password;
    transient private Boolean isLogged = false;
    transient private Badge badge;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLogged = false;
        this.badge = Badge.RECENSORE;  // Default badge
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(Boolean isLogged) {
        this.isLogged = isLogged;
    }

    public Badge getBadge() {
        return badge;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public String getNameBadge() {
        return badge.getName();
    }

    public static Client fromJson(String json) {
        return gson.fromJson(json, Client.class);
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public void updateBadge(int reviewCount) {
        if (reviewCount >= 20) {
            this.badge = Badge.CONTRIBUTORE_SUPER;
        } else if (reviewCount >= 15) {
            this.badge = Badge.CONTRIBUTORE_ESPERTO;
        } else if (reviewCount >= 10) {
            this.badge = Badge.CONTRIBUTORE;
        } else if (reviewCount >= 5) {
            this.badge = Badge.RECENSORE_ESPERTO;
        } else {
            this.badge = Badge.RECENSORE;
        }
    }
}
