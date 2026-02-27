package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
