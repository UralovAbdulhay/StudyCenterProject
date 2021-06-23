package controllers;

import connections.GroupConnection;
import connections.StudentConnection;
import entity.Group;
import entity.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class newStudentController implements Initializable {

    @FXML
    private TextField input;

    @FXML
    private ComboBox<Group> groupBox;

    @FXML
    private Button canBt;

    @FXML
    private Button okBt;

    private Student student;


    @FXML
    void cancel(ActionEvent event) {

        canBt.getScene().getWindow().hide();
    }


    @FXML
    void setGroup() {

        okBt.setDisable((
                        groupBox.getValue() == null
                                || input.getText().trim().isEmpty()
                )
        );


    }

    @FXML
    void submit(ActionEvent event) {

        if (this.student == null || this.student.getId() == 0) {
            Student student = new Student(0, input.getText().trim(), LocalDateTime.now(TimeZone.getDefault().toZoneId()),
                    groupBox.getValue());
            new StudentConnection().insertToStudent(student);
        } else {
            Group oldGroup = this.student.getGroup();
            this.student.setFullName(input.getText().trim());
            this.student.setGroup(groupBox.getValue());

            new StudentConnection().updateStudent(this.student, oldGroup);
        }


        input.getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGroup();
    }

    public void setStudent(Student student) {
        this.student = student;

        groupBox.setItems(new GroupConnection().getGroupListFromSql());

        if (this.student != null) {
            input.setText(student.getFullName());
            groupBox.setValue(student.getGroup());
        }

    }
}
