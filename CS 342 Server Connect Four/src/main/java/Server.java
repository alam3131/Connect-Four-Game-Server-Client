import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server{

    int count = 1; // Client number
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> numClientCallback, gameInfoCallback;
    CFourInfo info = new CFourInfo();

    Server(int portNum, Consumer<Serializable> numClientCall, Consumer<Serializable> gameInfoCall){
        numClientCallback = numClientCall;
        gameInfoCallback = gameInfoCall;
        server = new TheServer(portNum);
        server.start();
    }


    public class TheServer extends Thread{
        private int portNum;
        TheServer(int portNum) {
            this.portNum = portNum;
        }

        public void run() {
            // Server listens for and creates new clients threads
            try (ServerSocket mysocket = new ServerSocket(portNum);) {
                gameInfoCallback.accept("Server is waiting for a client!");

                while (true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    clients.add(c);
                    c.start();
                    numClientCallback.accept("Number of Clients Connected: " + clients.size());

                    count++;

                    if(clients.size() >= 2) {
                        info.have2Players = true;
                        gameInfoCallback.accept("Game Started!");
                    }
                }
            }//end of try
            catch (Exception e) {
                numClientCallback.accept("Server socket did not launch");
            }
        }//end of while
    }


    // In Charge of listening to input and output from clients
    class ClientThread extends Thread{

        Socket connection;
        int count; // Client's number
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void updateClients(CFourInfo data) {
            for(int i = 0; i < clients.size(); i++) {
                // get each client
                ClientThread t = clients.get(i);
                try {
                    // give updated message
                    t.out.writeObject(data);
                }
                catch(Exception e) {}
            }
        }

        public void run(){
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            ClientThread t = clients.get(count - 1);
            try {
                // Send client their number
                t.out.writeObject(count);

                updateClients(info);
            }
            catch(Exception e) {}


            while(true) {
                try {
                    info = (CFourInfo) in.readObject();

                    if(info.p1PlayAgain && info.p2PlayAgain) {
                        info = new CFourInfo();
                        info.newGame = true;
                    } else if (!info.p1PlayAgain && !info.p2PlayAgain) {
                        // Updates server listview about who played what
                        if (count == 1) {
                            gameInfoCallback.accept("Client 1 played " + info.p1Plays);
                            info.playerTurn = 2;
                        } else {
                            gameInfoCallback.accept("Client 2 played " + info.p2Plays);
                            info.playerTurn = 1;
                        }

                        // Updates server listview about winning
                        if (info.gameResult.getKey() == 1) {
                            gameInfoCallback.accept("Player 1 Wins!");
                        } else if (info.gameResult.getKey() == 2) {
                            gameInfoCallback.accept("Player 2 Wins!");
                        } else if (info.gameResult.getKey() == 3) {
                            gameInfoCallback.accept("Tie Game");
                        }
                    }

                    updateClients(info);
                }
                catch(Exception e) {
                    gameInfoCallback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    info.have2Players = false;
                    updateClients(info);
                    clients.remove(this);
                    numClientCallback.accept("Number of Clients Connected: " + clients.size());
                    break;
                }
            }
        }//end of run


    }//end of client thread
}
