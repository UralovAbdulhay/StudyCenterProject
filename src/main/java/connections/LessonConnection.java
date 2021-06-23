package connections;

import entity.Group;
import entity.Lesson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class LessonConnection extends Connections {


    public synchronized Lesson getLessonFromSql(int id) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM lesson  WHERE id = " + id + " order by id desc;"
            );
            Lesson lesson = null;
            if (resultSet.next()) {
                lesson = new Lesson(
                        resultSet.getInt("id"),
                        new GroupConnection().getGroupFromSql(resultSet.getInt("group_id")),
                        parseToLocalDateTime(resultSet.getString("lesson_date")),
                        resultSet.getString("description"),
                        new VisitConnection().getLessonListFromSql(id)
                );
            }
            return lesson;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized ObservableList<Lesson> getLessonListFromSql() {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM lesson order by id desc ;"
            );

            ObservableList<Lesson> lessons = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Lesson lesson = null;
                int id = resultSet.getInt("id");
                lesson = new Lesson(
                        id,
                        new GroupConnection().getGroupFromSql(resultSet.getInt("group_id")),
                        parseToLocalDateTime(resultSet.getString("lesson_date")),
                        resultSet.getString("description"),
                        new VisitConnection().getLessonListFromSql(id)
                );

                lessons.add(lesson);
            }
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    public synchronized ObservableList<Lesson> getLessonListFromSqlForHome(LocalDateTime time) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM lesson where lesson_date > '"+
                    dateTimeFormatter.format(time.minusHours(24)) +"' order by id desc;";
            ResultSet resultSet = statement.executeQuery(
                    sql
            );

            ObservableList<Lesson> lessons = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Lesson lesson = null;
                int id = resultSet.getInt("id");
                lesson = new Lesson(
                        id,
                        new GroupConnection().getGroupFromSql(resultSet.getInt("group_id")),
                        parseToLocalDateTime(resultSet.getString("lesson_date")),
                        resultSet.getString("description"),
                        new VisitConnection().getLessonListFromSql(id)
                );

                lessons.add(lesson);
            }
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }







    public synchronized void insertToLesson(Lesson lesson) {

//INSERT INTO "lesson" ("group_id", "description") VALUES (4, 'test')

        String sql = " INSERT OR IGNORE INTO lesson (description, group_id)" +
                " VALUES('" + lesson.getDescription() + "', " +
                lesson.getLesson_group().getId()+" );";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("insertToVisit statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public synchronized void updateLesson(Lesson lesson) {
//
//        String sql = " UPDATE lesson SET " +
//                "name = '" + group.getName() + "' " +
//                "WHERE id = " + group.getId() + " " +
//                ";";
//        try (Connection connection = connect()) {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            System.out.println("updateGroup statement = " + statement.executeUpdate());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    public synchronized void deleteLesson(Lesson lesson) {
        String sql = "DELETE FROM lesson WHERE id = " + lesson.getId() + ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("deleteLesson statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteLesson(Group group) {
        String sql = "DELETE FROM lesson WHERE group_id = " + group.getId() + ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("deleteLesson statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
