package entity;

import javafx.collections.ObservableList;
import lombok.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lesson extends Index {

    @Generated
    private int id;
    private Group lesson_group;
    private LocalDateTime date;
    private String description;

    private ObservableList<Visit> visits;

    public String getTimeOfLesson() {

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

//        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return dateFormat.format(this.date)+" \n"
                +timeFormat.format(this.date) + " - " +
                timeFormat.format(this.date.plusHours(2));

    }



}
