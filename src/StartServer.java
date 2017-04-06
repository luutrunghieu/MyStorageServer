import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class StartServer {
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(5000);
			ServerStorage server = new ServerStorage();
			Naming.rebind("rmi://localhost:5000/hieu", server);
			System.out.println("Server is ready");
		} catch (RemoteException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
