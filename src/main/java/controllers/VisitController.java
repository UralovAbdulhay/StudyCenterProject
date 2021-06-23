package controllers;

import connections.VisitConnection;
import entity.Lesson;
import entity.Student;
import entity.Visit;
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
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class VisitController implements Initializable {

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

    private Lesson lesson;


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

    public void setGroup(Lesson lesson) {

        if (lesson != null) {
            this.lesson = lesson;

            studentTable.refresh();

            groupNameLab.setText(lesson.getLesson_group().getName());
            leaasonTimeLab.setText(lesson.getTimeOfLesson());


            initTable();
            setCounts();


        }

    }


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
        studentTable.setItems(lesson.getLesson_group().getStudents());

        AtomicInteger i = new AtomicInteger(1);
        studentTable.getItems().forEach(e -> {
            e.setTr(i.getAndIncrement());
        });

        studentTable.refresh();

        studentTable.getItems().forEach(e -> {
            e.getCheckBox().setOnAction(ac ->
                    setCounts()
            );

            lesson.getVisits().forEach(w->{
                if (e.getId() == w.getStudent().getId()) {
                    e.getCheckBox().setSelected(w.isCame());
                }
            });
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


        ObservableList<Visit> visits = FXCollections.observableArrayList();

        studentTable.getItems().forEach(
                e -> {
                    visits.add(new Visit(e, e.getCheckBox().isSelected()));
                }
        );

        lesson.setVisits(visits);

        new VisitConnection().insertToVisit(this.lesson);

        okBt.getScene().getWindow().hide();

    }


}
