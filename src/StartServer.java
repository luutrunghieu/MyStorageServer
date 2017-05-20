
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.swing.JOptionPane;

public class StartServer {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String[] hostArr = br.readLine().split("=");
            String host = hostArr[1].trim();
            String[] portArr = br.readLine().split("=");
            int port = Integer.parseInt(portArr[1].trim());
            String[] nameArr = br.readLine().split("=");
            String name = nameArr[1].trim();
//            System.out.println(host+" - "+port+" - "+name);
            LocateRegistry.createRegistry(port);
            ServerStorage server = new ServerStorage();
            Naming.rebind("rmi://"+host+":"+port+"//"+name, server);
            System.out.println("rmi://"+host+":"+port+"/"+name);
            
            String[] serverFolderArr = br.readLine().split("=");
            String serverFolder = serverFolderArr[1].trim();
            File workingDir = new File(serverFolder);
            if(!workingDir.exists()){
                workingDir.mkdir();
            }
            server.setWorkingDirectory(workingDir);
            JOptionPane.showMessageDialog(null, "Server is ready");
        } catch (RemoteException | MalformedURLException e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
