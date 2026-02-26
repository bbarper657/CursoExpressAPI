package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ModuleCourseCreateDTO {
    @NotEmpty(message = "{msg.tuition.code.notEmpty}")
    @Size(max = 2, message = "{msg.tuition.code.size}")
    private String code;

    @NotEmpty(message = "{msg.moduleCourse.name.notEmpty}")
    @Size(max = 100, message = "{msg.moduleCourse.name.size}")
    private String name;

    @NotEmpty(message = "{msg.moduleCourse.description.notEmpty}")
    @Size(max = 500, message = "{msg.moduleCourse.description.size}")
    private String description;

    @NotNull(message = "{msg.course.duration.notNull}")
    private LocalTime duration;

    @NotNull(message = "{msg.moduleCourse.publicationDate.notNull}")
    private LocalDate publicationDate;
    private Long courseId;
}
