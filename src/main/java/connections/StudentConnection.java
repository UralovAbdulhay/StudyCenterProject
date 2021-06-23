package connections;

import entity.Group;
import entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentConnection extends Connections {

    public synchronized Student getStudentFromSql(int id) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM student  WHERE id = " + id + ";"
            );
            Student student = null;
            if (resultSet.next()) {
                student = new Student(
                        id,
                        resultSet.getString("full_name"),
                        parseToLocalDateTime(resultSet.getString("date")),
                        null
                );
            }
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized ObservableList<Student> getStudentListFromSql() {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM student order by id desc;"
            );

            ObservableList<Student> students = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Student student = null;
                int id = resultSet.getInt("id");
                student = new Student(
                        id,
                        resultSet.getString("full_name"),
                        parseToLocalDateTime(resultSet.getString("date")),
                        new GroupConnection().getGroupForStudent(id)
                );

                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized ObservableList<Student> getStudentListFromSqlByGroup(Group group) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT " +
                            "student.id as student_id, " +
                            "student.full_name, " +
                            "student.date as student_date, " +
                            "gr.id as grup_id " +
                            "FROM student " +
                            "INNER JOIN student_group on student_group.student_id = student.id " +
                            "INNER JOIN \"group\" gr on gr.id = student_group.group_id " +
                            "WHERE gr.id = " + group.getId() + " order by student.id desc ;"
            );

            ObservableList<Student> students = FXCollections.observableArrayList();
            while (resultSet.next()) {
                Student student = null;
                student = new Student(
                        resultSet.getInt("student_id"),
                        resultSet.getString("full_name"),
                        parseToLocalDateTime(resultSet.getString("student_date")),
                        group
                );
                students.add(student);
            }
            return students;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized void insertToStudent(Student student) {

        String sql = " INSERT OR IGNORE INTO student (full_name )" +
                " VALUES('" + student.getFullName() + "' );";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("insertToStudent statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String sql2 = String.format("INSERT OR IGNORE INTO student_group (" +
                "student_id," +
                " group_id ) values ((select max(student.id) from student), %d)", student.getGroup().getId());

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql2);
            System.out.println("insertToStudent_group statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void updateStudent_group(Student student, Group oldGroup) {

        // UPDATE "student" SET "full_name" = 'Abdulhay_' WHERE "id" = 1

        String sql = " UPDATE student_group SET " +
                "group_id = " + student.getGroup().getId() + " " +
                "WHERE student_id = " + student.getId() + " " +
                "and group_id = " + oldGroup.getId() +
                ";";
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("updateStudent_group_group statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public synchronized void updateStudent(Student student, Group oldGroup) {

        // UPDATE "student" SET "full_name" = 'Abdulhay_' WHERE "id" = 1

        String sql = " UPDATE student SET " +
                "full_name = '" + student.getFullName() + "' " +
                "WHERE id = " + student.getId() + " " +
                ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("updateStudent statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateStudent_group(student, oldGroup);


    }


    public synchronized void deleteStudent(Student student) {

        String sql = "DELETE FROM student WHERE id = " + student.getId() + ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("deleteStudent statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "DELETE FROM student_group WHERE student_id = " + student.getId() + ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql1);
            System.out.println("deleteStudent_group statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
