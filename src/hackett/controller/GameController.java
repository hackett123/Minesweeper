package hackett.controller;

import hackett.model.ModelManager;
import hackett.view.GuiHandler;

import javax.swing.SwingUtilities;

public class GameController {

    private static GameController instance;
    private ModelManager modelManager;;
    private GuiHandler guiHandler;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private GameController() {
        modelManager = ModelManager.getInstance(this);
        SwingUtilities.invokeLater(guiHandler = GuiHandler.getInstance(this));
    }

}
