package org.daw2.beatriz.CursoExpress.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDTO {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String specialty;
}
