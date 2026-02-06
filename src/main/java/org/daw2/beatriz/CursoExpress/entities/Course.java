package org.daw2.beatriz.CursoExpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.tuition.code.notEmpty}")
    @Size(max = 2, message = "{msg.tuition.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @NotEmpty(message = "{msg.course.name.notEmpty}")
    @Size(max = 100, message = "{msg.course.name.size}")
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty(message = "{msg.course.description.notEmpty}")
    @Size(max = 500, message = "{msg.course.description.size}")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "{msg.course.startDate.notNull}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull(message = "{msg.course.endDate.notNull}")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ModuleCourse> modulesCourses;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tuition> tuitions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Teacher teacher;

    public Course(String code, String name, String description, LocalDate startDate, LocalDate endDate, Teacher teacher) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
    }

}
