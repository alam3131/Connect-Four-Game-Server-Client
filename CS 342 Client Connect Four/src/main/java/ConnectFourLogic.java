import javafx.util.Pair;
import java.util.Objects;

public class ConnectFourLogic {
    // 0 = None; 1 = Player 1 win; 2 = Player 2 win; 3 = tie game
    public static Pair<Integer, Integer[][]> evalBoard (String buttonColor, GameButton[][] board) {
        // Check horizontal for win
        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 6; row++) {
                if((Objects.equals(board[row][col].color, buttonColor)) && (Objects.equals(board[row][col + 1].color, buttonColor)) && (Objects.equals(board[row][col + 2].color, buttonColor)) && (Objects.equals(board[row][col + 3].color, buttonColor))) {
                    Integer[][] colRow = {{col, row}, {col + 1, row}, {col + 2, row}, {col + 3, row}};
                    if(Objects.equals(buttonColor, "red")) {
                        return new Pair <Integer, Integer[][]> (1, colRow);
                    } else {
                        return new Pair <Integer, Integer[][]> (2, colRow);
                    }
                }
            }
        }

        // Check vertical for win
        for(int col = 0; col < 7; col++) {
            for(int row = 0; row < 3; row++) {
                if((Objects.equals(board[row][col].color, buttonColor)) && (Objects.equals(board[row+1][col].color, buttonColor)) && (Objects.equals(board[row+2][col].color, buttonColor)) && (Objects.equals(board[row+3][col].color, buttonColor))) {
                    Integer[][] colRow = {{col, row}, {col, row + 1}, {col, row + 2}, {col, row + 3}};
                    if(Objects.equals(buttonColor, "red")) {
                        return new Pair <Integer, Integer[][]> (1, colRow);
                    } else {
                        return new Pair <Integer, Integer[][]> (2, colRow);
                    }
                }
            }
        }

        // Check for positive diagonal for win
        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                if((Objects.equals(board[row][col].color, buttonColor)) && (Objects.equals(board[row+1][col+1].color, buttonColor)) && (Objects.equals(board[row+2][col+2].color, buttonColor)) && (Objects.equals(board[row+3][col+3].color, buttonColor))) {
                    Integer[][] colRow = {{col, row}, {col + 1, row + 1}, {col + 2, row + 2}, {col + 3, row + 3}};
                    if(Objects.equals(buttonColor, "red")) {
                        return new Pair <Integer, Integer[][]> (1, colRow);
                    } else {
                        return new Pair <Integer, Integer[][]> (2, colRow);
                    }
                }
            }
        }

        // Check for negative diagonal for win
        for(int col = 0; col < 4; col++) {
            for(int row = 3; row < 6; row++) {
                if((Objects.equals(board[row][col].color, buttonColor)) && (Objects.equals(board[row-1][col+1].color, buttonColor)) && (Objects.equals(board[row-2][col+2].color, buttonColor)) && (Objects.equals(board[row-3][col+3].color, buttonColor))) {
                    Integer[][] colRow = {{col, row}, {col + 1, row - 1}, {col + 2, row - 2}, {col + 3, row - 3}};
                    if(Objects.equals(buttonColor, "red")) {
                        return new Pair <Integer, Integer[][]> (1, colRow);
                    } else {
                        return new Pair <Integer, Integer[][]> (2, colRow);
                    }
                }
            }
        }

        // Searches for unclicked button
        for(int col = 0; col < 7; col++){
            for(int row = 0; row < 6; row++) {
                if(!board[row][col].clicked) {
                    return new Pair <Integer, Integer[][]> (0, null);
                }
            }
        }

        // If all buttons are pressed than return tie game
        return new Pair <Integer, Integer[][]> (3, null);
    }

    // Checks if the move is valid or not
    public static Boolean moveCheck(int col, int row, GameButton[][] board) {
        // Checks if the button has a pressed button below or is the bottom row
        if((row == 5) || board[row+1][col].clicked) {
            // Checks if the button has been clicked already
            if(!board[row][col].clicked) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
