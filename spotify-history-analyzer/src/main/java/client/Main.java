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
            Registry registry = LocateRegistry.getRegistry("localhost",1099);
            
            SpotifyService stub = (SpotifyService) registry.lookup("Spotifyservice");
            Scanner input = new Scanner(System.in);
            System.out.println("Insert a year:");
            int anno = input.nextInt();

            List<StreamRecordDTO> result = stub.getSongsByYear(anno);
            System.out.println(result);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
}
}