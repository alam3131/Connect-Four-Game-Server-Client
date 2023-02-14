import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Objects;
import java.util.function.Consumer;

public class Client extends Thread{

    Socket socketClient;

    static ObjectOutputStream out;
    static ObjectInputStream in;
    static int clientNum;
    static CFourInfo info;
    private int portNum;
    private String ipAddress;

    private String opponentMove; // Stores opponent's previous move

    private Consumer<Serializable> playerTurnCallback, gameInfoCallback, updateCallback, disableCallback, enableCallback, endGameCallback, resetGameCallback;

    Client(int portNum, String ipAddress, Consumer<Serializable> playerTurnCall,
           Consumer<Serializable> gameInfoCall, Consumer<Serializable> updateCall,
           Consumer<Serializable> disableCall, Consumer<Serializable> enableCall,
           Consumer<Serializable> endGameCall, Consumer<Serializable> resetGameCall){
        this.portNum = portNum;
        this.ipAddress = ipAddress;
        playerTurnCallback = playerTurnCall;
        gameInfoCallback = gameInfoCall;
        updateCallback = updateCall;
        disableCallback = disableCall;
        enableCallback = enableCall;
        endGameCallback = endGameCall;
        resetGameCallback = resetGameCall;
    }

    public void run() {

        try {
            socketClient= new Socket(ipAddress, portNum);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
            clientNum = (int)in.readObject(); // Attempts to read in client number from server
        }
        catch(Exception e) {}

        // Continues reading in objects until there is a win or a tie
        while(true) {
            try {
                info = (CFourInfo) in.readObject();

                System.out.println("################################");
                System.out.println("p1Plays: " + info.p1Plays);
                System.out.println("p2Plays: " + info.p2Plays);
                System.out.println("have2Players: " + info.have2Players);
                System.out.println("playerTurn: " + info.playerTurn);
                System.out.println("gameResult: " + info.gameResult);
                System.out.println("playAgain: " + info.p1PlayAgain);
                System.out.println("playAgain: " + info.p2PlayAgain);
                System.out.println("newGame: " + info.newGame);

                if(info.newGame) {
                    resetGameCallback.accept("");
                    info.newGame = false;
                }

                updateCallback.accept(""); // Updates the board given the new CFourInfo

                // Updates the listview on the opponent's moves
                if((clientNum == 1) && (!Objects.equals(info.p2Plays, "")) && (!Objects.equals(info.p2Plays, opponentMove))) {
                    gameInfoCallback.accept("Opponent played " + info.p2Plays);
                    opponentMove = info.p2Plays;

                } else if((clientNum == 2) && (!Objects.equals(info.p1Plays, "")) && (!Objects.equals(info.p1Plays, opponentMove))) {
                    gameInfoCallback.accept("Opponent played " + info.p1Plays);
                    opponentMove = info.p1Plays;
                }

                // If there is a win or a tie then change to result scene
                if(info.gameResult.getKey() == 1) {
                    endGameCallback.accept("Player 1 Won!");
                    enableCallback.accept("");
                } else if(info.gameResult.getKey() == 2) {
                    endGameCallback.accept("Player 2 Won!");
                    enableCallback.accept("");
                } else if(info.gameResult.getKey() == 3) {
                    endGameCallback.accept("Tie Game");
                    enableCallback.accept("");
                }

                // If it's not the player's turn then disable the board
                if(info.gameResult.getKey() != 1 && info.gameResult.getKey() != 2 && info.gameResult.getKey() != 3) {
                    if (info.playerTurn != clientNum) {
                        disableCallback.accept("");
                        playerTurnCallback.accept("Opponent's Turn!");
                    } else {
                        enableCallback.accept("");
                        playerTurnCallback.accept("Your Turn!");
                    }
                }
            }
            catch(Exception e) {}
        }
    }

    public static void send(CFourInfo data) {

        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
