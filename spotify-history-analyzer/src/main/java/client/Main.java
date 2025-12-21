package client;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import common.StreamRecord;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("1. Inizio il test di Gson...");

        // Proviamo a creare l'oggetto Gson. Se la libreria manca, qui esploderà.

        Gson gson = new Gson();
        System.out.println("2. Oggetto Gson creato con successo! La libreria è presente.");

        // Test di lettura file
        System.out.println("Di quale anno vuoi vedere quanti ascolti hai?");
        int anno = input.nextInt();
        
        String folderPath = "Spotify Extended Streaming History";
        File folder = new File (folderPath);
        if(!folder.exists() || !folder.isDirectory()){
            System.out.println("Errore: Cartella non trovata: " + folder.getAbsolutePath());
            return;
        }
        File[] listOfFiles = folder.listFiles();
        List<String> targetFiles = new ArrayList<>();
        int totalSongs=0;
        List<StreamRecord> filteredSongs = new ArrayList<>();
        System.out.println("Sono stati trovati "+listOfFiles.length+" file");
        if(listOfFiles != null){
            for (int i=0; i<listOfFiles.length; i++){     
                if(listOfFiles[i].getName().startsWith("Streaming_History_Audio_") && listOfFiles[i].getName().contains(String.valueOf(anno))){
                    targetFiles.add(listOfFiles[i].getName());
                    try(Reader reader = new FileReader(listOfFiles[i].getAbsolutePath())){
                        StreamRecord[] records = gson.fromJson(reader, StreamRecord[].class);

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
        for (int i=0; i<filteredSongs.size();i++){
            if(!filteredSongs.get(i).ts.startsWith(String.valueOf(anno))){
               filteredSongs.remove(i);
            } 
        }
        System.out.println("Nel "+anno+" hai ascoltato "+filteredSongs.size());

        System.out.println(targetFiles);
        
        //String filePath = "Spotify Extended Streaming History/Streaming_History_Audio_%d-2020_0.json".formatted(anno);
        //
        //File f = new File(filePath);
        //if (f.exists()) {
        //     System.out.println("3. File JSON trovato: " + f.getAbsolutePath());
        //     try (Reader reader = new FileReader(filePath)) {
        //         StreamRecord[] records = gson.fromJson(reader, StreamRecord[].class);
        //         System.out.println("4. LETTURA RIUSCITA! Trovati " + records.length + " record.");
        //         if(records.length > 0) {
        //             System.out.println(" Primo brano: " + records[0].toString());
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        //} else {
        //     System.out.println("ATTENZIONE: Non trovo il file JSON, ma se vedi il punto 2, Gson funziona comunque!");
        //}
    }
}