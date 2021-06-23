package controllers;

import connections.GroupConnection;
import entity.Group;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewGroupController implements Initializable {

    @FXML
    private TextField input;

    @FXML
    private Button canBt;

    @FXML
    private Button okBt;

    private Group group;

    @FXML
    void cancel(ActionEvent event) {
        input.getScene().getWindow().hide();
    }

    @FXML
    void submit(ActionEvent event) {

        String groupName = input.getText().trim().replace(" ", "_");

        if (this.group == null || this.group.getId() == 0) {
            Group group = new Group(0, groupName);

            new GroupConnection().insertToGroup(group);

        } else {
            this.group.setName(groupName);
            new GroupConnection().updateGroup(this.group);
        }
        input.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        check();
    }


    public void setGroup(Group group) {

        if (group != null) {
            this.group = group;
            input.setText(group.getName());
        }
    }

    @FXML
    private void check() {
        okBt.setDisable(input.getText().trim().isEmpty());
    }
}
