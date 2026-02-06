package org.daw2.beatriz.CursoExpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "tuitions")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.tuition.code.notEmpty}")
    @Size(max = 2, message = "{msg.tuition.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @NotNull(message = "{msg.course.endDate.notNull}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NotEmpty(message = "{msg.tuition.observation.notEmpty}")
    @Size(max = 500, message = "{msg.tuition.observation.size}")
    @Column(name = "observation", nullable = false)
    private String observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    public Tuition(String code, LocalDate date, String observation, Student student, Course course) {
        this.code = code;
        this.date = date;
        this.observation = observation;
        this.student = student;
        this.course = course;
    }
}
