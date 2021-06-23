package controllers;

import connections.StudentConnection;
import entity.Group;
import entity.Student;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.sql.Struct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class AddStudentsController implements Initializable {

    public Button okBt;
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private Label groupNameLab;

    @FXML
    private Label leaasonTimeLab;

    @FXML
    private Label coutOfCame;

    @FXML
    private Label coutOfNoCame;

    private Group group;
    private ObservableList<Student> students = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        studentTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                studentTable.getScene().getWindow().hide();
            }
        });

        studentTable.setStyle("-fx-font-size: 16");


    }

    private <S, T> TableColumn<S, T> creatTabCol(String title, double d) {
        TableColumn<S, T> newColumn = new TableColumn<S, T>(title);
        newColumn.setPrefWidth(d);
        newColumn.setStyle("-fx-alignment: CENTER");
        return newColumn;
    }

    public void setGroup(Group group, ObservableList<Student> students) {

        if (group != null) {
            this.group = group;
            this.students = students;

            groupNameLab.setText(group.getName());
            leaasonTimeLab.setText(localDateTimeParseToString(group.getDate()));

            initTable();
            setCounts();

        }

    }


    private synchronized String localDateTimeParseToString(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private void initTable() {
        TableColumn<Student, Integer> number = creatTabCol("№", 60);
        TableColumn<Student, String> studentName = creatTabCol("Student name", 300);
        TableColumn<Student, CheckBox> check = creatTabCol("Check", 300);

        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));

        studentName.setCellValueFactory(e -> (
                new SimpleStringProperty(e.getValue().getFullName())
        ));

        check.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getCheckBox())
        ));

        studentTable.getColumns().addAll(number, studentName, check);
        studentTable.setItems(students);

        AtomicInteger i = new AtomicInteger(1);
        studentTable.getItems().forEach(e -> {
            e.setTr(i.getAndIncrement());
        });

        studentTable.refresh();

        studentTable.getItems().forEach(e -> {
            e.getCheckBox().setOnAction(ac ->
                    setCounts()
            );
        });

    }

    private void setCounts() {
        coutOfCame.setText(
                studentTable.getItems().stream().filter(e -> e.getCheckBox().isSelected()).count() + ""
        );

        coutOfNoCame.setText(

                studentTable.getItems().stream().filter(e -> e.getCheckBox().isSelected()).count() -
                        studentTable.getItems().size() + ""
        );
        System.out.println("setCounts ******");
    }


    public void save(ActionEvent actionEvent) {

        studentTable.getItems().forEach(e -> {
            e.setGroup(this.group);

            if (e.getCheckBox().isSelected()) {
                new StudentConnection().insertToStudent(e);
            }

        });

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(studentTable.getItems().stream().filter(e -> e.getCheckBox().isSelected()).count() +
                " students were imported!");
        alert.setContentText("Imports were collected successful!");

        alert.showAndWait();


        okBt.getScene().getWindow().hide();

    }


}
