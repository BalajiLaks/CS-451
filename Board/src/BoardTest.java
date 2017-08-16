import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class BoardTest {

	@Test
	public void testMove()
	{
		try {
			Board b = new Board();
			b.printBoard();
			System.out.println("-------------------");
			b.makeMove(new Move(2,1,3,2));
			b.printBoard();
			System.out.println("-------------------");
			b.makeMove(new Move(2,3, 3, 4));
			b.printBoard();
			System.out.println("-------------------");
			b.makeMove(new Move(5, 0, 4,1));
			b.printBoard();
			System.out.println("-------------------");
			System.out.println(b.isValidMove(new Move(4,1,2,3)));
			System.out.println("-------------------");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}