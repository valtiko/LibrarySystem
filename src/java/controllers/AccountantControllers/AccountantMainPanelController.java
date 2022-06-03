package controllers.AccountantControllers;

import controllers.SceneController;

import java.io.IOException;

public class AccountantMainPanelController {





    public void goToAccountantInvoicePanel() {
        try {
            SceneController.startScene("AccountantScenes/AccountantInvoicePanel");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void goToAccountantBudgetPanel() {
        try {
            SceneController.startScene("AccountantScenes/AccountantNewBookProposalPanel");
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
