package controllers.LibrarianControllers;

import controllers.SceneController;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LibrarianMainPanelController {
    public void goToLibrarianBorrowsPanel() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianBookRequestPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToLibrarianBookProposalPanel() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianBookProposalPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToLibrarianReportsPanel() {
        try {
            SceneController.startScene("LibrarianScenes/LibrarianReportsPanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToLoginScreen() {
        try {
            SceneController.startScene("LoginScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
