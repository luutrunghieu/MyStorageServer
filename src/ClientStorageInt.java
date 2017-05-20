
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ClientStorageInt extends Remote {
    public File getWorkingDirectory() throws RemoteException ;

    public void sendFilesToServers(File filePath, ServerStorageInt sv) throws RemoteException;

    public LinkedList<FileInfo> getListFileInfo() throws RemoteException;

    public void deleteFile(File f) throws RemoteException;

    public InputStream getFileInputStream(File f) throws Exception;
    
    public OutputStream getFileOutputStream(File f) throws Exception;
}
