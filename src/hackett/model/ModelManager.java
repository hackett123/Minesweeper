package hackett.model;

import hackett.controller.GameController;

public class ModelManager {

    private static ModelManager instance;
    private GameController gameController;

    private static int NUM_ROWS, NUM_COLS, NUM_MINES;

    private Space[][] board;

    public static ModelManager getInstance(GameController gameController) {
        if (instance == null) {
            instance = new ModelManager(gameController);
        }
        return instance;
    }

    private ModelManager(GameController gameController) {
        this.gameController = gameController;
    }

    public Space[][] initBoard(int numRows, int numCols, int numMines) {
        NUM_ROWS = numRows;
        NUM_COLS = numCols;
        NUM_MINES = numMines;

        if (NUM_MINES == 10) {
            board = new Space[9][9];
        } else if (NUM_MINES == 40) {
            board = new Space[16][16];
        } else {
            board = new Space[16][30];
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Space();
            }
        }

        return board;
    }


    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                out += board[i][j].getText() + " ";
            }
            out += "\n";
        }
        return out;
    }
}
