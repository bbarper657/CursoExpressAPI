package org.daw2.beatriz.CursoExpress.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TuitionDTO {
    private Long id;
    private String code;
    private LocalDate date;
    private String observation;
    private Long studentId;
    private Long courseId;
}
