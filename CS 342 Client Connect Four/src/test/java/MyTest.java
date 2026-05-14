import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import javafx.application.Platform;

import java.util.Arrays;

class MyTest {

    private static boolean initialized = false;
    private GameButton[][] board;

    @BeforeAll
    static void initJFX() {
        // Initializes UI toolkit so test cases can run
        // Game logic and UI components are currently tightly coupled which makes it hard to test
        // TODO: Decouple logic and UI components so UI is not necessary for testing logic
        if (!initialized) {
            Platform.startup(() -> {});
            initialized = true;
        }
    }

    @BeforeEach
    void boardSetup() {
        board = new GameButton[6][7];

        // initialize all buttons
        for(int row = 0; row < 6; row++) {
            for(int col = 0; col < 7; col++) {
                board[row][col] = new GameButton(col, row);
            }
        }
    }

    @Test
    void evalBoard_horizontalWin_rightEdge() {
        // Red Moves
        board[5][3].clicked = true;
        board[5][4].clicked = true;
        board[5][5].clicked = true;
        board[5][6].clicked = true;

        board[5][3].color = "red";
        board[5][4].color = "red";
        board[5][5].color = "red";
        board[5][6].color = "red";

        // Yellow Moves
        board[5][2].clicked = true;
        board[4][3].clicked = true;
        board[4][4].clicked = true;

        board[5][2].color = "yellow";
        board[4][3].color = "yellow";
        board[4][4].color = "yellow";

        Integer[][] winPos = {{3, 5}, {4, 5}, {5, 5}, {6, 5}};
        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);
        assertEquals(1, actual.getKey(), "Incorrect winning player");
        assertTrue(Arrays.deepEquals(winPos, actual.getValue()), "Winning positions do not match expected output");
    }

    @Test
    void evalBoard_horizontalWin_middle() {
        // Red Moves
        board[5][3].clicked = true;
        board[5][4].clicked = true;
        board[5][5].clicked = true;
        board[3][4].clicked = true;
        board[3][5].clicked = true;

        board[5][3].color = "red";
        board[5][4].color = "red";
        board[5][5].color = "red";
        board[3][4].color = "red";
        board[3][5].color = "red";

        // Yellow Moves
        board[5][2].clicked = true;
        board[4][2].clicked = true;
        board[4][3].clicked = true;
        board[4][4].clicked = true;
        board[4][5].clicked = true;

        board[5][2].color = "yellow";
        board[4][2].color = "yellow";
        board[4][3].color = "yellow";
        board[4][4].color = "yellow";
        board[4][5].color = "yellow";

        Integer[][] winPos = {{2, 4}, {3, 4}, {4, 4}, {5, 4}};
        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("yellow", board);
        assertEquals(2, actual.getKey(), "Incorrect winning player");
        assertTrue(Arrays.deepEquals(winPos, actual.getValue()), "Winning positions do not match expected output");
    }

    @Test
    void evalBoard_verticalWin_column0() {
        // Red creates vertical 4-in-a-row in column 0
        board[5][0].clicked = true;
        board[4][0].clicked = true;
        board[3][0].clicked = true;
        board[2][0].clicked = true;

        board[5][0].color = "red";
        board[4][0].color = "red";
        board[3][0].color = "red";
        board[2][0].color = "red";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        Integer[][] expectedPos = {
                {0, 2},
                {0, 3},
                {0, 4},
                {0, 5}
        };

        assertEquals(1, actual.getKey(), "Incorrect winner for red vertical win");
        assertTrue(Arrays.deepEquals(expectedPos, actual.getValue()), "Vertical win positions mismatch");
    }

    @Test
    void evalBoard_verticalWin_column6() {
        // Yellow creates vertical 4-in-a-row in last column
        board[5][6].clicked = true;
        board[4][6].clicked = true;
        board[3][6].clicked = true;
        board[2][6].clicked = true;

        board[5][6].color = "yellow";
        board[4][6].color = "yellow";
        board[3][6].color = "yellow";
        board[2][6].color = "yellow";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("yellow", board);

        Integer[][] expectedPos = {
                {6, 2},
                {6, 3},
                {6, 4},
                {6, 5}
        };

        assertEquals(2, actual.getKey(), "Incorrect winner for yellow vertical win");
        assertTrue(Arrays.deepEquals(expectedPos, actual.getValue()), "Vertical win positions mismatch");
    }

    @Test
    void evalBoard_positiveDiagonalWin_basic() {
        // red diagonal from (2,3) -> (5,0)
        board[5][2].clicked = true;
        board[4][3].clicked = true;
        board[3][4].clicked = true;
        board[2][5].clicked = true;

        board[5][2].color = "red";
        board[4][3].color = "red";
        board[3][4].color = "red";
        board[2][5].color = "red";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        Integer[][] expected = {
                {2, 5},
                {3, 4},
                {4, 3},
                {5, 2}
        };

        assertEquals(1, actual.getKey());
        assertTrue(Arrays.deepEquals(expected, actual.getValue()));
    }

    @Test
    void evalBoard_positiveDiagonalWin_shifted() {
        board[5][1].clicked = true;
        board[4][2].clicked = true;
        board[3][3].clicked = true;
        board[2][4].clicked = true;

        board[5][1].color = "red";
        board[4][2].color = "red";
        board[3][3].color = "red";
        board[2][4].color = "red";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        Integer[][] expected = {
                {1, 5},
                {2, 4},
                {3, 3},
                {4, 2}
        };

        assertEquals(1, actual.getKey());
        assertTrue(Arrays.deepEquals(expected, actual.getValue()));
    }

    @Test
    void evalBoard_negativeDiagonalWin_basic() {
        board[2][2].clicked = true;
        board[3][3].clicked = true;
        board[4][4].clicked = true;
        board[5][5].clicked = true;

        board[2][2].color = "yellow";
        board[3][3].color = "yellow";
        board[4][4].color = "yellow";
        board[5][5].color = "yellow";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("yellow", board);

        Integer[][] expected = {
                {2, 2},
                {3, 3},
                {4, 4},
                {5, 5}
        };

        assertEquals(2, actual.getKey());
        assertTrue(Arrays.deepEquals(expected, actual.getValue()));
    }

    @Test
    void evalBoard_negativeDiagonalWin_shifted() {
        board[1][3].clicked = true;
        board[2][4].clicked = true;
        board[3][5].clicked = true;
        board[4][6].clicked = true;

        board[1][3].color = "yellow";
        board[2][4].color = "yellow";
        board[3][5].color = "yellow";
        board[4][6].color = "yellow";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("yellow", board);

        Integer[][] expected = {
                {3, 1},
                {4, 2},
                {5, 3},
                {6, 4}
        };

        assertEquals(2, actual.getKey());
        assertTrue(Arrays.deepEquals(expected, actual.getValue()));
    }

    @Test
    void evalBoard_noWin_emptyBoard() {
        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        assertEquals(0, actual.getKey(), "Incorrect winner for no win empty board");
        assertNull(actual.getValue());
    }

    @Test
    void evalBoard_noWin_partiallyFilled() {
        board[5][0].clicked = true;
        board[5][0].color = "red";

        board[5][1].clicked = true;
        board[5][1].color = "yellow";

        board[5][2].clicked = true;
        board[5][2].color = "red";

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        assertEquals(0, actual.getKey());
        assertNull(actual.getValue());
    }

    @Test
    void evalBoard_tieGame() {
        String[][] colors = {
                {"r","r","r","y","r","r","r"},
                {"y","y","y","r","y","y","y"},
                {"r","r","r","y","r","r","r"},
                {"y","y","y","r","y","y","y"},
                {"r","r","r","y","r","r","r"},
                {"y","y","y","r","y","y","y"}
        };

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                board[r][c].clicked = true;
                board[r][c].color = colors[r][c].equals("r") ? "red" : "yellow";
            }
        }

        Pair<Integer, Integer[][]> actual = ConnectFourLogic.evalBoard("red", board);

        assertEquals(3, actual.getKey());
        assertNull(actual.getValue());
    }

    // Checks that the move you are making will not lead to floating game piece
    @Test
    void moveCheck_lowestRow() {
        // Checks move validity on empty board
        // Only moves on row 5 should be valid
        assertFalse(ConnectFourLogic.moveCheck(3, 4, board), "Expected invalid move. Row 5 Col 3 not filled");
        assertTrue(ConnectFourLogic.moveCheck(3, 5, board), "Expected valid move on row 5");

        // Button in row 5 col 3 is now clicked
        // Checks to see if row 4 col 3 is now valid (it should be)
        // Move on row 3 col 3 should still be invalid
        board[5][3].clicked = true;
        assertTrue(ConnectFourLogic.moveCheck(3, 4, board), "Expected valid move");
        assertFalse(ConnectFourLogic.moveCheck(3, 3, board), "Expected invalid move");
    }

    // Checks that move is not already taken
    @Test
    void moveCheck_sameMove() {
        // Button on row 5 col 0 is now clicked. Clicking the button again would be an invalid move
        board[5][0].clicked = true;
        assertFalse(ConnectFourLogic.moveCheck(0, 5, board), "Expected invalid move");
    }
}
