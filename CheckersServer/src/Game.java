public class Game {

    private Player player1;
    private Player player2;
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

    Game(Player player1, Player player2, Board gameBoard)
    {
        this.setPlayer1(player1);
        this.setPlayer2(player2);
        this.setGameBoard(gameBoard);
    }

    public void startGame()
    {
        //Implement game logic here
    }

}
