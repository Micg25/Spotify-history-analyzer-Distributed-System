package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import common.SpotifyService;

public class Server {
    public static void main(String[] args) {
        try{
        SpotifyServiceImpl obj = new SpotifyServiceImpl(); 
        
        SpotifyService stub = (SpotifyService) UnicastRemoteObject.exportObject(obj, 0, null, null);
        
        Registry registry = LocateRegistry.createRegistry(1099);

        registry.rebind("Spotifyservice", stub);

        System.out.println("Rmi server ready on port 1099");
        
} catch (RemoteException e){
    e.printStackTrace();
}
}
}