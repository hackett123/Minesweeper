package hackett.model;

import hackett.controller.GameController;

public class ModelManager {

    private static ModelManager instance;
    private GameController gameController;

    private static int NUM_ROWS, NUM_COLS, NUM_MINES;

    private static int UNREVEALED_SPACES;

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
        UNREVEALED_SPACES = NUM_ROWS * NUM_COLS;
        Space.resetNumSpaces();

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
                board[i][j].setX(i);
                board[i][j].setY(j);
            }
        }

        return board;
    }

    public void revealSpace(Space space, boolean hasLost) {
        if (space.isHidden()) {
            int thisX = space.getXPos();
            int thisY = space.getYPos();
            Space thisSpace = board[space.getXPos()][space.getYPos()];
            thisSpace.reveal();

            if (thisSpace.getNeighborMines() == -1) {
                //found mine, lost game.
                gameController.loseGame();
                return;
            }

            if (thisSpace.getNeighborMines() == 0) {
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        if (y == 0 && x == 0) {
                            continue;
                        } else {
                            try {
                                Space thatSpace = board[thisX + x][thisY + y];
                                if (thatSpace.isHidden()) {
                                    revealSpace(thatSpace, hasLost);
                                }
                            } catch (IndexOutOfBoundsException e) {
                                continue;
                            }
                        }
                    }
                }
            }

            if (!hasLost) {
                UNREVEALED_SPACES--;
                if (UNREVEALED_SPACES == NUM_MINES) {
                    gameController.wonGame();
                }
            }

        }
    }

    public void fillMines(Space exceptThisSpace) {
        int currMine;

        for (currMine  = 0; currMine < NUM_MINES; currMine++) {

            int randX = (int) (Math.random() * NUM_ROWS);
            int randY = (int) (Math.random() * NUM_COLS);

//            System.out.println("Curr count : " + currMine);
//            System.out.println("X : " + randX);
//            System.out.println("Y : " + randY);
            if (board[randX][randY].hasMine()) {
                currMine--;
            }
            if ((exceptThisSpace == board[randX][randY])) {
                currMine--;
            } else {
                board[randX][randY].setToMine();
                //System.out.println("Mine given to " + randX + " , " + randY);
            }
        }
    }

    private int helpCount(int i, int j) {
        int currNeighbors = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (y == 0 && x == 0) {
                    continue;
                } else {
                    try {
                        if (board[i + x][j + y].hasMine()) {
                            currNeighbors++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
        }
        return currNeighbors;
    }

    /*
    Method traverses throughout initially created board and determines the number
    of bombs surrounding each individual space. We skip those which hold mines,
    as we represent those as -1 within the space class.
     */
    public void countNeighbors() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Space curr = board[i][j];
                if (curr.hasMine()) {
                    continue;
                }
                int currNeighbors = helpCount(i, j);

                curr.setNeighborMines(currNeighbors);
            }
        }
    }

    public void toggleFlag(Space space) {
        space.toggleFlag();
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
