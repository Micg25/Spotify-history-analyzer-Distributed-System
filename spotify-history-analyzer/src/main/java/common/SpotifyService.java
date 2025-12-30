package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
public interface SpotifyService extends Remote{
    SessionDTO getSongsByYear(Token token, int year, SessionDTO currentSession) throws RemoteException;
    Token login(String username) throws RemoteException;
    public List <String> showHistory(SessionDTO currentSession) throws RemoteException;
}
