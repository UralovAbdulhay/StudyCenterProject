package connections;

import entity.Lesson;
import entity.Visit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class VisitConnection extends Connections {


    public synchronized ObservableList<Visit> getLessonListFromSql(int lesson_id) {

        try (Connection connection = connect()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM visit where lesson_id = " + lesson_id + " ;"
            );

            ObservableList<Visit> visits = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Visit visit = null;
                visit = new Visit(
                        new StudentConnection().getStudentFromSql(resultSet.getInt("student_id")),
                        resultSet.getBoolean("came")
                );

                visits.add(visit);
            }
            return visits;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public synchronized void insertToVisit(Lesson lesson) {



        lesson.getVisits().forEach(
                item -> {
                    try (Connection con = connect()) {

                        if ((con.createStatement().executeQuery(
                                "select count(lesson_id) as count from visit where lesson_id = "
                                        + lesson.getId() + " and student_id = " + item.getStudent().getId() + ";"
                        ).getInt("count")) == 0
                        ) {
                            con.close();
                            insertVisit(item, lesson.getId());
                        } else {
                            con.close();
                            updateVisit(item, lesson.getId());
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
    }

    private synchronized void insertVisit(Visit visit, int lessonId) {

        //   UPDATE "visit" SET came = true WHERE "lesson_id" = 1 AND "student_id" = 4
        String sql = "INSERT OR IGNORE INTO visit (lesson_id, student_id, came) VALUES " +
                String.format(" ( %d , %d, %d) ; ",
                        lessonId,
                        visit.getStudent().getId(),
                        visit.isCame() ? 1 : 0);

        System.out.println("insertVisit = \n " + sql);


        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("insertVisit statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private synchronized void updateVisit(Visit visit, int lessonId) {

        //   UPDATE "visit" SET came = true WHERE "lesson_id" = 1 AND "student_id" = 4


        String sql = "UPDATE visit SET " +
                " came = " + (visit.isCame() ? 1 : 0) + " " +
                "WHERE lesson_id = " + lessonId + " " +
                "AND student_id = " + visit.getStudent().getId() + ";";

        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("updateStudent statement = " + statement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
