package Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.UnmarshalException;

import de.fhac.mazenet.server.Board;
import de.fhac.mazenet.server.Card;
import de.fhac.mazenet.server.Position;
import de.fhac.mazenet.server.generated.MazeCom;
import de.fhac.mazenet.server.generated.MoveMessageType;

public class KI {
	private MazeCom maze;
	private Board board;
	private Card card;
	private Position currentPos;
	private int id;
	private Position endPos;

	public KI(MazeCom maze, int id) throws UnmarshalException, IOException {
		this.maze = maze;
		this.id = id;
		board = new Board(maze.getAwaitMoveMessage().getBoard());
		card = new Card(board.getShiftCard());
		currentPos = new Position(board.findPlayer(id));
	}

	protected MoveMessageType move() {
		MoveMessageType zug = new MoveMessageType();
		List<Card> moeglicheRotationen = card.getPossibleRotations();
		ArrayList<Position> placeShiftCardPositions = (ArrayList<Position>) Position.getPossiblePositionsForShiftcard();
		//Default-Bewertung
		Bewertung bewertung = new Bewertung(Integer.MAX_VALUE, card, new Position(1,0), currentPos);
		// für jede mögliche rotation der Karte		
		for (Card c : moeglicheRotationen) {
			zug.setShiftCard(c); // gebe dem Zug die rotierte Karte
			
			// für jede mögliche Position der Karte (Einführungspunkt)
			for (Position p : placeShiftCardPositions) {
				if(p.equals(board.getForbidden()))
				{
					System.out.println(board.getForbidden().getCol()+" , "+board.getForbidden().getRow());
					continue;
				}
				zug.setShiftPosition(p); // schiebe die Karte an die Position p
				
				// simuliere den Zug auf einem geklonten Board
				Board temp = board.fakeShift(zug);
				if(temp.findTreasure(maze.getAwaitMoveMessage().getTreasure()) == null){
					continue;
				}
				//temp.setCard(p.getRow(), p.getCol(), c);
				List<Position> currentMoves = temp.getAllReachablePositions(temp.findPlayer(id)); // speicher
				currentPos = new Position(temp.findPlayer(id));
				Position tempTreasure = new Position(temp.findTreasure(maze.getAwaitMoveMessage().getTreasure())); 
				for (Position pos : currentMoves) {
					int wertung = Math.abs(tempTreasure.getCol() - pos.getCol())
							+ Math.abs(tempTreasure.getRow() - pos.getRow());
					if (bewertung.getWertung() > wertung) 
					{
						bewertung.setWertung(wertung);
						bewertung.setCard(c);
						bewertung.setCardpos(p);
						bewertung.setPos(pos);
					}
				}
				
				
			}
		}
		
		System.out.println("Player_ID: "+id);
		if(bewertung.getPos() == board.findPlayer(id))
		{
			for(int i = 3;i <= 1;i--)
			{
				int player = id%4;
				Position enemy = board.findPlayer(player);
				
			}
		}
		zug.setNewPinPos(bewertung.getPos());
		zug.setShiftCard(bewertung.getCard());
		zug.setShiftPosition(bewertung.getCardpos());
		return zug;
	}

	public MazeCom getInFromServer() {
		return maze;
	}

	public Board getBoard() {
		return board;
	}

	public Card getCard() {
		return card;
	}

	public Position getCurrentPos() {
		return currentPos;
	}

	public int getId() {
		return id;
	}

	public Position getEndPos() {
		return endPos;
	}

}
