package client;

import com.google.gson.Gson; // <--- Qui stiamo testando se la libreria funziona!
import common.StreamRecord;
import java.io.FileReader;
import java.io.Reader;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("1. Inizio il test di Gson...");

        // Proviamo a creare l'oggetto Gson. Se la libreria manca, qui esploderà.
        try {
            Gson gson = new Gson();
            System.out.println("2. Oggetto Gson creato con successo! La libreria è presente.");
        } catch (NoClassDefFoundError e) {
            System.out.println("ERRORE FATALE: La libreria Gson non è trovata!");
            return;
        }

        // Test di lettura file
        String filePath = "Spotify Extended Streaming History/Streaming_History_Audio_2017-2020_0.json";
        
        File f = new File(filePath);
        if (f.exists()) {
             System.out.println("3. File JSON trovato: " + f.getAbsolutePath());
             try (Reader reader = new FileReader(filePath)) {
                 Gson gson = new Gson();
                 StreamRecord[] records = gson.fromJson(reader, StreamRecord[].class);
                 System.out.println("4. LETTURA RIUSCITA! Trovati " + records.length + " record.");
                 if(records.length > 0) {
                     System.out.println("   Primo brano: " + records[0].toString());
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
        } else {
             System.out.println("ATTENZIONE: Non trovo il file JSON, ma se vedi il punto 2, Gson funziona comunque!");
        }
    }
}