
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerStorageInt extends Remote {

    public void setWorkingDirectory(File dir) throws RemoteException;

    public File getWorkingDirectory() throws RemoteException;
    
    public void setSyncing(boolean status) throws RemoteException;
    
    public boolean getSyncing() throws RemoteException;

    public void sendFilesToClients(File filePath, ClientStorageInt c) throws RemoteException;

    public void synchronize(ClientStorageInt c) throws RemoteException;

    public String[] getListFiles() throws RemoteException;
    
    public InputStream getFileInputStream(File f) throws Exception;
    
    public OutputStream getFileOutputStream(File f) throws Exception;
    
    public boolean deleteFiles(File f) throws RemoteException;
}
