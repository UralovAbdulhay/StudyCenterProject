package connections;

import entity.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class GroupConnection extends Connections {


    public synchronized Group getGroupFromSql(int id) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM \"group\"  WHERE id = " + id + " order by id desc;"
            );
            Group group = null;
            if (resultSet.next()) {
                group = new Group(
                        id,
                        resultSet.getString("name"),
                        parseToLocalDateTime(resultSet.getString("date")),
                        null
                );
                group.setStudents(new StudentConnection().getStudentListFromSqlByGroup(group));
            }
            return group;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized ObservableList<Group> getGroupListFromSql() {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM \"group\" order by id desc;"
            );

            ObservableList<Group> groups = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Group group = null;
                int id = resultSet.getInt("id");
                group = new Group(
                        id,
                        resultSet.getString("name"),
                        parseToLocalDateTime(resultSet.getString("date")),
                        null
                );
                group.setStudents(new StudentConnection().getStudentListFromSqlByGroup(group));


                groups.add(group);
            }
            return groups;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Group getGroupForStudent(int studentId) {
        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT     " +
                            "       gr.id,\n" +
                            "       gr.name,\n" +
                            "       gr.date\n" +
                            "FROM \"group\" gr\n" +
                            "         inner join student_group on student_group.group_id = gr.id\n" +
                            "         INNER JOIN student on \"student_group\".student_id = student.id\n" +
                            "where student.id = " + studentId + " order by gr.id desc;"
            );

            Group group = null;
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                group = new Group(
                        id,
                        resultSet.getString("name"),
                        parseToLocalDateTime(resultSet.getString("date")),
                        null
                );

            }

            return group;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized void insertToGroup(Group group) {

        String sql = " INSERT OR IGNORE INTO \"group\" (name)" +
                " VALUES('" + group.getName() + "' );";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("insertToGroup statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public synchronized void updateGroup(Group group) {

        String sql = " UPDATE \"group\" SET " +
                "name = '" + group.getName() + "' " +
                "WHERE id = " + group.getId() + " " +
                ";";
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("updateGroup statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void deleteGroup(Group group) {

        try (Connection con = connect()) {

            if ((con.createStatement()
                    .executeQuery(
                            "select count(group_id) as count from student_group where group_id = "
                                    + group.getId() + " ;"
                    ).getInt("count")) == 0
            ) {
                con.close();

                String sql = "DELETE FROM \"group\" WHERE id = " + group.getId() + ";";

                try (Connection connection = connect()) {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    System.out.println("deleteGroup statement = " + statement.executeUpdate());
                    new LessonConnection().deleteLesson(group);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }





}
