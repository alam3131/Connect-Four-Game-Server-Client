# Connect Four Game
 With the use of a server, two clients may connect to the server and play a game of connect four. A maximum of two players should be connected. Players will take turns choosing squares on the grid and attempt to be the first to connect four grids horizontally, vertically, or diagonally. Game rules are the same as the classic Connect Four Game. The game has logic fully implemented. The game was designed to simulate server-client transactions on a single computer.
 
 ### Directions to Launch the Game
 Make sure to have Apache Maven installed. Running the game would be easier on an IDE such as Intellj. Have Intellij installed as well. Download both the server and client folders. Open both the server and client folders in Intellij, preferably both in separate window. 
 
 1. Run the server by running the command "mvn compile exec:java" in the terminal of the window for the server code.
 2. Have two terminals running in the client code window. Run command "mvn compile exec:java" in each of the terminals. **Error may occur if command is ran too quickly one after the other. Make sure to pause for a few seconds for one terminal to finish running before executing command on the other terminal.**
 3. Once server and two client windows are running, enter the port number "5555" and "127.0.0.1" then click the start buttons. Start the server before starting the clients.
 4. The game will indicate which player's turn it is and server will show when clients have connected. Players may now play the game. Have fun!

**Note: The play again button that appears after the game has finished is still a work in progress and does not work. Sorry. :(**
 
 ### Technologies
* Apache Maven 4.0.0
* JavaFX 19
* Maven surefire plugin 2.22.1
* Maven compiler plugin 3.8.1
 
 
