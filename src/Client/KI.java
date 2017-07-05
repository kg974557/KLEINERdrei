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
	private Position treasure;

	public KI(MazeCom maze, int id) throws UnmarshalException, IOException {
		this.maze = maze;
		this.id = id;
		board = new Board(maze.getAwaitMoveMessage().getBoard());
		treasure = new Position(board.findTreasure(maze.getAwaitMoveMessage().getTreasure()));
		card = new Card(board.getShiftCard());
		currentPos = new Position(board.findPlayer(id));
	}

	protected MoveMessageType move() {
		MoveMessageType zug = new MoveMessageType();
		List<Card> moeglicheRotationen = card.getPossibleRotations();
		//Default-Bewertung
		Bewertung bewertung = new Bewertung(Integer.MAX_VALUE, card, new Position(1, 0), currentPos);
		ArrayList<Position> placeShiftCardPositions = (ArrayList<Position>) Position.getPossiblePositionsForShiftcard();
		// für jede mögliche rotation der Karte
		for (Card c : moeglicheRotationen) {
			zug.setShiftCard(c); // gebe dem Zug die rotierte Karte

			// für jede mögliche Position der Karte (Einführungspunkt)
			for (Position p : placeShiftCardPositions) {
				zug.setShiftPosition(p); // schiebe die Karte an die Position p

				// simuliere den Zug auf einem geklonten Board
				Board temp = board.fakeShift(zug);
				//temp.setCard(p.getRow(), p.getCol(), c);
				List<Position> currentMoves = temp.getAllReachablePositions(temp.findPlayer(id)); // speicher
				currentPos = new Position(temp.findPlayer(id));																					// mögliche
				System.out.println(currentPos);
				if (temp.pathPossible(currentPos, treasure)) {
					zug.setNewPinPos(treasure);
					return zug;
				} 
				else 
				{
					for (Position pos : currentMoves) {
						int wertung = Math.abs(treasure.getCol() - pos.getCol())
								+ Math.abs(treasure.getRow() - pos.getRow());
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
		}

		System.out.println("Treasure: " + treasure);

		if (board.getForbidden() != null) {
			Position forbiddenMove = new Position(board.getForbidden());
			System.out.println("ForbiddenMove: " + forbiddenMove);
		}
		System.out.println(bewertung);
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
