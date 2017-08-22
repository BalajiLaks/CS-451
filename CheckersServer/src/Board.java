import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by bmix1 on 8/14/2017.
 */

public class Board implements Serializable {
    private static final char red = 'r';
    private static final char redKing = 'R';
    private static final char black = 'b';
    private static final char blackKing = 'B';
    private static final char empty = '-';

    private double sqrt2, sqrt8;
    private char turn;
    private char[][] board;
    private String winner;

    public class MoveException extends Exception {
        public MoveException(String message) { super(message); }
    }

    public char[][] getBoard()
    {
        return this.board;
    }

    public char getTurn(){ return this.turn; }

    public Board(){
        board = new char[8][8];
        sqrt2 = Math.sqrt(2);
        sqrt8 = Math.sqrt(8);
        this.reset();
    }

    public void reset() {
        turn = black;
        winner = "none";
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   0 1 2 3 4 5 6 7\n");
        for (int i = 0; i < 8; i++) {
            sb.append(i);
            sb.append(" ");
            for (int j = 0; j < 8; j++) {
                sb.append("|");
                sb.append(board[i][j]);
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public boolean isValidMoveSequence(MoveSequence moveSequence) {
        LinkedList<Move> moves = moveSequence.getMoves();
        if (moves.size() == 0) {
            return false;
        }
        else if (moves.size() == 1){
            Point start = moves.get(0).getStartPosition();
            Point end = moves.get(0).getEndPosition();
            if (start.distance(end) == sqrt2)
                return isValidMove(moves.get(0), board[start.x][start.y]);
            else if (start.distance(end) == sqrt8)
                return isValidEndMove(moves.get(0), board[start.x][start.y]);
            else
                return false;
        }
        else {

            Point start = moves.get(0).getStartPosition(), end;
            char startPlayer = board[start.x][start.y];
            if (!isValidMove(moves.get(0), startPlayer)) {
                return false;
            }

            for (int i = 1; i< moves.size(); i++) {
                start = moves.get(i).getStartPosition();
                end = moves.get(i).getEndPosition();

                if (start.distance(end) != sqrt8)
                    return false;
                else {
                    if (i == moves.size() - 1){
                        if (!isValidEndMove(moves.get(i),startPlayer))
                            return false;
                    }
                    else if (!isValidMove(moves.get(i), startPlayer))
                        return false;
                }
            }
        }
        return true;
    }

    // only call after validating move
    public void doTurn (MoveSequence moveSequence) throws MoveException {
        LinkedList<Move> moves = moveSequence.getMoves();
        for (Move move : moves)
            makeMove(move);

        switchTurn();
    }

    public boolean isWon() {
        int red = 0, black = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (Character.toLowerCase(c) == 'b')
                    black++;
                else if (Character.toLowerCase(c) == 'r')
                    red++;
            }
        }

        if (black == 0) {
            winner = "red";
            return true;
        }
        else if (red == 0) {
            winner = "black";
            return true;
        }

        return false;
    }

    public String getWinner() {
        return this.winner;
    }

    private void switchTurn() {
        if (turn == red) {
            turn = black;
        }
        else {
            turn = red;
        }
    }

    // only call after validating move
    private void makeMove(Move move) throws MoveException {
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

    /**
     * Returns whether a given move is valid within rules of checkers besides
     * for checking if the move should fail because more jumps can be made
     * @param move the move being checked
     * @return whether move is valid in this context
     */
    private boolean isValidMove(Move move, char startPlayer) {
        Point start = move.getStartPosition();
        Point end = move.getEndPosition();
        double distance  = Move.distance(start, end);
        if (Character.toLowerCase(startPlayer) != turn) { //not moving your own piece
            return false;
        }
        else if (board[end.x][end.y] != empty) { //end location isn't empty
            return false;
        }
        else if (startPlayer == red && start.x < end.x 		//disallow non-king pieces from
                || startPlayer == black && end.x < start.x) { //moving in wrong direction
            return false;
        }
        else if (distance == sqrt8) {
            //stop players from jumping over their own pieces or empty locations
            if (start.x > end.x && start.y > end.y) {
                if (Character.toLowerCase(board[start.x-1][start.y-1]) == turn
                        || board[start.x-1][start.y-1] == empty)
                    return false;
            }
            else if (start.x > end.x && start.y < end.y) {
                if (Character.toLowerCase(board[start.x-1][start.y+1]) == turn
                        || board[start.x-1][start.y+1] == empty)
                    return false;
            }
            else if (start.x < end.x && start.y < end.y) {
                if (Character.toLowerCase(board[start.x+1][start.y+1]) == turn
                        || board[start.x+1][start.y+1] == empty)
                    return false;
            }
            else if (start.x < end.x && start.y > end.y) {
                if (Character.toLowerCase(board[start.x+1][start.y-1]) == turn
                        || board[start.x+1][start.y-1] == empty)
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns whether a given move is valid without within rules of checkers including
     * checking if the move should fail because more jumps can be made
     * @param move the move being checked
     * @return whether move is valid in this context
     */
    private boolean isValidEndMove(Move move, char startPlayer) {
        if (!isValidMove(move, startPlayer))
            return false;
        else {
            Point start = move.getStartPosition();
            Point end = move.getEndPosition();

            //check if there are still moves the player can make
            if (startPlayer == red) {
                if (end.y - 2 >= 0 && end.x - 2 >= 0 && Character.toLowerCase(board[end.x - 1][end.y - 1]) == black
                        && board[end.x - 2][end.y - 2] == empty)
                    return false;

                else if (end.x - 2 >= 0 && end.y + 2 <= 7 && Character.toLowerCase(board[end.x - 1][end.y + 1]) == black
                        && board[end.x - 2][end.y + 2] == empty)
                    return false;
            }
            else if (startPlayer == black) {
                if (end.x + 2 <= 7 && end.y + 2 <= 7 && Character.toLowerCase(board[end.x + 1][end.y + 1]) == red
                        && board[end.x + 2][end.y + 2] == empty)
                    return false;
                else if (end.x + 2 <= 7 && end.y - 2 >= 0 && Character.toLowerCase(board[end.x + 1][end.y - 1]) == red
                        && board[end.x + 2][end.y - 2] == empty)
                    return false;
            }


            if (startPlayer == redKing || startPlayer == blackKing) {
                if (start.x > end.x && start.y > end.y) {
                    if (end.x+2 <= 7 && end.y-2 >=0 && Character.toLowerCase(board[end.x+1][end.y-1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y-2] == empty)
                        return false;
                    else if (end.y - 2 >= 0 && end.x - 2 >= 0 && Character.toLowerCase(board[end.x - 1][end.y - 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y - 2] == empty)
                        return false;
                    else if (end.x - 2 >= 0 && end.y + 2 <= 7 && Character.toLowerCase(board[end.x - 1][end.y + 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y + 2] == empty)
                        return false;
                }
                else if (start.x > end.x && start.y < end.y) {
                    if (end.x+2 <= 7 && end.y+2 <=7 && Character.toLowerCase(board[end.x+1][end.y+1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y+2] == empty)
                        return false;
                    else if (end.y - 2 >= 0 && end.x - 2 >= 0 && Character.toLowerCase(board[end.x - 1][end.y - 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y - 2] == empty)
                        return false;
                    else if (end.x - 2 >= 0 && end.y + 2 <= 7 && Character.toLowerCase(board[end.x - 1][end.y + 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y + 2] == empty)
                        return false;
                }
                else if (start.x < end.x && start.y < end.y) {
                    if (end.x+2 <= 7 && end.y+2 <=7 && Character.toLowerCase(board[end.x+1][end.y+1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y+2] == empty)
                        return false;
                    else if (end.x+2 <= 7 && end.y-2 >=0 && Character.toLowerCase(board[end.x+1][end.y-1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y-2] == empty)
                        return false;
                    else if (end.x - 2 >= 0 && end.y + 2 <= 7 && Character.toLowerCase(board[end.x - 1][end.y + 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y + 2] == empty)
                        return false;
                }
                else if (start.x < end.x && start.y > end.y) {
                    if (end.x+2 <= 7 && end.y+2 <=7 && Character.toLowerCase(board[end.x+1][end.y+1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y+2] == empty)
                        return false;
                    else if (end.y - 2 >= 0 && end.x - 2 >= 0 && Character.toLowerCase(board[end.x - 1][end.y - 1]) == switchColor(startPlayer)
                            && board[end.x - 2][end.y - 2] == empty)
                        return false;
                    else if (end.x+2 <= 7 && end.y-2 >=0 && Character.toLowerCase(board[end.x+1][end.y-1]) == switchColor(startPlayer)
                            && board[end.x+2][end.y-2] == empty)
                        return false;
                }
            }
        }

        return true;
    }

    private char switchColor(char c) {
        if (Character.toLowerCase(c) == black)
            return red;
        else
            return black;
    }

    public void print() {
        System.out.println(this.toString());
    }


}
