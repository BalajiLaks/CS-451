import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by bmix1 on 8/14/2017.
 */

public class Board implements Serializable{
    private static final char red = 'r';
    private static final char redKing = 'R';
    private static final char black = 'b';
    private static final char blackKing = 'B';
    private static final char empty = '-';

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
        this.reset();
    }

    public void reset() {
        turn = black;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i < 3) {
                    if (i == 0 || i == 2) {
                        if (j % 2 == 0) {
                            board[i][j] = empty;
                        } else {
                            board[i][j] = black;
                        }
                    }
                    else {
                        if (j % 2 == 0) {
                            board[i][j] = black;
                        } else {
                            board[i][j] = empty;
                        }
                    }
                }
                else if (i < 5) {
                    board[i][j] = empty;
                }
                else {
                    if (i == 5 || i == 7) {
                        if (j % 2 == 0) {
                            board[i][j] = red;
                        } else {
                            board[i][j] = empty;
                        }
                    }
                    else {
                        if (j % 2 == 0) {
                            board[i][j] = empty;
                        } else {
                            board[i][j] = red;
                        }
                    }
                }
            }

        }
    }

    public void switchTurn() {
        if (turn == red) {
            turn = black;
        }
        else {
            turn = red;
        }
    }

    public char getTurn() {
        return turn;
    }

    // only call after validating move
    public void makeMove(Move move) throws MoveException{
        Point start = move.getStartPosition();
        Point end = move.getEndPosition();

        double distance = Move.distance(start, end);
        if (distance != sqrt2 && distance != sqrt8){
            throw new MoveException(String.format("Invalid move %s -> %s",
                    start.toString(), end.toString()));
        }

        char c = board[start.x][start.y];
        if (c != red && c != 'R' && c != blackKing && c != black) {
            System.out.println(c);
            System.out.println(start.toString());
            throw new MoveException("Tried to move from location with no piece");
        }
        else if (Character.toLowerCase(c) != turn) {
            throw new MoveException(String.format("Turn violation"));
        }
        else if (c == red && end.x == 0) {
            c = redKing;
        }
        else if (c == black && end.x == 7){
            c = blackKing;
        }

        board[start.x][start.y] = empty;
        board[end.x][end.y] = c;

        if (distance == sqrt8) {
            if (start.x > end.x && start.y > end.y) {
                board[start.x-1][start.y-1] = empty;
            }
            else if (start.x > end.x && start.y < end.y) {
                board[start.x-1][start.y+1] = empty;
            }
            else if (start.x < end.x && start.y < end.y) {
                board[start.x+1][start.y+1] = empty;
            }
            else if (start.x < end.x && start.y > end.y) {
                board[start.x+1][start.y-1] = empty;
            }
        }

    }

    // only call after validating move
    public void makeMoveSequence (LinkedList<Move> moves) throws MoveException{
        for (Move move : moves)
            makeMove(move);

    }

    public boolean isValidMoveSequence(LinkedList<Move> moves) {
        if (moves.size() == 0) {
            return false;
        }
        else if (moves.size() == 0){
            Point start = moves.get(0).getStartPosition();
            Point end = moves.get(0).getEndPosition();
            if (start.distance(end) != sqrt2)
                return false;
            else
                return isValidMove(moves.get(0));
        }
        else {
            for (int i =0; i< moves.size(); i++) {
                Point start = moves.get(i).getStartPosition();
                Point end = moves.get(i).getEndPosition();
                if (start.distance(end) != sqrt8)
                    return false;
                else {
                    if (i == moves.size() - 1){
                        if (!isValidEndMove(moves.get(i)))
                            return false;
                    }
                    else if (!isValidMove(moves.get(i)))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether a given move is valid without within rules of checkers besides
     * for checking if the move should fail because more jumps can be made
     * @param move the move being checked
     * @return whether move is valid in this context
     */
    public boolean isValidMove(Move move) {
        Point start = move.getStartPosition();
        Point end = move.getEndPosition();
        double distance  = Move.distance(start, end);
        if (Character.toLowerCase(board[start.x][start.y]) != turn) { //not moving your own piece
            return false;
        }
        if (board[end.x][end.y] != empty) { //end location isn't empty
            return false;
        }
        else if (distance != sqrt2 && distance != sqrt8) { //invalid move distances
            return false;
        }
        else if (board[start.x][start.y] == red && start.x < end.x 		//disallow non-king pieces from
                || board[start.x][start.y] == black && end.x < start.x) { //moving in wrong direction
            return false;
        }
        else if (distance == sqrt8) {
            //stop players from jumping over their own pieces or empty locations
            if (start.x > end.x && start.y > end.y) {
                if (board[start.x-1][start.y-1] == empty
                        || Character.toLowerCase(board[start.x-1][start.y-1]) == turn)
                    return false;
            }
            else if (start.x > end.x && start.y < end.y) {
                if (board[start.x-1][start.y+1] == empty
                        || Character.toLowerCase(board[start.x-1][start.y+1]) == turn)
                    return false;
            }
            else if (start.x < end.x && start.y < end.y) {
                if (board[start.x+1][start.y+1] == empty
                        || Character.toLowerCase(board[start.x+1][start.y+1]) == turn)
                    return false;
            }
            else if (start.x < end.x && start.y > end.y) {
                if (board[start.x+1][start.y-1] == empty
                        || Character.toLowerCase(board[start.x+1][start.y-1]) == turn)
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns whether a given move is valid without within rules of checkers including
     * for checking if the move should fail because more jumps can be made
     * @param move the move being checked
     * @return whether move is valid in this context
     */
    public boolean isValidEndMove(Move move) {
        if (!isValidMove(move))
            return false;
        else {
            Point start = move.getStartPosition();
            Point end = move.getEndPosition();

            //check if there are still moves the player can make
            if (board[start.x][start.y] == red) {
                if (Character.toLowerCase(board[end.x-1][end.y-1]) == black
                        && board[end.x-2][end.y-2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x-1][end.y+1]) == black
                        && board[end.x-2][end.y+2] == empty)
                    return false;
            }
            else if (board[start.x][start.y] == black) {
                if (Character.toLowerCase(board[end.x+1][end.y+1]) == red
                        && board[end.x+2][end.y+2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y-1]) == red
                        && board[end.x+2][end.y-2] == empty)
                    return false;
            }
            else if (board[start.x][start.y] == redKing) {
                if (Character.toLowerCase(board[end.x+1][end.y+1]) == black
                        && board[end.x+2][end.y+2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y-1]) == black
                        && board[end.x+2][end.y-2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y+1]) == black
                        && board[end.x+2][end.y+2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y-1]) == black
                        && board[end.x+2][end.y-2] == empty)
                    return false;

            }
            else if (board[start.x][start.y] == blackKing) {
                if (Character.toLowerCase(board[end.x+1][end.y+1]) == red
                        && board[end.x+2][end.y+2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y-1]) == red
                        && board[end.x+2][end.y-2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y+1]) == red
                        && board[end.x+2][end.y+2] == empty)
                    return false;
                else if (Character.toLowerCase(board[end.x+1][end.y-1]) == red
                        && board[end.x+2][end.y-2] == empty)
                    return false;
            }
        }
        return true;
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



}