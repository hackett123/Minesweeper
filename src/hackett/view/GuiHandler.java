package hackett.view;

import hackett.controller.GameController;
import hackett.model.Difficulty;
import hackett.model.Space;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GuiHandler implements Runnable {

    private static GuiHandler instance;
    private GameController gameController;

    private static int NUM_ROWS, NUM_COLS, NUM_MINES;

    private int flagCount;

    private Space[][] board;
    private Difficulty difficulty;

    private boolean firstMove;

    private boolean lostGame = false;

    //GUI Constants
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final Dimension DIM_CONTAINER = new Dimension(WIDTH, HEIGHT);
    private static final Dimension DIM_PANEL_GAME = new Dimension(WIDTH * 3 / 4, HEIGHT * 3 / 4);
    private static final Dimension DIM_PANEL_STATUS = new Dimension (WIDTH / 8, HEIGHT / 8);
    private static final Dimension DIM_PANEL_OPTIONS = new Dimension (WIDTH / 8, HEIGHT / 8);
    private final Color COLOR_BACKGROUND = new Color(0x252f3f);
    private final Color PINK_BACKGROUND = new Color(0xffcce3);
    private final Font BIG_PINK_FONT = new Font(Font.DIALOG, Font.BOLD, 48);
    private final Font MEDIUM_PINK_FONT = new Font(Font.DIALOG, Font.BOLD, 24);
    private final Font SMALL_PINK_FONT = new Font(Font.DIALOG, Font.BOLD, 16);

    /*
    GUI Component for frame. rest are declared locally.
     */
    private JFrame frame;
    private JPanel container, panelGame, panelStatus, panelOptions;
    JRadioButton beginnerDifficulty, intermediateDifficulty, expertDifficulty;
    private static JLabel timerLabel, minesRemaining;
    private static Timer timer;

    public static GuiHandler getInstance(GameController gameController) {
        if (instance == null) {
            instance = new GuiHandler(gameController);
        }
        return instance;
    }

    private GuiHandler(GameController gameController) {
        this.gameController = gameController;
        this.firstMove = true;
        this.flagCount = 0;
    }

    /*
    First, chooseDifficulty(). Then, set board to gc's start game.
     */
    private void startGame() {
        timer.stop();
        this.firstMove = true;
        this.lostGame = false;
        this.flagCount = 0;

        frame.dispose();
        run();

        this.difficulty = getDifficulty();
        this.board = gameController.startGame(difficulty);
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
                NUM_COLS = 32;
                break;
        }
        this.minesRemaining.setText("MINES REMAINING : " + NUM_MINES);
        initPanelGameComps();

        timer.restart();

        //this.gameController.printGame();
    }

    public void wonGame() {
        timer.stop();

        JFrame frameWonGame = new JFrame(":)");
        JPanel containerWonGame = new JPanel();
        containerWonGame.setPreferredSize(new Dimension(500, 200));
        JLabel lostLabel = new JLabel();
        lostLabel.setFont(BIG_PINK_FONT);
        lostLabel.setForeground(Color.WHITE);
        lostLabel.setText("Way to go!");
        containerWonGame.add(lostLabel, BorderLayout.CENTER);
        containerWonGame.setBackground(PINK_BACKGROUND);
        this.container.setVisible(true);
        frameWonGame.add(containerWonGame);
        frameWonGame.pack();
        frameWonGame.setVisible(true);

        frameWonGame.repaint();
        containerWonGame.validate();
    }

    private void loseGameMessage() {
        JFrame frameLostGame = new JFrame(":(");
        JPanel containerLostGame = new JPanel();
        containerLostGame.setPreferredSize(new Dimension(500, 200));
        JLabel lostLabel = new JLabel();
        lostLabel.setFont(BIG_PINK_FONT);
        lostLabel.setForeground(Color.WHITE);
        lostLabel.setText("This ain't it chief");
        containerLostGame.add(lostLabel, BorderLayout.CENTER);
        containerLostGame.setBackground(new Color(0xffcce3));
        this.container.setVisible(true);
        frameLostGame.add(containerLostGame);
        //frameLostGame.setPreferredSize(new Dimension(500, 500));
        frameLostGame.pack();
        frameLostGame.setVisible(true);

        frameLostGame.repaint();
        containerLostGame.validate();
    }

    public void loseGame() {
        if (!lostGame) {
            loseGameMessage();
        }
        lostGame = true;
        timer.stop();
    }


    /*
    Called once startGame button has been pressed. Create a menu w beginner, intermediate, expert. Once chosen,
    send output back to startGame within gameController.
     */
    private Difficulty getDifficulty() {
        if (this.difficulty == null) {
            this.difficulty = Difficulty.BEGINNER;
        }
        return this.difficulty;
    }
    private void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    /*
    revealSpace is called by the action listeners for our panelGame buttons. Calls upon gameController method to do
    all model managing. Then redraw everything.
     */
    private void revealSpace(Space space) {
        gameController.revealSpace(space, firstMove, false);
        if (firstMove) {
            firstMove = false;
        }
        this.frame.repaint();

        //need to overwrite validation to keep visible.
        this.container.validate();

        //this.gameController.printGame();
    }

    /*
    Initiate and instantiate all JComponents.
        JFrame frame, JPanel container, panelGame, panelStatus, panelOptions
            panelGame -> NUM_ROW x NUM_COL array of JButtons
            panelStatus -> smiley face, mines left, timer
            panelOptions -> start buttons.
     */
    @Override
    public void run() {
        //TODO : implement graphics

        this.frame = new JFrame("Hackett's Minesweeper");

        recreate();

        this.frame.setResizable(true);
        //this.frame.setPreferredSize(DIM_CONTAINER);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    private void recreate() {
        initPanels();
        initPanelOptionComps();
        initPanelStatusComps();
        assembleComps();
    }

    private void initPanels() {
        this.container = new JPanel();
        this.container.setPreferredSize(DIM_CONTAINER);
        this.container.setBackground(COLOR_BACKGROUND);

        this.panelGame = new JPanel();
        this.panelGame.setPreferredSize(DIM_PANEL_GAME);
        this.panelGame.setBackground(COLOR_BACKGROUND);

        this.panelOptions = new JPanel();
        this.panelOptions.setPreferredSize(DIM_PANEL_OPTIONS);
        this.panelOptions.setBackground(COLOR_BACKGROUND);

        this.panelStatus = new JPanel();
        this.panelStatus.setPreferredSize(DIM_PANEL_STATUS);
        this.panelStatus.setBackground(COLOR_BACKGROUND);
    }

    private void initPanelGameComps() {

        this.panelGame.setLayout(new GridLayout(NUM_ROWS, NUM_COLS));

        //space buttons already instantiated.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Space current = board[i][j];
                current.addMouseListener(new MouseAdapter() {
                     @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                     if (SwingUtilities.isLeftMouseButton(e)) {
                         //normal
                         current.setEnabled(false);
                         revealSpace(current);
                         current.setBackground(Space.COLOR_REVEALED_SPACE);
                         current.setOpaque(true);
                         current.setBorderPainted(true);
                     }
                     if (SwingUtilities.isRightMouseButton(e)) {
                         gameController.toggleFlag(current);
                         if (current.isFlagged()) {
                             flagCount++;
                         } else {
                             flagCount--;
                         }
                         minesRemaining.setText("MINES REMAINING : " + (NUM_MINES - flagCount));
                         frame.repaint();
                         container.validate();
                     }
                }
            });

                this.panelGame.add(current, i, j);
            }
        }

        this.frame.repaint();

        //need to overwrite validation to keep visible.
        this.container.validate();

    }

    private void initPanelOptionComps() {
        JButton startGame = new JButton("START NEW GAME");
        startGame.addActionListener(e -> startGame());


        beginnerDifficulty = new JRadioButton();
        intermediateDifficulty = new JRadioButton();
        expertDifficulty = new JRadioButton();
        JRadioButton[] radiobuttons = {beginnerDifficulty, intermediateDifficulty, expertDifficulty};

        for (JRadioButton rb : radiobuttons) {
            rb.setForeground((PINK_BACKGROUND));
            rb.setFont(SMALL_PINK_FONT);
        }


        beginnerDifficulty.setText("BEGINNER DIFFICULTY");
        beginnerDifficulty.addActionListener(e -> {
            setDifficulty(Difficulty.BEGINNER);
            intermediateDifficulty.setSelected(false);
            expertDifficulty.setSelected(false);
        });

        intermediateDifficulty.setText("INTERMEDIATE DIFFICULTY");
        intermediateDifficulty.addActionListener(e -> {
            setDifficulty(Difficulty.INTERMEDIATE);
            beginnerDifficulty.setSelected(false);
            expertDifficulty.setSelected(false);
        });

        expertDifficulty.setText("EXPERT DIFFICULTY");
        expertDifficulty.addActionListener(e -> {
            setDifficulty(Difficulty.EXPERT);
            beginnerDifficulty.setSelected(false);
            intermediateDifficulty.setSelected(false);
        });


        panelOptions.setLayout(new GridLayout(2, 3,1, 1));


        panelOptions.add(beginnerDifficulty);
        panelOptions.add(intermediateDifficulty);
        panelOptions.add(expertDifficulty);
        panelOptions.add(startGame);

        startGame.setBackground(PINK_BACKGROUND);
        startGame.setOpaque(true);
        startGame.setBorderPainted(false);
        startGame.setFont(SMALL_PINK_FONT);

        this.panelOptions.setVisible(true);
    }

    private void initPanelStatusComps() {

        this.panelStatus.setLayout(new GridLayout(1, 3));

        minesRemaining = new JLabel("MINES REMAINING : " + (NUM_MINES - flagCount));
        minesRemaining.setForeground(PINK_BACKGROUND);
        minesRemaining.setFont(SMALL_PINK_FONT);
        this.panelStatus.add(minesRemaining);

        ImageIcon guiView = null;
        try {
            Image image = ImageIO.read(new File("src/res/110logo.png"));
            image = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            guiView = new ImageIcon(image);

        } catch (IOException ie) {
            System.out.println("Image rendering error");
        }

        JLabel smiley = new JLabel(guiView, JLabel.CENTER);
        smiley.setPreferredSize(new Dimension(150, 100));

        smiley.setIcon(guiView);

        smiley.setBackground(PINK_BACKGROUND);
        smiley.setForeground(PINK_BACKGROUND);

        this.panelStatus.add(smiley);

        timerLabel = new JLabel("Time : 0");
        timerLabel.setForeground(PINK_BACKGROUND);
        timerLabel.setFont(MEDIUM_PINK_FONT);
        timer = new Timer(1000, new ActionListener() {
            int seconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText("Time : " + seconds++);
            }
        });

        this.panelStatus.add(timerLabel, 1, 2);
        this.panelStatus.setVisible(true);
    }

    private void assembleComps() {
        this.container.setLayout(new BorderLayout());
        this.container.add(panelStatus, BorderLayout.NORTH);
        this.container.add(panelGame, BorderLayout.CENTER);
        this.container.add(panelOptions, BorderLayout.SOUTH);
        this.container.setVisible(true);
        this.frame.add(container);
    }

}
