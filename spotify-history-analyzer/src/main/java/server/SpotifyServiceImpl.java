package server;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import common.SessionDTO;
import common.SpotifyService;
import common.StreamRecordDTO;
import common.Token;
public class SpotifyServiceImpl extends UnicastRemoteObject implements SpotifyService{

    private final TokenGenerator tokenGenerator = TokenGenerator.getInstance();
    private final TokenStore tokenStore = TokenStore.getInstance();
    private List<StreamRecordDTO> allSongs;
    public SpotifyServiceImpl() throws RemoteException{
        super();
    }
    
    public SessionDTO getSongsByYear(Token token, int anno, SessionDTO currentSession) throws RemoteException{

        
        currentSession.currentResult().clear();
        if(!tokenStore.isValid(token.payload()) || !tokenStore.isValidSignature(token)){
            System.out.println("[Server] Access denied, invalid or expired token.");
            // Rilancia l'eccezione al client
            throw new RemoteException("SESSION_EXPIRED: Login again.");

        }
        List<StreamRecordDTO> filteredSongs = new ArrayList<>();

        if(anno >= 2008){

        Gson gson = new Gson();
        String folderPath = "Spotify Extended Streaming History";
        File folder = new File (folderPath);

        if(!folder.exists() || !folder.isDirectory()){
            System.out.println("Errore: Cartella non trovata: " + folder.getAbsolutePath());
            return new SessionDTO(currentSession.currentResult(),currentSession.history());
        }
        File[] listOfFiles = folder.listFiles();
        List<String> targetFiles = new ArrayList<>();
        System.out.println("Sono stati trovati "+listOfFiles.length+" file");
        
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                // 1. Controllo solo che sia un file audio di Spotify, IGNORO l'anno nel nome del file
                //    perché i nomi dei file spesso coprono range di anni (es. 2010-2015).
                if (file.getName().startsWith("Streaming_History_Audio_")) {
                    
                    try (Reader reader = new FileReader(file.getAbsolutePath())) {
                        StreamRecordDTO[] records = gson.fromJson(reader, StreamRecordDTO[].class);
                    
                        if (records != null) {
                            // 2. FILTRO GRANULARE: Controllo canzone per canzone
                            for (StreamRecordDTO song : records) {
                                // Il campo 'ts' è formattato come "YYYY-MM-DDTHH:MM:SSZ"
                                // Quindi basta controllare se inizia con l'anno richiesto.
                                if (song.ts != null && song.ts.startsWith(String.valueOf(anno))) {
                                    filteredSongs.add(song);
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
}               currentSession.currentResult().addAll(filteredSongs);
                currentSession.history().put(String.valueOf(2)+String.valueOf(anno),"In the year " + anno + " you've listened to " + filteredSongs.size() + " songs!");
                return new SessionDTO(currentSession.currentResult(),currentSession.history());
        }
    }
    else{
        System.out.println("Invalid year, 2008 minimum");
        return new SessionDTO(new ArrayList<>(),currentSession.history());
    }
    return new SessionDTO(new ArrayList<>(),currentSession.history());
}


    public Token login(String username) throws RemoteException {
        System.out.println("[Server] Login requested for " + username);
        Token token = tokenGenerator.tokenBuild(username);
        tokenStore.store(token);
        System.out.println("[Server] Login OK. Sending token to client.");
        return token; // Restituisce il token al Client
        }   

    public Map<String, String> showHistory(SessionDTO currentSession) throws RemoteException{

        return currentSession.history();
        

    }
}
