package hackett.model;

import hackett.controller.GameController;

public class ModelManager {

    private static ModelManager instance;
    private GameController gameController;

    public static ModelManager getInstance(GameController gameController) {
        if (instance == null) {
            instance = new ModelManager(gameController);
        }
        return instance;
    }

    private ModelManager(GameController gameController) {
        this.gameController = gameController;
    }
}
