package controllers;

import connections.GroupConnection;
import connections.LessonConnection;
import entity.Group;
import entity.Lesson;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class NewLessonController implements Initializable {

    @FXML
    private TextField input;

    @FXML
    private ComboBox<Group> groupBox;

    @FXML
    private Button canBt;

    @FXML
    private Button okBt;


    private Lesson lesson;

    @FXML
    void cancel(ActionEvent event) {

        canBt.getScene().getWindow().hide();

    }


    @FXML
    void submit(ActionEvent event) {
        lesson = new Lesson(0, groupBox.getValue(), LocalDateTime.now(TimeZone.getDefault().toZoneId()),
                input.getText(), null);
        new LessonConnection().insertToLesson(lesson);

        canBt.getScene().getWindow().hide();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        groupBox.setItems(new GroupConnection().getGroupListFromSql());
        setGroup();

    }

    @FXML
    void setGroup() {
        okBt.setDisable((
                        groupBox.getValue() == null
                                || input.getText().trim().isEmpty()
                )
        );
    }

}