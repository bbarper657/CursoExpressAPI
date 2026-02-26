package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TuitionCreateDTO {

    @NotEmpty(message = "{msg.tuition.code.notEmpty}")
    @Size(max = 2, message = "{msg.tuition.code.size}")
    private String code;

    @NotNull(message = "{msg.tuition.date.notNull}")
    private LocalDate date;

    @NotEmpty(message = "{msg.tuition.observation.notEmpty}")
    @Size(max = 500, message = "{msg.tuition.observation.size}")
    private String observation;
    private Long studentId;
    private Long courseId;
}
