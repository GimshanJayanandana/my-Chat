package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dto.RegistrationDto;
import lk.ijse.model.RegistrationModel;
import lk.ijse.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

public class loginFormController {

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private AnchorPane rootNode;


    private RegistrationModel registrationModel = new RegistrationModel();

    public void initialize() throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        thread.start();

    }

    @FXML
    void loginOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        try {
            boolean isValid = false;
            try {
                isValid = registrationModel.isValidUser(userName, password);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            if (isValid) {

                try {
                    registrationModel.getUserInfo(userName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chatWall.fxml"));
                Parent root = loader.load();

                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();

                RegistrationDto userDto = registrationModel.getUserInfo(userName);

                ChatWallFormController chatWallFormController = loader.getController();

                chatWallFormController.setUser(userDto);
                txtUserName.setText("");
                txtPassword.setText("");

            } else {
                new Alert(Alert.AlertType.ERROR, "User Name And Password Did Not Matched Try Again").showAndWait();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void signUpOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/registerForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Register");
    }
    @FXML
    void forgotPasswordOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/ForgotPassword.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.setTitle("Forgot Password");
    }
    @FXML
    void txtPasswordGoToLoginOnAction(ActionEvent event) {
        loginOnAction(new ActionEvent());
    }

    @FXML
    void txtUserNameGoToPasswordOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
}