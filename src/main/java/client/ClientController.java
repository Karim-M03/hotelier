/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;
import shared.*;

/**
 *
 * @author kappa
 */
public class ClientController {
    
    private final NetworkClient network;
    
    public ClientController(){
        network = new NetworkClient();
    }
    
    protected String register(String username, String password){
        
        Response response = network.register(username, password);
        if(response == null || !response.isSuccess()){
            return response.getResponseMessage();
        }
        return "";
        
        
    }
    
   protected Hotel searchHotel(String hotelName, String city) {
        Response<Hotel> response = network.searchHotel(hotelName, city);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            if (response.getResponseCode() == 404) {
                System.out.println("Hotel not found: " + hotelName + " in " + city);
            } else {
                System.err.println("Error searching for hotel: " + response.getResponseMessage());
            }
            return null;
        }
    }
    
    
    
    
}
