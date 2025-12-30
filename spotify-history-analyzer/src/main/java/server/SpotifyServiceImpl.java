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
    }
    else{
        System.out.println("Invalid year, 2008 minimum");
    }
        currentSession.currentResult().addAll(filteredSongs);
        currentSession.history().add("In the year " + anno + " you've listened to " + filteredSongs.size() + " songs!");
        return new SessionDTO(currentSession.currentResult(),currentSession.history());
    
}

    public Token login(String username) throws RemoteException {
        System.out.println("[Server] Login requested for " + username);
        Token token = tokenGenerator.tokenBuild(username);
        tokenStore.store(token);
        System.out.println("[Server] Login OK. Sending token to client.");
        return token; // Restituisce il token al Client
        }   

    public List<String> showHistory(SessionDTO currentSession) throws RemoteException{

        return currentSession.history();
        

    }
}
