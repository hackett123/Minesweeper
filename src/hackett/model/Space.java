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
    private int value;
    ImageIcon mine;

    private int x;
    private int y;

    public static final Color COLOR_HIDDEN_SPACE = new Color(0x8fa0bc);
    public static final Color COLOR_REVEALED_SPACE = new Color(0xbecce2);

    public Space() {
        super();
        this.setText("  ");
        this.setEnabled(true);
        this.setBackground(COLOR_HIDDEN_SPACE);
        this.setOpaque(true);
        this.setBorderPainted(true);
        this.setVisible(true);

        //-1 for mines
        neighborMines = 0;
        value = 0;
        hasMine = false;
        isHidden = true;
        mine = null;
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

    public ImageIcon makeImageIcon() {
        ImageIcon guiView = null;
        try {
            Image image = ImageIO.read(new File("res/bomb.jpg"));
            image = image.getScaledInstance(this.getWidth(),this.getHeight(), Image.SCALE_SMOOTH);
            guiView = new ImageIcon(image);

        } catch (IOException ie) {
            System.out.println("Image rendering error");
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
        } else {
            this.setBackground(COLOR_REVEALED_SPACE);
            this.setOpaque(true);
            if (this.neighborMines > 0) {
                this.setText("" + this.neighborMines);
            } else if (this.neighborMines == -1) {
                this.setIcon(makeImageIcon());
                this.setText("");
            } else {
                this.setText("");
            }
        }
    }

}
