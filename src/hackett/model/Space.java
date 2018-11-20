package hackett.model;

import hackett.view.GuiHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Space extends JButton {

    private int neighborMines;
    private boolean hasMine;
    private boolean isHidden;
    private boolean isFlagged;
    private int value;
    private ImageIcon mine;
    private ImageIcon flag;

    private int x;
    private int y;

    public static final Color COLOR_HIDDEN_SPACE = new Color(0x8fa0bc);
    public static final Color COLOR_REVEALED_SPACE = new Color(0xbecce2);

    private static int NUM_SPACES;

    public Space() {
        super();
        this.setText("  ");
        this.setEnabled(true);
        this.setBackground(COLOR_HIDDEN_SPACE);
        this.setOpaque(true);
        this.setBorderPainted(true);
        this.setVisible(true);

        //-1 for mines
        this.neighborMines = 0;
        this.value = 0;
        this.hasMine = false;
        this.isHidden = true;
        this.isFlagged = false;
        mine = null;
        NUM_SPACES++;
    }

    public void toggleFlag() {
        if (this.isFlagged) {
            this.unFlag();
        } else {
            this.markFlagged();
        }
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public void markFlagged() {
        this.isFlagged = true;
        this.flag = makeImageIcon("/Users/Michael/Desktop/Penn/Fall 2018/TA/CIS110/final_project/res/penn_flag.png");
    }
    public void unFlag() {
        this.isFlagged = false;
    }

    public void reveal() {
        this.isHidden = false;
        this.setText("" + neighborMines);
        this.setEnabled(false);
    }

    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public int getNeighborMines() {
        return this.neighborMines;
    }

    public boolean hasMine() {
        return this.hasMine;
    }

    public int getXPos() {
        return this.x;
    }
    public int getYPos() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setToMine() {
        this.hasMine = true;
        this.neighborMines = -1;
    }

    public ImageIcon makeImageIcon(String location) {
        ImageIcon guiView = null;
        try {
            Image image = ImageIO.read(new File(location));

            int sideLength = 0;
            if (NUM_SPACES == 81) {
                sideLength = 40;
            } else if (NUM_SPACES == 256) {
                sideLength = 25;
            } else {
                sideLength = 10;
            }
            image = image.getScaledInstance(sideLength,sideLength, Image.SCALE_SMOOTH);
            guiView = new ImageIcon(image);

        } catch (IOException ie) {
            System.out.println(ie);
        }
        return guiView;
    }

    @Override
    public String toString() {
        String out = "";
        out += "Text : " + this.getText();
        out += "Num Neighbor Mines : " + this.neighborMines;
        out += "Value : " + this.value;
        return out;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isHidden) {
            this.setBackground(COLOR_HIDDEN_SPACE);
            this.setOpaque(true);
            if (this.isFlagged) {
                if (this.flag == null) {
                    this.flag = makeImageIcon("res/penn_flag.png");
                }
                setIcon(this.flag);
                this.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setIcon(null);
            }
        } else {
            //remove flag first
            setIcon(null);

            this.setBackground(COLOR_REVEALED_SPACE);
            this.setOpaque(true);
            if (this.neighborMines > 0) {
                this.setText("" + this.neighborMines);
            } else if (this.neighborMines == -1) {
                if (this.mine == null) {
                    this.mine = makeImageIcon("res/bomb.jpg");
                }
                this.setIcon(this.mine);
                this.setHorizontalAlignment(SwingConstants.CENTER);
                setText("");
                this.setBackground(Color.WHITE);
                this.setForeground(Color.WHITE);
            } else {
                this.setText("");
            }
        }
    }

}
