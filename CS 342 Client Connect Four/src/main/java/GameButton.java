import javafx.scene.control.Button;

public class GameButton extends Button{
    String color;
    Boolean clicked;
    int row;
    int col;


    GameButton(int c, int r) {
        color = "";
        clicked = false;
        row = r;
        col = c;
    }
}
