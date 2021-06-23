package controllers;


import connections.GroupConnection;
import connections.LessonConnection;
import connections.StudentConnection;
import entity.Group;
import entity.Lesson;
import entity.Student;
import entity.Visit;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    @FXML
    private TextField homeSearch;

    @FXML
    private Button homeClear;

    @FXML
    private TableView<Lesson> homeTable;

    @FXML
    private TextField groupSearch;

    @FXML
    private Button gruopClear;

    @FXML
    private TableView<Group> groupTable;

    @FXML
    private TextField studentSearch;

    @FXML
    private Button studentClear;

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TextField lessonSearch;

    @FXML
    private Button lessonClear;

    @FXML
    private TableView<Lesson> lessonTable;


    private ObservableList<Group> groups = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private ObservableList<Lesson> lessonsForHome = FXCollections.observableArrayList();


    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    protected synchronized String localDateTimeParseToString(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    @FXML
    void groupClearAction(ActionEvent event) {
        groupSearch.clear();
        groupFind();
    }

    @FXML
    void groupFind() {

        String natija = groupSearch.getText().trim().toLowerCase();

        if (natija.isEmpty()) {
            groupTable.setItems(groups);
        } else {
            groupTable.setItems(
                    FXCollections.observableArrayList(
                            groups.stream()
                                    .filter(e -> e.toString().toLowerCase().contains(natija))
                                    .collect(Collectors.toList())
                    )
            );
        }
        groupTable.refresh();

    }

    @FXML
    void homeClearAction(ActionEvent event) {
        homeSearch.clear();
        homeFind();
    }

    @FXML
    void homeFind() {

        String natija = homeSearch.getText().trim().toLowerCase();

        if (natija.isEmpty()) {
            homeTable.setItems(lessonsForHome);
        } else {
            homeTable.setItems(
                    FXCollections.observableArrayList(
                            lessonsForHome.stream()
                                    .filter(e -> e.toString().toLowerCase().contains(natija))
                                    .collect(Collectors.toList())
                    )
            );
        }
        homeTable.refresh();

    }

    @FXML
    void lessonClearAction(ActionEvent event) {
        lessonSearch.clear();
        lessonFind();
    }

    @FXML
    void lessonFind() {

        String natija = lessonSearch.getText().trim().toLowerCase();

        if (natija.isEmpty()) {
            lessonTable.setItems(lessons);
        } else {
            lessonTable.setItems(
                    FXCollections.observableArrayList(
                            lessons.stream()
                                    .filter(e -> e.toString().toLowerCase().contains(natija))
                                    .collect(Collectors.toList())
                    )
            );
        }
        lessonTable.refresh();

    }

    @FXML
    void studentClearAction(ActionEvent event) {
        studentSearch.clear();
        studentFind();
    }

    @FXML
    void studentFind() {
        String natija = studentSearch.getText().trim().toLowerCase();

        if (natija.isEmpty()) {
            studentTable.setItems(students);
        } else {
            studentTable.setItems(
                    FXCollections.observableArrayList(
                            students.stream()
                                    .filter(e -> e.toString().toLowerCase().contains(natija))
                                    .collect(Collectors.toList())
                    )
            );
        }
        studentTable.refresh();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateLists();

        initHomeTab();
        initGroupTab();
        initStudentTab();
        initLessonTab();

        homeTable.setStyle("-fx-font-size: 14");
        groupTable.setStyle("-fx-font-size: 14");
        studentTable.setStyle("-fx-font-size: 14");
        lessonTable.setStyle("-fx-font-size: 14");


    }

    private void initHomeTab() {

        TableColumn<Lesson, Integer> number = creatTabCol("№", 60);
        TableColumn<Lesson, String> groupName = creatTabCol("Group", 250);
        TableColumn<Lesson, String> countOfStudents = creatTabCol("Students", 150);
        TableColumn<Lesson, String> countOfCame = creatTabCol("Attendings", 150);
        TableColumn<Lesson, String> countOfOutCame = creatTabCol("Absents", 150);
        TableColumn<Lesson, String> timeOfLesson = creatTabCol("Time of lesson", 250);
        TableColumn<Lesson, String> descriptionOfLesson = creatTabCol("Description", 250);

        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));


        groupName.setCellValueFactory(e -> (
                new SimpleStringProperty(e.getValue().getLesson_group().getName())
        ));


        countOfStudents.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getLesson_group().getStudents().size() + ""
                )
        ));


        countOfCame.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getVisits().stream().filter(Visit::isCame).count() + ""
                )
        ));

        countOfOutCame.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getVisits().stream().filter(r -> !r.isCame()).count() + ""
                )
        ));


        timeOfLesson.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getTimeOfLesson()
                )
        ));

        descriptionOfLesson.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getDescription()
                )
        ));

        homeTable.getColumns().clear();
        homeTable.getColumns().addAll(number, groupName, countOfStudents, countOfCame, countOfOutCame,
                timeOfLesson, descriptionOfLesson);
        homeTable.setItems(lessonsForHome);

        AtomicInteger tr = new AtomicInteger(1);
        homeTable.getItems().forEach(e -> e.setTr(tr.getAndIncrement()));

        homeTable.refresh();

    }


    private void initGroupTab() {

        TableColumn<Group, Integer> number = creatTabCol("№", 60);
        TableColumn<Group, String> nameOfGroup = creatTabCol("Name of group", 300);
        TableColumn<Group, String> countOfStudents = creatTabCol("Count of students", 300);
        TableColumn<Group, String> startTimeOfGroup = creatTabCol("Start time of group", 300);


        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));

        nameOfGroup.setCellValueFactory(e -> (
                new SimpleStringProperty(e.getValue().getName())
        ));


        countOfStudents.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getStudents().size() + ""
                )
        ));


        startTimeOfGroup.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        localDateTimeParseToString(e.getValue().getDate())
                )
        ));

        groupTable.getColumns().clear();
        groupTable.getColumns().addAll(number, nameOfGroup, countOfStudents, startTimeOfGroup);
        groupTable.setItems(groups);

        AtomicInteger tr = new AtomicInteger(1);
        groupTable.getItems().forEach(e -> e.setTr(tr.getAndIncrement()));

        groupTable.refresh();


        groupTable.setRowFactory(tableView -> {
            final TableRow<Group> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem editItem = new MenuItem(("Edit"));
            MenuItem removeItem = new MenuItem(("Delete"));
            MenuItem showItem = new MenuItem(("Show students"));
            MenuItem new_lesson = new MenuItem("New group");
            MenuItem new_lesson1 = new MenuItem("New group");


            editItem.setOnAction(e -> newGroup(groupTable.getSelectionModel().getSelectedItem()));
            new_lesson.setOnAction(e -> newGroup(null));
            new_lesson1.setOnAction(e -> newGroup(null));
            showItem.setOnAction(e -> showStudentsOfGroup(groupTable.getSelectionModel().getSelectedItem()));
            removeItem.setOnAction(e -> deleteGroup(groupTable.getSelectionModel().getSelectedItem()));


            rowMenu.getItems().addAll(editItem, new_lesson, showItem, new SeparatorMenuItem(), removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise(new ContextMenu(new_lesson1))
            );
            return row;
        });


    }


    private void initStudentTab() {

        TableColumn<Student, Integer> number = creatTabCol("№", 60);
        TableColumn<Student, String> fullName = creatTabCol("Full name", 300);
        TableColumn<Student, String> groupName = creatTabCol("Group name", 300);
        TableColumn<Student, String> DateOfCame = creatTabCol("Date of came", 300);


        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));

        fullName.setCellValueFactory(e -> (
                new SimpleStringProperty(e.getValue().getFullName())
        ));


        groupName.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getGroup().getName() + ""
                )
        ));


        DateOfCame.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        localDateTimeParseToString(e.getValue().getDate())
                )
        ));

        studentTable.getColumns().clear();
        studentTable.getColumns().addAll(number, fullName, groupName, DateOfCame);
        studentTable.setItems(students);

        AtomicInteger tr = new AtomicInteger(1);
        studentTable.getItems().forEach(e -> e.setTr(tr.getAndIncrement()));

        studentTable.refresh();

        studentTable.setRowFactory(tableView -> {
            final TableRow<Student> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem editItem = new MenuItem(("Edit"));
            MenuItem removeItem = new MenuItem(("Delete"));
            MenuItem add_student = new MenuItem("Add student");
            MenuItem add_student1 = new MenuItem("Add student");


            editItem.setOnAction(e -> newStudent(studentTable.getSelectionModel().getSelectedItem()));
            add_student.setOnAction(e -> newStudent(null));
            add_student1.setOnAction(e -> newStudent(null));
            removeItem.setOnAction(e -> deleteStudent(studentTable.getSelectionModel().getSelectedItem()));

            rowMenu.getItems().addAll(editItem, add_student, removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise(new ContextMenu(add_student1))
            );
            return row;
        });

    }


    private void initLessonTab() {

        TableColumn<Lesson, Integer> number = creatTabCol("№", 60);
        TableColumn<Lesson, String> groupName = creatTabCol("Lesson name", 300);
        TableColumn<Lesson, String> startTimeOfGroup = creatTabCol("Start time of lesson", 300);
        TableColumn<Lesson, String> descriptionOfLesson = creatTabCol("Description", 300);

        TableColumn<Lesson, String> countOfStudents = creatTabCol("Students", 150);
        TableColumn<Lesson, String> countOfCame = creatTabCol("Attendings", 150);
        TableColumn<Lesson, String> countOfOutCame = creatTabCol("Absents", 150);


        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));


        groupName.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getLesson_group().getName() + ""
                )
        ));


        startTimeOfGroup.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        localDateTimeParseToString(e.getValue().getDate())
                )
        ));

        descriptionOfLesson.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getDescription()
                )
        ));


        countOfStudents.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getLesson_group().getStudents().size() + ""
                )
        ));


        countOfCame.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getVisits().stream().filter(Visit::isCame).count() + ""
                )
        ));

        countOfOutCame.setCellValueFactory(e -> (
                new SimpleStringProperty(
                        e.getValue().getVisits().stream().filter(r -> !r.isCame()).count() + ""
                )
        ));

        lessonTable.getColumns().clear();
        lessonTable.getColumns().addAll(number, groupName, countOfStudents, countOfCame, countOfOutCame,
                startTimeOfGroup, descriptionOfLesson);
        lessonTable.setItems(lessons);

        AtomicInteger tr = new AtomicInteger(1);
        lessonTable.getItems().forEach(e -> e.setTr(tr.getAndIncrement()));

        lessonTable.refresh();


        lessonTable.setRowFactory(tableView -> {
            final TableRow<Lesson> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();

            MenuItem editItem = new MenuItem(("Start roll call"));
            MenuItem removeItem = new MenuItem(("Delete"));
            MenuItem new_lesson = new MenuItem("New lesson");
            MenuItem new_lesson1 = new MenuItem("New lesson");


            editItem.setOnAction(e -> editVisit());
            new_lesson.setOnAction(e -> newLesson());
            new_lesson1.setOnAction(e -> newLesson());
            removeItem.setOnAction(e -> deleteLesson(lessonTable.getSelectionModel().getSelectedItem()));

            rowMenu.getItems().addAll(editItem, new_lesson, removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise(new ContextMenu(new SeparatorMenuItem(), new_lesson1))
            );
            return row;
        });

    }

    private void editVisit() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/visitWindow.fxml"));

        Stage stage = new Stage();

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Edit visit");
        Scene scene = new Scene(parent);

        VisitController controller = loader.getController();
        controller.setGroup(lessonTable.getSelectionModel().getSelectedItem());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.initOwner(lessonTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);


        stage.setResizable(false);
        stage.showAndWait();
        updateLists();

    }


    private void showStudentsOfGroup(Group group) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/showStudents.fxml"));

        Stage stage = new Stage();

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle(group.getName() + "'s students");
        Scene scene = new Scene(parent);

        ShowStudentController controller = loader.getController();
        controller.setGroup(group);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.initOwner(lessonTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);


        stage.setResizable(false);
        stage.showAndWait();
        updateLists();

    }


    private void newLesson() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/newLesson.fxml"));

        Stage stage = new Stage();

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("New Lesson");
        Scene scene = new Scene(parent);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.initOwner(lessonTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);


        stage.setResizable(false);
        stage.showAndWait();
        updateLists();

    }



    private void newGroup(Group group) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/newGroup.fxml"));

        Stage stage = new Stage();

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("New group");
        Scene scene = new Scene(parent);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.initOwner(groupTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);

        NewGroupController controller = loader.getController();
        controller.setGroup(group);

        stage.setResizable(false);
        stage.showAndWait();
        updateLists();

    }


    private void deleteGroup(Group group) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete");
        alert.setContentText("Are you sure?");
        alert.setHeaderText("DELETE");

        if (alert.showAndWait().get() == ButtonType.OK) {
            new GroupConnection().deleteGroup(group);
            updateLists();
        }
    }



    private void newStudent(Student student) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/newStudent.fxml"));

        Stage stage = new Stage();

        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("New Student");
        Scene scene = new Scene(parent);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.initOwner(studentTable.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        newStudentController controller = loader.getController();
        controller.setStudent(student);


        stage.showAndWait();
        updateLists();

    }


    private void deleteLesson(Lesson lesson) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete");
        alert.setContentText("Are you sure?");
        alert.setHeaderText("DELETE");

        if (alert.showAndWait().get() == ButtonType.OK) {
            new LessonConnection().deleteLesson(lesson);
            updateLists();
        }
    }

    private void deleteStudent(Student student) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete");
        alert.setContentText("Are you sure?");
        alert.setHeaderText("DELETE");

        if (alert.showAndWait().get() == ButtonType.OK) {
            new StudentConnection().deleteStudent(student);
            updateLists();
        }
    }


    private <S, T> TableColumn<S, T> creatTabCol(String title, double d) {
        TableColumn<S, T> newColumn = new TableColumn<S, T>(title);
        newColumn.setPrefWidth(d);
        newColumn.setStyle("-fx-alignment: CENTER");
        return newColumn;

    }

    private void updateLists() {
        groups = new GroupConnection().getGroupListFromSql();
        students = new StudentConnection().getStudentListFromSql();
        lessons = new LessonConnection().getLessonListFromSql();
        lessonsForHome = new LessonConnection().getLessonListFromSqlForHome(
                LocalDateTime.now(TimeZone.getDefault().toZoneId()));

        initGroupTab();
        initHomeTab();
        initLessonTab();
        initStudentTab();
    }

}

