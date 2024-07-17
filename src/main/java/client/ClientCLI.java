package client;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import server.Exceptions.ClientSocketException;
import shared.*;

/**
 * Classe ClientCLI per la gestione dell'interfaccia a riga di comando del client HOTELIER
 * 
 */
public class ClientCLI {
    
    private Client client;
    private ClientController cc;
    private Scanner input;
    public ClientCLI() {
        this.input = new Scanner(System.in);
        boolean connected = false;
        while (!connected) {
            try {
                cc = new ClientController();
                connected = true; // If no exception, connection is successful
            } catch (ClientSocketException e) {
                System.out.println("Can't connect to the server: " + e.getMessage());
                System.out.println("Retry? (yes/no)");
                String userResponse = input.nextLine();
                if (!userResponse.equalsIgnoreCase("yes")) {
                    System.out.println("Exiting application.");
                    if (input != null) {
                        input.close();
                    }
                    System.exit(1); // Exit the program if the user does not wish to retry
                }
            }
        }
    }

            
    
    
    
    public void run() {
        
        while (true) {
            if (client == null || !client.getIsLogged()) {
                System.out.print("1 -> Register\n");
                System.out.print("2 -> Login\n");
                System.out.print("3 -> Search Hotel\n");
                System.out.print("4 -> Search All Hotels\n");
                System.out.print("Insert command: ");
            } else {
                System.out.print("Welcome " + client.getUsername() + " to HOTELIER, here listed you can find the possible operations\n");
                System.out.print("1 -> Search Hotel\n");
                System.out.print("2 -> Search All Hotels\n");
                System.out.print("3 -> Insert Review\n");
                System.out.print("4 -> Show my Badges\n");
                System.out.print("5 -> Logout\n");
                System.out.print("Insert command: ");
            }
            int cmd;
            try{
                cmd = input.nextInt();
                input.nextLine();         
            }catch (InputMismatchException e){
                System.out.println("Invalid input. Please enter a valid integer.");
                 input.nextLine();
                continue;
            }
            
            
            if ((client == null || !client.getIsLogged()) && (cmd <= 0 || cmd > 4) || (client != null && client.getIsLogged() && (cmd <= 0 || cmd > 5))) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }
            
            switch (cmd) {
                case 1:
                    if (client == null || !client.getIsLogged()) {
                        register();
                    } else {
                        searchHotel();
                    }
                    break;
                case 2:
                    if (client == null || !client.getIsLogged()) {
                        login();
                    } else {
                        searchAllHotels();
                    }
                    break;
                case 3:
                    if (client == null || !client.getIsLogged()) {
                        searchHotel();
                    } else {
                        searchHotel();
                    }
                    break;
                case 4:
                    if (client == null || !client.getIsLogged()) {
                        searchAllHotels();
                    } else {
                        //showMyBadges();
                    }
                    break;
                case 5:
                    if (client != null && client.getIsLogged()) {
                        //logout();
                    }
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }
    
     private void register() {
        System.out.print("-----------REGISTRATION----------\n");
        String username = "";
        String password = "";

        while (username.isEmpty()) {
            System.out.print("Enter username: ");
            username = input.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            }
        }

        while (password.isEmpty()) {
            System.out.print("Enter password: ");
            password = input.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
            }
        }
        
        String result = cc.register(username, password);
        if(!result.isEmpty()){
            System.out.print(result + "\n");
            System.out.print("Insert another username \n");
            register();
        }else{
            System.out.print("Registration successfull \n\n\n");
        }
    }
     
    private void searchHotel(){
        System.out.print("-----------SEARCH HOTEL----------\n");
        String hotelName = "";
        String city = "";

        while (hotelName.isEmpty()) {
            System.out.print("Enter Hotel Name: ");
            hotelName = input.nextLine();
            if (hotelName.isEmpty()) {
                System.out.println("the Hotel name cannot be empty. Please try again.");
            }
        }

        while (city.isEmpty()) {
            System.out.print("Enter City: ");
            city = input.nextLine();
            if (city.isEmpty()) {
                System.out.println("City cannot be empty. Please try again.");
            }
        }
        
        Hotel hotel = cc.searchHotel(hotelName, city);
        if(hotel != null) System.out.print(hotel.toString());
       
   
    }
    
    private void login(){
        System.out.print("-----------LOGIN----------\n");
        String username = "";
        String password = "";

        while (username.isEmpty()) {
            System.out.print("Enter username: ");
            username = input.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty. Please try again.");
            }
        }

        while (password.isEmpty()) {
            System.out.print("Enter password: ");
            password = input.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
            }
        }

        Client client = cc.login(username, password);
        if(client != null){
            System.out.println("Login successfull");
            this.client = client;
        }
        
         
    }


    private void searchAllHotels(){
        System.out.print("-----------SEARCH ALL HOTELS----------\n");
        String city = "";

        while (city.isEmpty()) {
            System.out.print("Enter City: ");
            city = input.nextLine();
            if (city.isEmpty()) {
                System.out.println("City cannot be empty. Please try again.");
            }
        }


        List<Hotel> hotels = cc.searchAllHotels(city);
        if(hotels != null){
            for (Hotel hotel : hotels) {
                System.out.println(hotel.toString());
            }
        }

    }
     
     
    

    
}