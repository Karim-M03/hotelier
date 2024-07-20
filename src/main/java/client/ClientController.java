package client;

import java.util.List;
import shared.*;

/**
 * ClientController per gestire le operazioni di rete e lo stato del client
 */
public class ClientController {

    private final NetworkClient network;
    private Client client;

    public ClientController() throws Exceptions.ClientSocketException {
        network = new NetworkClient();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> network.close()));
    }

    public boolean isClientLogged() {
        return client != null && client.getIsLogged();
    }

    public String getClientUsername() {
        return client != null ? client.getUsername() : null;
    }

    public void register(String username, String password) throws Exception {
        Response<String> response = network.register(username, password);
        if (response == null || !response.isSuccess()) {
            throw new Exception(response != null ? response.getResponseMessage() : "Unknown error");
        }
    }

    public Hotel searchHotel(String hotelName, String city) throws Exception {
        Response<Hotel> response = network.searchHotel(hotelName, city);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new Exception(response.getResponseMessage());
        }
    }

    public void login(String username, String password) throws Exception {
        Response<Client> response = network.login(username, password);
        if (response.isSuccess()) {
            client = response.getData();
            client.setIsLogged(true);
        } else {
            throw new Exception(response.getResponseMessage());
        }
    }

    public List<Hotel> searchAllHotels(String city) throws Exception {
        Response<List<Hotel>> response = network.searchAllHotels(city);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new Exception(response.getResponseMessage());
        }
    }

    public void insertReview(String hotelName, String city, int generalScore, int[] scores) throws Exception {
        Response<String> response = network.insertReview(client.getUsername(), hotelName, city, generalScore, scores);
        if(!response.isSuccess()){
            throw new Exception(response.getResponseMessage());
        }
    }

    public String showMyBadge() throws Exception{
        Response<String> response = network.showMyBadge(client.getUsername());
        if(!response.isSuccess()){
            throw new Exception(response.getResponseMessage());
        }else{
            return response.getData();
        }
    }

    public void logout() {
        client = null;
    }
}
