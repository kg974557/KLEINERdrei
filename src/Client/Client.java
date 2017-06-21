package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import de.fhac.mazenet.server.networking.XmlInStream;
import de.fhac.mazenet.server.networking.XmlOutStream;
import generated.LoginMessageType;
import generated.MazeCom;
import generated.MazeComType;

public class Client {
	private XmlInStream inFromServer;
	private XmlOutStream outToServer;
	public Client() {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			try {
				Socket clientSocket = new Socket("localhost", 5123);
				inFromServer = new XmlInStream(clientSocket.getInputStream());
				outToServer = new XmlOutStream(clientSocket.getOutputStream());
				MazeCom mc = new MazeCom();
				LoginMessageType lmt = new LoginMessageType();
				lmt.setName("KleinerDrei");
				mc.setLoginMessage(lmt);
				mc.setMcType(MazeComType.LOGIN);
				outToServer.write(mc);
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	public static void main(String[] args) {
		new Client();
	}
}
