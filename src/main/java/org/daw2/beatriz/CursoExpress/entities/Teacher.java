package org.daw2.beatriz.CursoExpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.teacher.name.notEmpty}")
    @Size(max = 100, message = "{msg.teacher.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotEmpty(message = "{msg.teacher.phone.notEmpty}")
    @Size(max = 25, message = "{msg.teacher.phone.size}")
    @Column(name = "phone", nullable = false, length = 25)
    private String phone;

    @NotEmpty(message = "{msg.teacher.name.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.agent.email.notValid}")
    @Size(max = 100, message = "{msg.teacher.email.size}")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotEmpty(message = "{msg.teacher.specialty.notEmpty}")
    @Size(max = 300, message = "{msg.teacher.specialty.size}")
    @Column(name = "specialty", nullable = false, length = 300)
    private String specialty;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Course> courses;

    public Teacher(String name, String phone, String email, String specialty) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialty = specialty;
    }
}
