import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class BoardTest {

	@Test
	public void testIsValidEndMove() {
		Board b = new Board();
		try {
			//black non-king
			b.switchTurn();
			b.makeMove(new Move(5,6,4,5));
			b.makeMove(new Move(4,5,3,4));
			b.switchTurn();
			Assert.assertTrue(b.isValidEndMove(new Move(2,3,4,5)));
			b.switchTurn();
			b.makeMove(new Move(6,7,5,6));
			b.switchTurn();
			Assert.assertFalse(b.isValidEndMove(new Move(2,3,4,5)));
			Assert.assertTrue(b.isValidEndMove(new Move(2,5,4,3)));
			b.switchTurn();
			b.makeMove(new Move(5,0,4,1));
			b.makeMove(new Move(6,1,5,0));
			b.switchTurn();
			Assert.assertFalse(b.isValidEndMove(new Move(2,5,4,3)));
			b.reset();

			//red non-king
			b.makeMove(new Move(2,1,3,2));
			b.makeMove(new Move(3,2,4,3));
			b.switchTurn();
			Assert.assertTrue(b.isValidEndMove(new Move(5,4,3,2)));
			b.switchTurn();
			b.makeMove(new Move(1,0,2,1));
			b.switchTurn();
			Assert.assertFalse(b.isValidEndMove(new Move(5,4,3,2)));
			Assert.assertTrue(b.isValidEndMove(new Move(5,2,3,4)));
			b.switchTurn();
			b.makeMove(new Move(2,7,3,6));
			b.makeMove(new Move(1,6,2,7));
			b.switchTurn();
			Assert.assertFalse(b.isValidEndMove(new Move(5,2,3,4)));
		}
		catch (Board.MoveException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsValidMove()
	{
		try {
			Board b = new Board();

			//ensure you can't move other players piece
			Assert.assertFalse(b.isValidMove(new Move(5, 0, 4, 1)));

			//ensure you can't move invalid distances
			Assert.assertFalse(b.isValidMove(new Move(2, 1, 2, 1)));
			Assert.assertFalse(b.isValidMove(new Move(2, 1, 1, 1)));

			//ensure non-kings cant move backwards
			b.makeMove(new Move(2,3,3,4));
			b.makeMove(new Move(1,2,2,3));
			Assert.assertFalse(b.isValidMove(new Move(2, 1, 1, 2)));
			b.switchTurn();
			b.makeMove(new Move(5,2,4,3));
			b.makeMove(new Move(6,1,5,2));
			Assert.assertFalse(b.isValidMove(new Move(5, 0, 6, 1)));
			b.reset();

			//--------- distance=sqrt2 moves non-king -----------------

			//ensure that you cant move into filled locations and can move
			//into empty locations
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 3, 0)));
			b.makeMove(new Move(2, 1, 3, 0));
			Assert.assertTrue(b.isValidMove(new Move(3, 0, 4, 1)));
			b.makeMove(new Move(3, 0, 4, 1));
			Assert.assertFalse(b.isValidMove(new Move(4, 1, 5, 0)));
			Assert.assertFalse(b.isValidMove(new Move(2, 1, 5, 2)));

			//--------- distance=sqrt8 moves non-king -----------------
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(5, 2, 3, 0)));
			Assert.assertFalse(b.isValidMove(new Move(5, 2, 3, 4)));
			Assert.assertFalse(b.isValidMove(new Move(5, 4, 3, 2)));
			b.makeMove(new Move(5, 2, 3, 0));
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(1, 0, 2, 1)));
			b.makeMove(new Move(1, 0, 2, 1));
			b.switchTurn();
			Assert.assertFalse(b.isValidMove(new Move(3, 0, 1, 2)));

			//king red piece
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 3, 2)));
			b.makeMove(new Move(2, 1, 3, 2));
			Assert.assertTrue(b.isValidMove(new Move(3, 2, 4, 3)));
			b.makeMove(new Move(3, 2, 4, 3));
			Assert.assertTrue(b.isValidMove(new Move(0, 1, 1, 0)));
			b.makeMove(new Move(0, 1, 1, 0));
			Assert.assertTrue(b.isValidMove(new Move(1, 0, 2, 1)));
			b.makeMove(new Move(1, 0, 2, 1));
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 3, 2)));
			b.makeMove(new Move(2, 1, 3, 2));
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(3, 0, 2, 1)));
			b.makeMove(new Move(3, 0, 2, 1));
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 1, 0)));
			b.makeMove(new Move(2, 1, 1, 0));
			Assert.assertTrue(b.isValidMove(new Move(1, 0, 0, 1)));
			b.makeMove(new Move(1, 0, 0, 1));

			//--------- distance=sqrt2 moves king -----------------
			Assert.assertTrue(b.isValidMove(new Move(0, 1, 1, 0)));
			b.makeMove(new Move(0, 1, 1, 0));
			Assert.assertTrue(b.isValidMove(new Move(5, 4, 4, 5)));
			b.makeMove(new Move(5, 4, 4, 5));
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(1, 2, 2, 1)));
			b.makeMove(new Move(1, 2, 2, 1));
			Assert.assertTrue(b.isValidMove(new Move(4, 3, 5, 4)));
			b.makeMove(new Move(4, 3, 5, 4));
			Assert.assertTrue(b.isValidMove(new Move(3, 2, 4, 3)));
			b.makeMove(new Move(3, 2, 4, 3));
			b.switchTurn();
			Assert.assertTrue(b.isValidMove(new Move(1, 0, 3, 2)));
			b.makeMove(new Move(1, 0, 3, 2));
			Assert.assertTrue(b.isValidMove(new Move(3, 2, 2, 1)));
			b.makeMove(new Move(3, 2, 2, 1));
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 1, 0)));
			b.makeMove(new Move(2, 1, 1, 0));
			Assert.assertFalse(b.isValidMove(new Move(1, 0, 3, 2)));
			Assert.assertTrue(b.isValidMove(new Move(1, 0, 2, 1)));
			b.makeMove(new Move(1, 0, 2, 1));
			Assert.assertTrue(b.isValidMove(new Move(2, 1, 1, 2)));
			b.makeMove(new Move(2, 1, 1, 2));
			Assert.assertFalse(b.isValidMove(new Move(1, 2, 3, 0)));

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}