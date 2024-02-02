package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dto.RegistrationDto;
import lk.ijse.model.RegistrationModel;

import java.io.IOException;
import java.sql.SQLException;

public class forgotPasswordFormController {
    @FXML
    private AnchorPane forgotPasswordPane;

    @FXML
    private TextField txtStrongPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtVerifyContact;

    RegistrationModel registrationModel = new RegistrationModel();

    public void initialize(){
        txtUserName.setDisable(true);
        txtStrongPassword.setDisable(true);
    }
    private void clearFields(){
        txtVerifyContact.setText("");
        txtUserName.setText("");
        txtStrongPassword.setText("");
    }
    @FXML
    void btnBackOnAction(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/loginForm.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.forgotPasswordPane.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Forgot Password");
        stage.setResizable(false);
        stage.centerOnScreen();

    }
    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String name = txtUserName.getText();
        String password = txtStrongPassword.getText();
        int phoneNumber = Integer.parseInt(txtVerifyContact.getText());


        var dto = new RegistrationDto(name,password,phoneNumber);
        boolean isUpdated = registrationModel.update(dto);
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION,"Your Password Has Been Updated").show();
            clearFields();
        }
    }
    @FXML
    void txtSearchContactOnAction(ActionEvent event) throws SQLException {
        String phoneNumber = txtVerifyContact.getText();
        RegistrationDto registrationDto = registrationModel.search(phoneNumber);
        if (registrationDto != null){
            txtUserName.setText(registrationDto.getUserName());
            txtVerifyContact.setText(String.valueOf(registrationDto.getPhone_number()));
            txtUserName.setDisable(false);
            txtStrongPassword.setDisable(false);
            txtUserName.setDisable(false);
            txtStrongPassword.requestFocus();
        }else {
            new Alert(Alert.AlertType.INFORMATION,"User Is Not Found").show();
        }
    }
//    @FXML
//    void txtUserNameGoToStrongPasswordOnAction(ActionEvent event) {
//        txtStrongPassword.requestFocus();
//    }
}
