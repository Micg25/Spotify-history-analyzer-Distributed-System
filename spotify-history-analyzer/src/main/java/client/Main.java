package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import common.SpotifyService;
public class Main {
    public static void main(String[] args) {
        try {
        // 1. Connessione RMI
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        SpotifyService stub = (SpotifyService) registry.lookup("Spotifyservice");
        Scanner input = new Scanner(System.in);
        System.out.print("\n Username: ");
        String user = input.next();
        Client test = new Client(user, stub);
        test.run();
        } 
        catch (Exception e) {
            System.out.println(e);
        }

}
}