package client;


import java.rmi.RemoteException;
import java.util.Scanner;

import common.SessionDTO;
import common.SpotifyService;
import common.Token;

/**
 * Client is a class on the client-side. Client holds the state of the session.
 */
public class Client {
    private String user;
    private SpotifyService service;
    private SessionDTO session;
    public Client(String user, SpotifyService serv) {
        this.user = user;
        service = serv;
        this.session = new SessionDTO();
    }

    public void run() {
        Scanner input = new Scanner(System.in); 
    try{
        Token token = service.login(user);
        System.out.println(""+token+user);

        
            // 2. Ciclo del Menu
            boolean running = true;
            while (running) {
                System.out.println("\n--- MENU ---");
                System.out.println("1. Login");
                System.out.println("2. How many songs have you listened in a specific year");
                System.out.println("3. Show History");
                System.out.println("0. Exit");
                System.out.print("Choice: ");

                if (input.hasNextInt()) {
                    int choice = input.nextInt();

                    switch (choice) {
                        case 1 -> {      
                            System.out.print("\n username: ");
                            user = input.next();
                            token = service.login(user);
                            }
                        case 2 -> {
                            System.out.print("Insert a year: ");
                            if (input.hasNextInt()) {
                                int anno = input.nextInt();
                                String opid="2"+String.valueOf(anno);
                                try {
                                    
                                    session  = service.getSongsByYear(token, anno, session); 
                                    
                                    if (session.currentResult().isEmpty()) {
                                        if(session.history().containsKey(opid)){
                                            String oldMessage = session.history().get(opid);
                                            System.out.println("CACHE HIT!: "+oldMessage);
                                            break;
                                        }
                                        System.out.println("In the year " + anno + " no songs found.");
                                    } else {
                                        System.out.println("[" + user +"]" + "In the year " + anno + " you've listened to " + session.currentResult().size() + " songs!");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Server Error: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Invalid year format.");
                                input.next(); // Pulisce buffer
                            }
                        }
                        
                        case 3 -> {
                            
                            for(String result : service.showHistory(session).values()){
                                System.out.println(result);
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

        }
        catch (RemoteException e) {
            System.err.println("RMI ERROR: " + e.getMessage());
        }
         catch (Exception e) {
            e.printStackTrace();
        }




    }
}

