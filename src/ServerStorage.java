
import com.healthmarketscience.rmiio.SerializableInputStream;
import com.healthmarketscience.rmiio.SerializableOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStorage extends UnicastRemoteObject implements ServerStorageInt, Serializable {

    private File workingDirectory;
    boolean syncing;

    protected ServerStorage() throws RemoteException {
        super();
        syncing = false;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void setWorkingDirectory(File dir) throws RemoteException {
        // TODO Auto-generated method stub
        workingDirectory = dir;

    }

    @Override
    public File getWorkingDirectory() throws RemoteException {
        return workingDirectory;

    }

    @Override
    public void setSyncing(boolean status) throws RemoteException {
        this.syncing = status;
    }

    @Override
    public boolean getSyncing() throws RemoteException {
        return syncing;
    }

    @Override
    public String[] getListFiles() throws RemoteException {
        return workingDirectory.list();
    }

    @Override
    public InputStream getFileInputStream(File f) throws Exception {
        return new SerializableInputStream(new FileInputStream(f));
    }

    @Override
    public OutputStream getFileOutputStream(File f) throws Exception {
        return new SerializableOutputStream(new FileOutputStream(f));
    }

    @Override
    public boolean deleteFiles(File f) throws RemoteException {
        return f.delete();

    }

    @Override
    public void sendFilesToClients(File f, ClientStorageInt c) throws RemoteException {

        // TODO Auto-generated method stub
        try {
            InputStream is = this.getFileInputStream(f);
            OutputStream os = c.getFileOutputStream(new File(c.getWorkingDirectory() + "//" + f.getName()));
            byte[] data = new byte[16 * 1024 * 1024];
            int len = 0;
            while ((len = is.read(data)) >= 0) {
                os.write(data, 0, len);
            }
            is.close();
            os.close();
        } catch (Exception ex) {
            Logger.getLogger(ServerStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void synchronize(ClientStorageInt c) throws RemoteException {
        // TODO Auto-generated method stub
        File[] svListFile = workingDirectory.listFiles();
        System.out.println("Syncing...");
        LinkedList<FileInfo> ll = c.getListFileInfo();
        Iterator<FileInfo> it = ll.iterator();
        while (it.hasNext()) {
            FileInfo fi = it.next();
//			System.out.println(fi.getPath()+" - "+fi.getName()+" - "+fi.getLastModified()+" - "+fi.getLength());
            boolean exist = false;
            for (File f : svListFile) {
                if (fi.getName().equals(f.getName())) {
                    exist = true;
                    if (fi.getLastModified() > f.lastModified() && fi.getLength()!= f.length()) {
//                        f.delete();
                        c.sendFilesToServers(new File(fi.getPath()), this);
                    } else if (f.lastModified() > fi.getLastModified() && fi.getLength()!= f.length()) {
//                        c.deleteFile(new File(fi.getPath()));
                        try {
                            sendFilesToClients(f, c);
                        } catch (Exception ex) {
                            Logger.getLogger(ServerStorage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                }
            }
            if (!exist) {
                c.sendFilesToServers(new File(fi.getPath()), this);
            }
        }
        for (File f : svListFile) {
            Iterator<FileInfo> it2 = ll.iterator();
            boolean exist = false;
            while (it.hasNext()) {
                FileInfo fi = it.next();
                if (fi.getName().equals(f.getName())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                sendFilesToClients(f, c);
            }
        }
    }

}
