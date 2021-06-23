package entity;

import javafx.collections.ObservableList;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group extends Index {

    private int id;
    private String name;
    private LocalDateTime date;
    private ObservableList<Student> students;

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
        this.date = LocalDateTime.now();
    }


    @Override
    public String toString() {
        return name;
    }



    public String toStringForExport() {

        StringBuilder builder = new StringBuilder();

        students.forEach(e->{
            builder.append(e.toStringForExport());
        });

        return builder.toString();
    }


}
