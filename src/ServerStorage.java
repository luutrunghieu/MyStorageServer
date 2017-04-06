import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class ServerStorage extends UnicastRemoteObject implements ServerStorageInt{
	private String workingDirectory;
	private String workingFile;
	protected ServerStorage() throws RemoteException{
		super();
	}
	@Override
	public void setWorkingDirectory(String dir) throws RemoteException {
		// TODO Auto-generated method stub
		workingDirectory = dir;
		
	}
	@Override
	public void setWorkingFile(String file) throws RemoteException {
		// TODO Auto-generated method stub
		workingFile = file;
	}
	
	
	@Override
	public String getWorkingDirectory() throws RemoteException {
		return workingDirectory;
		
	}
	@Override
	public String getWorkingFile() throws RemoteException {
		// TODO Auto-generated method stub
		return workingFile;
	}
	@Override
	public void sendFilesToClients(String filePath,ClientStorageInt c) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			File f = new File(filePath);
//			String[] arr = filePath.split("\\");
//			String fileName = arr[arr.length-1];
			String fileName = f.getName();
			FileInputStream in = new FileInputStream(f);
			byte[] data = new byte[1024*1024];
			int len = in.read(data);
			while(len>0){
				c.receiveData(fileName, data, len);
				len = in.read(data);
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void receiveData(String fileName, byte[] data, int len) throws RemoteException {
		// TODO Auto-generated method stub
		try {
			String filePath = workingDirectory +"\\"+ fileName;
			System.out.println(filePath);
			File f = new File(filePath);
			f.createNewFile();
			FileOutputStream out = new FileOutputStream(f, true);
			out.write(data, 0, len);
			out.flush();
			out.close();
			System.out.println("Done writing data...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void synchronize(ClientStorageInt c) throws RemoteException {
		// TODO Auto-generated method stub
		File serverDir = new File(workingDirectory);
		File[] svListFile = serverDir.listFiles();
		
//		File clientDir = new File("ClientStorage");
		File[] cListFile = c.getListFile();
		
		HashSet<File> serverHs = new HashSet<>();
		for(File f:svListFile){
			serverHs.add(f);
		}
		
		HashSet<File> clientHs = new HashSet<>();
		for(File f:cListFile){
			clientHs.add(f);
		}
		
		HashSet<File> temp = (HashSet<File>) serverHs.clone();
		temp.retainAll(clientHs);
		serverHs.removeAll(temp);
		clientHs.removeAll(temp);
		
		for(File f: serverHs){
			sendFilesToClients(f.getPath(),c);
		}
		for(File f: clientHs){
			c.sendFilesToServers(f.getPath(), this);
		}
	}
	
	
}
