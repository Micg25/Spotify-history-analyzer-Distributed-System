package server;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import common.SpotifyService;
import common.StreamRecordDTO;

public class SpotifyServiceImpl extends UnicastRemoteObject implements SpotifyService{

    public SpotifyServiceImpl() throws RemoteException{
        super();
    }

    public List<StreamRecordDTO> getSongsByYear(int anno){
        
        Gson gson = new Gson();
        String folderPath = "Spotify Extended Streaming History";
        File folder = new File (folderPath);
        List<StreamRecordDTO> filteredSongs = new ArrayList<>();

        if(!folder.exists() || !folder.isDirectory()){
            System.out.println("Errore: Cartella non trovata: " + folder.getAbsolutePath());
            return filteredSongs;
        }
        File[] listOfFiles = folder.listFiles();
        List<String> targetFiles = new ArrayList<>();
        System.out.println("Sono stati trovati "+listOfFiles.length+" file");
        
        if(listOfFiles != null){
            for (int i=0; i<listOfFiles.length; i++){     
                if(listOfFiles[i].getName().startsWith("Streaming_History_Audio_") && listOfFiles[i].getName().contains(String.valueOf(anno))){
                    targetFiles.add(listOfFiles[i].getName());
                    try(Reader reader = new FileReader(listOfFiles[i].getAbsolutePath())){
                        StreamRecordDTO[] records = gson.fromJson(reader, StreamRecordDTO[].class);

                        if(records != null){
                            Collections.addAll(filteredSongs, records);  // oppure  
                                                                         // import java.util.Arrays; // <--- Serve questo import
                                                                         // Invece di Collections.addAll(filteredSongs, records);
                                                                         //System.out.println(records[0]);
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    
                    //System.out.println(filteredSongs);
                }
                    else{
                    continue;
                }
            }
        }
        System.out.println("TOTALE: "+filteredSongs.size());
        return filteredSongs;
    }
}
