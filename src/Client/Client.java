package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import de.fhac.mazenet.server.generated.LoginMessageType;
import de.fhac.mazenet.server.generated.MazeCom;
import de.fhac.mazenet.server.generated.MazeComType;
import de.fhac.mazenet.server.networking.XmlInStream;
import de.fhac.mazenet.server.networking.XmlOutStream;

public class Client {
	private XmlInStream inFromServer;
	private XmlOutStream outToServer;
	public Client() {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		try {
			Socket clientSocket = new Socket("localhost", 5123);
			inFromServer = new XmlInStream(clientSocket.getInputStream());
			outToServer = new XmlOutStream(clientSocket.getOutputStream());
			
			// Spielgeschehen
			// Thread Serverlistener
			Thread t = new Listener(inFromServer, outToServer);
			
			//login
			MazeCom mc = new MazeCom();
			LoginMessageType lmt = new LoginMessageType();
			String name = JOptionPane.showInputDialog("Gebe den Spielernamen an");
		//	lmt.setName("KleinerDrei"); // nur ein User des Clients möglich
			lmt.setName(name);
			mc.setLoginMessage(lmt);
			mc.setMcType(MazeComType.LOGIN);
			outToServer.write(mc);
			t.run();
			System.out.println("Close");			
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public static void main(String[] args) {
		new Client();
	}
}
