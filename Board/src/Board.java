import java.awt.*;
import java.util.LinkedList;

/**
 * Created by bmix1 on 8/14/2017.
 */

public class Board {

	private double sqrt2, sqrt8;
	private char turn;
	private char[][] board;

	public class MoveException extends Exception {
		public MoveException(String message) { super(message); }
	}

	public Board(){
		board = new char[8][8];
		sqrt2 = Math.sqrt(2);
		sqrt8 = Math.sqrt(8);
		turn = 'r';
		this.reset();
	}

	public void switchTurn() {
		if (turn == 'r') {
			turn = 'b';
		}
		else {
			turn = 'r';
		}
	}

	public void makeMove(Move move) throws MoveException{
		Point start = move.getStartPosition();
		Point end = move.getEndPosition();

		double distance = Move.distance(start, end);
		if (distance != sqrt2 && distance != sqrt8){
			throw new MoveException(String.format("Invalid move %s -> %s",
					start.toString(), end.toString()));
		}

		char c = board[start.x][start.y];
		if (c != 'r' && c != 'R' && c != 'B' && c != 'b') {
			System.out.println(c);
			System.out.println(start.toString());
			throw new MoveException("Tried to move from location with no piece");
		}
		else if (c == 'r' && start.x == 0) {
			c = 'R';
		}
		else if (c == 'b' && start.x == 7){
			c = 'B';
		}

		board[start.x][start.y] = 'E';
		board[end.x][end.y] = c;

		if (distance == sqrt8) {
			if (start.x > end.x && start.y > end.y) {
				board[start.x-1][start.y-1] = 'E';
			}
			else if (start.x > end.x && start.y < end.y) {
				board[start.x-1][start.y+1] = 'E';
			}
			else if (start.x < end.x && start.y < end.y) {
				board[start.x+1][start.y+1] = 'E';
			}
			else if (start.x < end.x && start.y > end.y) {
				board[start.x+1][start.y-1] = 'E';
			}
		}

	}

	public boolean isValidMoveSequence(LinkedList<Move> moves) {
		if (moves.size() == 0) {
			return false;
		}
		for (Move move : moves) {
			if (!isValidMove(move))
				return false;
		}
		return true;
	}

	/**
	 * Returns whether a given move is valid
	 * @param move the move to be checked
	 * @return whether move is determined to be valid
	 */
	public boolean isValidMove(Move move) {
		Point start = move.getStartPosition();
		Point end = move.getEndPosition();
		double distance  = Move.distance(start, end);
		if (board[start.x][start.y] != turn) { //not moving your own piece
			return false;
		}
		else if (distance != sqrt2 && distance != sqrt8 && distance == 0) { //invalid move distances
			return false;
		}
		else {
			if (distance == sqrt2) {
				if (start.x < end.x && start.y == end.y || start.x > end.x && start.y == end.y //invalid locations
						|| start.y < end.y && start.x == end.x || start.y > end.y && start.x == end.x) {
					return false;
				}
				else if (board[start.x][start.y] == 'r' && start.x < end.x 		//disallow non-king pieces from
						|| board[start.x][start.y] == 'b' && end.x < start.x) { //moving in wrong direction
					return false;
				}
				else if (board[end.x][end.y] == 'E') //end location isn't empty
					return false;
			}
			else if (distance == sqrt8) {
				//stop players from jumping over their own pieces or empty locations
				if (start.x > end.x && start.y > end.y) {
					if (board[start.x-1][start.y-1] == 'E' ||
							Character.toLowerCase(board[start.x-1][start.y-1]) == turn)
						return false;
				}
				else if (start.x > end.x && start.y < end.y) {
					if (board[start.x-1][start.y+1] == 'E' ||
							Character.toLowerCase(board[start.x-1][start.y+1]) == turn)
						return false;
				}
				else if (start.x < end.x && start.y < end.y) {
					if (board[start.x+1][start.y+1] == 'E' ||
							Character.toLowerCase(board[start.x+1][start.y+1]) == turn)
						return false;
				}
				else if (start.x < end.x && start.y > end.y) {
					if (board[start.x+1][start.y-1] == 'E' ||
							Character.toLowerCase(board[start.x+1][start.y-1]) == turn)
						return false;
				}
				if (board[end.x][end.y] != 'E') //end location isn't empty
					return false;
			}
			return true;
		}
	}

	public void printBoard() {
		System.out.println("   0 1 2 3 4 5 6 7");
		for (int i = 0; i < 8; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < 8; j++) {
				System.out.print("|" + board[i][j]);
			}
			System.out.println("|");
		}
	}

	private void reset() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (i < 3) {
					if (i == 0 || i == 2) {
						if (j % 2 == 0) {
							board[i][j] = 'E';
						} else {
							board[i][j] = 'b';
						}
					}
					else {
						if (j % 2 == 0) {
							board[i][j] = 'b';
						} else {
							board[i][j] = 'E';
						}
					}
				}
				else if (i < 5) {
					board[i][j] = 'E';
				}
				else {
					if (i == 5 || i == 7) {
						if (j % 2 == 0) {
							board[i][j] = 'r';
						} else {
							board[i][j] = 'E';
						}
					}
					else {
						if (j % 2 == 0) {
							board[i][j] = 'E';
						} else {
							board[i][j] = 'r';
						}
					}
				}
			}

		}
	}

}
