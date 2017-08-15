import java.io.Serializable;

public class Board implements Serializable{

    private int[][] board = new int[8][8];
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Board()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = 0;
            }
        }
    }

    public int getValue(int a, int b)
    {
        return board[a][b];
    }

    public void displayBoard()
    {
        //implement UI to display the board
    }

}
