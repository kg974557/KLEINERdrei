package Client;

import java.io.IOException;

import javax.xml.bind.UnmarshalException;

import de.fhac.mazenet.server.generated.AwaitMoveMessageType;
import de.fhac.mazenet.server.generated.MazeCom;
import de.fhac.mazenet.server.generated.MazeComType;
import de.fhac.mazenet.server.networking.XmlInStream;
import de.fhac.mazenet.server.networking.XmlOutStream;

public class Listener extends Thread{

	private XmlInStream inFromServer;
	private XmlOutStream outToServer;
	private int id;
	
	public Listener(XmlInStream in, XmlOutStream out) {
		inFromServer = in;
		outToServer = out;
		id = 1;
	}
	
	public void run(){
		try {
			boolean playing = true;
			while(playing){
				MazeCom maz = inFromServer.readMazeCom();			
				// user id wird abgespeichert
				if(maz.getMcType().equals(MazeComType.LOGINREPLY)){
					id = maz.getLoginReplyMessage().getNewID(); 
					System.out.println("Die User Id lautet: "+id);
				}
				// hier muss unser Zug getätigt werden
				else if(maz.getMcType().equals(MazeComType.AWAITMOVE)){
					AwaitMoveMessageType move = maz.getAwaitMoveMessage();
					KI neuerZug = new KI(maz, id);
					MazeCom retMaz = new MazeCom();
					retMaz.setMoveMessage(neuerZug.move());
					retMaz.setMcType(MazeComType.MOVE);
					outToServer.write(retMaz);
				}
				// Fehlerbehandlung
				else if(maz.getMcType().equals(MazeComType.ACCEPT)){
					System.out.println(maz.getAcceptMessage().getErrorCode().toString());
				}else if (maz.getMcType().equals(MazeComType.DISCONNECT) || maz.getMcType().equals(MazeComType.WIN))
				{
					playing = false;
				}
			}
		} catch (UnmarshalException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
