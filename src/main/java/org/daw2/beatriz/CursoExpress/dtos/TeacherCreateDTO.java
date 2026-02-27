package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherCreateDTO {
    @NotEmpty(message = "{msg.teacher.name.notEmpty}")
    @Size(max = 100, message = "{msg.teacher.name.size}")
    private String name;

    @NotEmpty(message = "{msg.teacher.phone.notEmpty}")
    @Size(max = 25, message = "{msg.teacher.phone.size}")
    private String phone;

    @NotEmpty(message = "{msg.teacher.name.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.agent.email.notValid}")
    @Size(max = 100, message = "{msg.teacher.email.size}")
    private String email;

    @NotEmpty(message = "{msg.teacher.specialty.notEmpty}")
    @Size(max = 300, message = "{msg.teacher.specialty.size}")
    private String specialty;
}
