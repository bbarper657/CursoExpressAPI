package org.daw2.beatriz.CursoExpress.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ModuleCourseDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private LocalTime duration;
    private LocalDate publicationDate;
    private Long courseId;
}
