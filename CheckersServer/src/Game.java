import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Game {

    private Player player1 = null;
    private Player player2 = null;
    private Board gameBoard;

    public Board getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Game()
    {
        this.player1 = null;
        this.player2 = null;
        this.gameBoard = new Board();
    }

    public Game(Player player1, Player player2, Board gameBoard)
    {
        this.setPlayer1(player1);
        this.setPlayer2(player2);
        this.setGameBoard(gameBoard);
    }

    public void startGame()
    {
        PrintStream out1 = player1.getStringOutput();
        PrintStream out2 = player2.getStringOutput();
        while(!(out1.checkError() && out2.checkError()))
        {
            /*
            ObjectOutputStream sendObject1 = player1.getObjectOutput();
            ObjectInputStream receiveObject1 = player1.getObjectInput();
            ObjectOutputStream sendObject2 = player2.getObjectOutput();
            ObjectInputStream receiveObject2 = player2.getObjectInput();
            */
            try{
                player1.getObjectOutput().writeObject(gameBoard);
                System.out.println("Board written");
                gameBoard = (Board)player1.getObjectInput().readObject();
                System.out.println("Board read");
                player2.getObjectOutput().writeObject(gameBoard);
                System.out.println("Board written");
                gameBoard = (Board)player2.getObjectInput().readObject();
                System.out.println("Board read");
                //gameBoard.printBoard();
            }
            catch (Exception e)
            {
                System.out.print(e);
            }
            gameBoard.print();
            break;
        }

    }

}
