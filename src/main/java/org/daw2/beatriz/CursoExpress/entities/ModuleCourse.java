package org.daw2.beatriz.CursoExpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "modulesCourses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 2)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Course course;

    public ModuleCourse(String code, String name, String description, LocalTime duration, LocalDate publicationDate, Course course) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.publicationDate = publicationDate;
        this.course = course;
    }
}
