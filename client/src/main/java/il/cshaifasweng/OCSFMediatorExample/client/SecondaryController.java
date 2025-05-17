package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SecondaryController {
    @FXML
    public Label markLabel;
    @FXML
    public Label statusLabel;
    @FXML
    private GridPane grid;
    private Button[][] cells = new Button[3][3];
    private SimpleClient client;

    // Define colors
    private static final String X_COLOR = "#ff3333"; // Red
    private static final String O_COLOR = "#3399ff"; // Blue
    private static final String DEFAULT_BUTTON_STYLE =
            "-fx-font-size: 36px; " +
                    "-fx-focus-color: transparent; " +
                    "-fx-background-color: #444444; " +
                    "-fx-text-fill: %s;"; // Color will be inserted here

    public SecondaryController(SimpleClient client) {
        this.client = client;
    }

    @FXML
    public void initialize(){
        createBoard();
        markLabel.setFont(new Font("Arial", 18));
        markLabel.setText("Your mark: "+client.getNotation());
        statusLabel.setFont(new Font("Arial", 14));
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    public Button getButton(int row, int col){
        return cells[row][col];
    }

    private void createBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button cell = new Button();
                cell.setPrefSize(100, 100);
                cell.setStyle(String.format(DEFAULT_BUTTON_STYLE, "white")); // Default text color
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> {
                    try {
                        handleMove(r, c);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                cells[row][col] = cell;
                grid.add(cell, col, row);
                cell.setText("");
            }
        }
    }

    private void handleMove(int row, int col) throws IOException {
        String message = "";
        Button clicked = cells[row][col];
        if (clicked.getText() != null && clicked.getText().isEmpty() && this.client.isMyturn()) {
            String notation = this.client.getNotation();
            clicked.setText(notation);

            // Set color based on mark
            if (notation.equals("X")) {
                clicked.setStyle(String.format(DEFAULT_BUTTON_STYLE, X_COLOR));
            } else {
                clicked.setStyle(String.format(DEFAULT_BUTTON_STYLE, O_COLOR));
            }

            message = "change "+ row + "," + col + " to " + notation;
            if(!isWinner()){
                client.sendToServer(message);
                client.setTurn(false);
            }
        }
    }

    private boolean isWinner() {
        return false;
    }
}