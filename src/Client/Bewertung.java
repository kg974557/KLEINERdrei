package Client;

import de.fhac.mazenet.server.Card;
import de.fhac.mazenet.server.Position;

public class Bewertung {

	private int wertung;
	private Card card;
	private Position cardpos;
	private Position pos;
	
	@Override
	public String toString() {
		return "Bewertung [wertung=" + wertung + ", card=" + card + ", cardpos=" + cardpos + ", pos=" + pos + "]";
	}

	public Bewertung(int wertung, Card card, Position cardpos, Position pos) {
		this.wertung = wertung;
		this.card = card;
		this.cardpos = cardpos;
		this.pos = pos;
	}

	public Position getCardpos() {
		return cardpos;
	}

	public void setCardpos(Position cardpos) {
		this.cardpos = cardpos;
	}

	public int getWertung() {
		return wertung;
	}

	public void setWertung(int wertung) {
		this.wertung = wertung;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
}
