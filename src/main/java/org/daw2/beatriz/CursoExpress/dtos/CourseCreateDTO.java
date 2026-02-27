package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CourseCreateDTO {
    @NotEmpty(message = "{msg.tuition.code.notEmpty}")
    @Size(max = 2, message = "{msg.tuition.code.size}")
    private String code;

    @NotEmpty(message = "{msg.course.name.notEmpty}")
    @Size(max = 100, message = "{msg.course.name.size}")
    private String name;

    @NotEmpty(message = "{msg.course.description.notEmpty}")
    @Size(max = 500, message = "{msg.course.description.size}")
    private String description;

    @NotNull(message = "{msg.course.startDate.notNull}")
    private LocalDate startDate;

    @NotNull(message = "{msg.course.endDate.notNull}")
    private LocalDate endDate;
    private Long teacherId;
}
