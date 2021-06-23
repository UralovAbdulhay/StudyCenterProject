package controllers;

import connections.GroupConnection;
import entity.Group;
import entity.Student;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class ShowStudentController implements Initializable {

    public Button okBt;
    public Button importBt;
    public Button exportBt;
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private Label groupNameLab;

    @FXML
    private Label leaasonTimeLab;

    @FXML
    private Label coutOfCame;


    private Group group;

    private File importFile;
    private File exportFile;


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

    public void setGroup(Group group) {


        this.group = group;

        studentTable.refresh();

        groupNameLab.setText(group.getName());
        leaasonTimeLab.setText("Started on\n" + localDateTimeParseToString(group.getDate()));

        initTable();

        coutOfCame.setText(
                studentTable.getItems().size() + ""
        );
    }


    private void initTable() {
        TableColumn<Student, Integer> number = creatTabCol("№", 60);
        TableColumn<Student, String> studentName = creatTabCol("Student name", 300);
        TableColumn<Student, String> date = creatTabCol("Date", 300);

        number.setCellValueFactory(e -> (
                new SimpleObjectProperty<>(e.getValue().getTr())
        ));

        studentName.setCellValueFactory(e -> (
                new SimpleStringProperty(e.getValue().getFullName())
        ));

        date.setCellValueFactory(e -> (
                new SimpleStringProperty(localDateTimeParseToString(e.getValue().getDate()))
        ));

        studentTable.getColumns().addAll(number, studentName, date);
        studentTable.setItems(group.getStudents());

        AtomicInteger i = new AtomicInteger(1);
        studentTable.getItems().forEach(e -> {
            e.setTr(i.getAndIncrement());
        });

        studentTable.refresh();


    }


    public void save(ActionEvent actionEvent) {

        okBt.getScene().getWindow().hide();

    }


    private synchronized String localDateTimeParseToString(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @FXML
    private void doImport(ActionEvent actionEvent) {


        try {

            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().clear();
            chooser.setTitle("Import from file");
            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("~.txt files", "*.txt"));

            chooser.setInitialDirectory((exportFile != null ? exportFile.getParentFile() : (importFile != null ? importFile.getParentFile() : null)));

            File file1 = chooser.showOpenDialog(importBt.getScene().getWindow());

            if (file1 == null) {
                return;
            }

            importFile = file1;

            BufferedReader reader = new BufferedReader(new FileReader(importFile));
            String s = "";
            StringBuilder result = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                result.append(s).append("\n");
            }


//            System.out.println(getStudentFromFile(result.toString()));

            ObservableList<Student> students = getStudentFromFile(result.toString());

            if (!students.isEmpty()) {


                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/addStudentsWindow.fxml"));

                Stage stage = new Stage();

                Parent parent = null;
                try {
                    parent = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stage.setTitle("Import students");
                Scene scene = new Scene(parent);

                AddStudentsController controller = loader.getController();
                controller.setGroup(group, students);

                scene.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ESCAPE) {
                        stage.close();
                    }
                });

                stage.setScene(scene);
                stage.initOwner(studentTable.getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);


                stage.setResizable(false);
                stage.showAndWait();
                updateLists();

            } else {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Format did not match!");
                alert.setHeaderText("Format fail!");

                alert.showAndWait();


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void updateLists() {

this.group = new GroupConnection().getGroupFromSql(group.getId());
        studentTable.setItems(group.getStudents());

        AtomicInteger integer = new AtomicInteger(1);
        studentTable.getItems().forEach(e -> {
            e.setTr(integer.getAndIncrement());
        });

        coutOfCame.setText(
                studentTable.getItems().size() + ""
        );

        studentTable.refresh();

    }

    private ObservableList<Student> getStudentFromFile(String s) {
        ObservableList<Student> students = FXCollections.observableArrayList();

        String[] lines = s.split("\n");

        for (String line : lines) {

            int id;
            StringBuilder fullName = new StringBuilder();

            String[] details = line.split(" ");


                for (int i1 = 1; i1 < details.length; i1++) {
                    fullName.append(details[i1]+" ");
                }

                Student student = new Student(fullName.toString(), LocalDateTime.now());

                students.add(student);
            }



        return students;

    }


    @FXML
    private void doExport() {

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().clear();
        chooser.setTitle("Save as");

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("~.txt files", "*.txt"));

        chooser.setInitialDirectory((exportFile != null ? exportFile.getParentFile() : (importFile != null ? importFile.getParentFile() : null)));

        File file1 = chooser.showSaveDialog(exportBt.getScene().getWindow());

        exportFile = file1 != null ? file1 : exportFile;

        if (exportFile != null) {
            write();
        }

    }

    private void write() {

        try {
            FileWriter fw = new FileWriter(this.exportFile);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.group.toStringForExport());
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
