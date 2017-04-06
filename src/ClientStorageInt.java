import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientStorageInt extends Remote{
	public void receiveData(String fileName, byte[] data, int len) throws RemoteException;
	
	public void sendFilesToServers(String filePath, ServerStorageInt sv) throws RemoteException;
	
	public File[] getListFile() throws RemoteException;
}
