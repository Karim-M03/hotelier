/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author kappa
 */
public class ServerMain {
    
    public static void main(String[] args){
        ServerController server = new ServerController();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received SIGINT, shutting down...");
            server.stop();  
        }));
        
        server.start();

        
    }
    
}
