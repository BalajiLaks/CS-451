import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class BoardTest {

	@Test
	public void testMoveValidation()
	{
		try {
			Board b = new Board();
			LinkedList<Move> moves = new LinkedList<Move>();
			moves.add(new Move(2, 3, 3, 2));
			Assert.assertTrue(b.isValidMoveSequence(new MoveSequence(moves)));
//			b.reset();	Board b = new Board();
//			LinkedList<Move> moves = new LinkedList<Move>();
//
//			//ensure you can't move other players piece
//			moves.add(new Move(5, 0, 4, 1));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//
//			//ensure you can't move invalid distances
//			moves.add(new Move(2, 1, 2, 1));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			moves.add(new Move(0,1,3,2));
//			moves.add(new Move(5,0,4,1));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//
//			//ensure non-kings cant move backwards
//			moves.add(new Move(2,3,3,4));
//			b.doTurn(moves);
//			moves.clear();
//			moves.add(new Move(5,0,4,1));
//			b.doTurn(moves);
//			moves.clear();
//			moves.add(new Move(3,4,2,3));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			b.reset();
//
//			//ensure that you cant move into empty locations
//			moves.add(new Move(2,3,3,4));
//			moves.add(new Move(3,4,4,5));
//			b.doTurn(moves);
//			moves.clear();
//			moves.add(new Move(5,6,4,5));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//
//			//ensure jump validation is working
//			moves.add(new Move(5,6,3,4));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(1,2,2,3));
//			moves.add(new Move(2,3,4,1));
//			moves.add(new Move(4,1,3,0));
//			moves.add(new Move(0,1,1,2));
//			moves.add(new Move(1,2,2,3));
//			moves.add(new Move(2,3,4,1));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(3,4,2,3));
//			moves.add(new Move(2,3,1,2));
//			moves.add(new Move(1,2,0,1));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(2,7,3,6));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(0,1,2,3));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			moves.add(new Move(0,1,1,2));
//			moves.add(new Move(1,2,2,3));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(3,6,4,7));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(2,3,0,1));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			System.out.println(b.toString());
//			moves.add(new Move(6,7,5,6));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(4,1,3,2));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(2,3,4,1));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(1,6,2,7));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(4,1,2,3));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			moves.add(new Move(4,1,3,2));
//			moves.add(new Move(3,2,2,3));
//			b.doTurn(moves);
//			moves.clear();
//
//
//			moves.add(new Move(2, 7, 3, 6));
//			Assert.assertTrue(b.isValidMoveSequence(moves));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(2,3,4,1));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//
//			//end move validation
//			b.reset();
//			moves.add(new Move(2,3,3,2));
//			moves.add(new Move(3,2,4,1));
//			moves.add(new Move(1,4,2,3));
//			b.doTurn(moves);
//			moves.clear();
//			moves.add(new Move(5,0,3,2));
//			b.print();
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//			b.reset();
//
//			moves.add(new Move(2,3,3,4));
//			moves.add(new Move(3,4,4,5));
//			moves.add(new Move(1,2,2,3));
//			b.doTurn(moves);
//			moves.clear();
//			moves.add(new Move(5,6,3,4));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.clear();
//
//			moves.add(new Move(5,6,3,4));
//			moves.add(new Move(3,4,1,2));
//			Assert.assertTrue(b.isValidMoveSequence(moves));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(2,1,3,0));
//			moves.add(new Move(1,0,2,1));
//			moves.add(new Move(0,1,1,0));
//			b.doTurn(moves);
//			moves.clear();
//
//
//			moves.add(new Move(1,2,0,1));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(0,3,1,2));
//			moves.add(new Move(2,5,3,4));
//			b.doTurn(moves);
//			moves.clear();
//
//			moves.add(new Move(0,1,2,3));
//			Assert.assertFalse(b.isValidMoveSequence(moves));
//			moves.add(new Move(2,3,4,5));
//			Assert.assertTrue(b.isValidMoveSequence(moves));
//			b.doTurn(moves);
//			moves.clear();


		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}