package hackett.controller;

import hackett.model.Difficulty;
import hackett.model.ModelManager;
import hackett.model.Space;
import hackett.view.GuiHandler;

import javax.swing.SwingUtilities;

public class GameController {

    /*
    MVC components
     */
    private static GameController instance;
    private ModelManager modelManager;
    private GuiHandler guiHandler;

    /*
    Instance fields.
     */
    boolean hasWon, hasLost;
    private static int NUM_ROWS, NUM_COLS, NUM_MINES;

    //board is instantiated by model and passed by reference to C and V.
    private Space[][] board;


    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private GameController() {
        this.hasWon = false;
        this.hasLost = false;
        modelManager = ModelManager.getInstance(this);
        SwingUtilities.invokeLater(guiHandler = GuiHandler.getInstance(this));
    }

    public void loseGame() {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                revealSpace(board[i][j], false, true);
            }
        }

        this.hasLost = true;
        guiHandler.loseGame();
    }

    /*
    Given a space from the guiHandler, pass it to the modelManager. modelManager will modify space and all other
    relevant spaces' states. Once done, the guiHandler will redraw the grid based upon the states of each mine.
     */
    public void revealSpace(Space space, boolean firstMove, boolean hasLost) {
        if (firstMove) {
            modelManager.fillMines(space);
            modelManager.countNeighbors();
        }
        modelManager.revealSpace(space, hasLost);
    }


    /*
    Init values for static values and send to model. receive board and return it.
    Prompt menu for difficulty and then send that value to the model manager to initiate the board;
        Note that the spaces themselves are not fully filled until first move made.
     */
    public Space[][] startGame(Difficulty difficulty) {

        switch (difficulty) {
            case BEGINNER :
                NUM_MINES = 10;
                NUM_ROWS = 9;
                NUM_COLS = 9;
                break;
            case INTERMEDIATE :
                NUM_MINES = 40;
                NUM_ROWS = 16;
                NUM_COLS = 16;
                break;
            case EXPERT :
                NUM_MINES = 99;
                NUM_ROWS = 16;
                NUM_COLS = 30;
                break;
        }
        System.out.println("Difficulty selected : " + difficulty);
        System.out.println("Num rows : " + NUM_ROWS);
        System.out.println("Num cols : " + NUM_COLS);
        System.out.println("Num mines : " + NUM_MINES);

        this.board = modelManager.initBoard(NUM_ROWS, NUM_COLS, NUM_MINES);
        return this.board;
    }

    /*
    Reveal all spaces. Use hasWon or hasLost to determine appropriate message to user.
     */
    public void endGame() {
        //somehow need to kill program.
    }

    /*
    Just startGame() without the menu for difficulty.
     */
    public Space[][] restartGame() {
        //TODO : IMPLEMENT

        return null;
    }

    public void wonGame() {
        guiHandler.wonGame();
    }

    /*
    If user clicks the quit button, this method is called. Kills program.
     */
    public void quitGame() {

    }

    public void printGame() {
        System.out.println(modelManager);
    }

}
