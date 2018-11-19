package hackett.model;

import hackett.view.GuiHandler;

import javax.swing.*;
import java.awt.*;

public class Space extends JButton {

    private int neighborMines;
    private boolean hasMine;
    private boolean isHidden;
    private int value;
    Image mine;

    public static final Color COLOR_HIDDEN_SPACE = new Color(0x8fa0bc);
    public static final Color COLOR_REVEALED_SPACE = new Color(0xbecce2);

    public Space() {
        super();
        this.setEnabled(true);
        this.setBackground(COLOR_HIDDEN_SPACE);
        this.setOpaque(true);
        this.setBorderPainted(true);
        this.setVisible(true);

        neighborMines = 0;
        value = 0;
        hasMine = false;
        isHidden = true;
        mine = null;
    }

    public void init(boolean hasMine) {
        this.hasMine = hasMine;
    }

    @Override
    public String toString() {
        String out = "";
        out += "Text : " + this.getText();
        out += "Num Neighbor Mines : " + this.neighborMines;
        out += "Value : " + this.value;
        return out;
    }

}
