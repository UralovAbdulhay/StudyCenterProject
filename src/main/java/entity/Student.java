package entity;

import lombok.*;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class Student extends Index {

    private int id;

    private String fullName;
    private LocalDateTime date;
    private Group group;

    public Student(String fullName, LocalDateTime date) {
        this.fullName = fullName;
        this.date = date;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", date=" + date +
                ", group=" + group +
                '}';
    }


    public String toStringForExport() {
        int id_ = id % 45;


        return
                "F190301" + ((id_ <= 9) ? 0 + "" + id_ : id_) +
                        " " + fullName + "\n";
    }
}
