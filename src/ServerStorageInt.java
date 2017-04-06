import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerStorageInt extends Remote{
	public void setWorkingDirectory(String dir) throws RemoteException;
	
	public String getWorkingDirectory() throws RemoteException;
	
	public void setWorkingFile(String file) throws RemoteException;
	
	public String getWorkingFile() throws RemoteException;
	
	public void sendFilesToClients(String filePath, ClientStorageInt c) throws RemoteException;
	
	public void receiveData(String fileName, byte[] data, int len) throws RemoteException;
	
	public void synchronize(ClientStorageInt c) throws RemoteException;
}
