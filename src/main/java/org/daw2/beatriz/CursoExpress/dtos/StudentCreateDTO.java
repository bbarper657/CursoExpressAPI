package org.daw2.beatriz.CursoExpress.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StudentCreateDTO {

    @NotEmpty(message = "{msg.student.name.notEmpty}")
    @Size(max = 100, message = "{msg.student.name.size}")
    private String name;

    @NotEmpty(message = "{msg.student.name.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.student.email.notValid}")
    @Size(max = 100)
    private String email;

    @NotEmpty(message = "{msg.student.phone.notEmpty}")
    @Size(max = 25, message = "{msg.student.phone.size}")
    private String phone;

    private MultipartFile image;
}
