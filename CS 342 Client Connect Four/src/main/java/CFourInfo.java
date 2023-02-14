import java.io.Serializable;
import javafx.util.Pair;

public class CFourInfo implements Serializable{
    String p1Plays;
    String p2Plays;
    Boolean have2Players;
    int playerTurn;
    Pair<Integer, Integer[][]> gameResult; // 0 = None; 1 = Player 1 win; 2 = Player 2 win; 3 = tie game
    Boolean p1PlayAgain;
    Boolean p2PlayAgain;
    Boolean newGame;


    CFourInfo() {
        p1Plays = "";
        p2Plays = "";
        have2Players = false;
        gameResult = new Pair<Integer, Integer[][]> (0, null);
        playerTurn = 1;
        p1PlayAgain = false;
        p2PlayAgain = false;
        newGame = false;
    }
}
