package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import common.SpotifyService;
import common.StreamRecordDTO;


public class Main {
    public static void main(String[] args) {
        try {
            // 1. Connessione RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            SpotifyService stub = (SpotifyService) registry.lookup("Spotifyservice");
            
            Scanner input = new Scanner(System.in);
            

            
            // 2. Ciclo del Menu
            boolean running = true;
            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. How many songs have you listened in a specific year");
                System.out.println("0. Exit");
                System.out.print("Choice: ");

                if (input.hasNextInt()) {
                    int choice = input.nextInt();

                    switch (choice) {
                        case 1 -> {
                            System.out.print("Insert a year: ");
                            if (input.hasNextInt()) {
                                int anno = input.nextInt();
                                try {
                                    
                                    List<StreamRecordDTO> result = stub.getSongsByYear(anno); 
                                    
                                    if (result.isEmpty()) {
                                        System.out.println("In the year " + anno + " no songs found.");
                                    } else {
                                        System.out.println("In the year " + anno + " you've listened to " + result.size() + " songs!");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Server Error: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Invalid year format.");
                                input.next(); // Pulisce buffer
                            }
                        }
                        
                        case 0 -> {
                            System.out.println("Exiting...");
                            running = false;
                        }

                        default -> System.out.println("Invalid choice, try again.");
                    }
                } else {
                    // Se l'utente scrive lettere invece di numeri nel menu
                    String junk = input.next();
                    System.out.println("Invalid input: " + junk);
                }
            } // Fine while
            
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}