package client;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import shared.*;

/**
 * Classe ClientCLI per la gestione dell'interfaccia a riga di comando del client HOTELIER
 * 
 */
public class ClientCLI {
    
    private ClientController cc;
    private Scanner input;

    public ClientCLI() {
        this.input = new Scanner(System.in);
        boolean connected = false;
        while (!connected) {
            try {
                cc = new ClientController();
                connected = true; // If no exception, connection is successful
            } catch (Exceptions.ClientSocketException e) {
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
            if (!cc.isClientLogged()) {
                System.out.print("1 -> Register\n");
                System.out.print("2 -> Login\n");
                System.out.print("3 -> Search Hotel\n");
                System.out.print("4 -> Search All Hotels\n");
                System.out.print("5 -> Exit\n");
                System.out.print("Insert command: ");
            } else {
                System.out.print("Welcome " + cc.getClientUsername() + " to HOTELIER, here listed you can find the possible operations\n");
                System.out.print("1 -> Search Hotel\n");
                System.out.print("2 -> Search All Hotels\n");
                System.out.print("3 -> Insert Review\n");
                System.out.print("4 -> Show my Badges\n");
                System.out.print("5 -> Logout\n");
                System.out.print("6 -> Exit\n");
                System.out.print("Insert command: ");
            }

            int cmd;
            try {
                cmd = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                input.nextLine();
                continue;
            }

            if ((!cc.isClientLogged() && (cmd <= 0 || cmd > 5)) || (cc.isClientLogged() && (cmd <= 0 || cmd > 6))) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }

            switch (cmd) {
                case 1:
                    if (!cc.isClientLogged()) {
                        register();
                    } else {
                        searchHotel();
                    }
                    break;
                case 2:
                    if (!cc.isClientLogged()) {
                        login();
                    } else {
                        searchAllHotels();
                    }
                    break;
                case 3:
                    if (!cc.isClientLogged()) {
                        searchHotel();
                    } else {
                        // insertReview();
                    }
                    break;
                case 4:
                    if (!cc.isClientLogged()) {
                        searchAllHotels();
                    } else {
                        // showMyBadges();
                    }
                    break;
                case 5:
                    if (cc.isClientLogged()) {
                        logout();
                    } else {
                        exitApplication();
                    }
                    break;
                case 6:
                    if (cc.isClientLogged()) {
                        exitApplication();
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
        System.out.print("0 -> Back\n");
        System.out.print("Enter username: ");
        String username = input.nextLine();
        if (username.equals("0")) return;

        System.out.print("Enter password: ");
        String password = input.nextLine();
        if (password.equals("0")) return;

        try {
            cc.register(username, password);
            System.out.print("Registration successful \n\n\n");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void searchHotel() {
        System.out.print("-----------SEARCH HOTEL----------\n");
        System.out.print("0 -> Back\n");
        System.out.print("Enter Hotel Name: ");
        String hotelName = input.nextLine();
        if (hotelName.equals("0")) return;

        System.out.print("Enter City: ");
        String city = input.nextLine();
        if (city.equals("0")) return;

        try {
            Hotel hotel = cc.searchHotel(hotelName, city);
            if (hotel != null) System.out.print(hotel.toString());
        } catch (Exception e) {
            System.out.println("Search hotel failed: " + e.getMessage());
        }
    }

    private void login() {
        System.out.print("-----------LOGIN----------\n");
        System.out.print("0 -> Back\n");
        System.out.print("Enter username: ");
        String username = input.nextLine();
        if (username.equals("0")) return;

        System.out.print("Enter password: ");
        String password = input.nextLine();
        if (password.equals("0")) return;

        try {
            cc.login(username, password);
            System.out.println("Login successful");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void searchAllHotels() {
        System.out.print("-----------SEARCH ALL HOTELS----------\n");
        System.out.print("0 -> Back\n");
        System.out.print("Enter City: ");
        String city = input.nextLine();
        if (city.equals("0")) return;

        try {
            List<Hotel> hotels = cc.searchAllHotels(city);
            if (hotels != null) {
                for (Hotel hotel : hotels) {
                    System.out.println(hotel.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Search all hotels failed: " + e.getMessage());
        }
    }

    private void logout() {
        System.out.print("-----------LOGOUT----------\n");
        cc.logout();
        System.out.println("Logout successful");
    }

    private void exitApplication() {
        System.out.println("Exiting application.");
        if (input != null) {
            input.close();
        }
        System.exit(0);
    }
}
