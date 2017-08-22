import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class MoveSequence implements Serializable{
	LinkedList<Move> moves = new LinkedList<Move>();

	public MoveSequence(LinkedList<Move> moves) {
		this.moves = moves;
	}

	public LinkedList<Move> getMoves() {
		return moves;
	}

}
