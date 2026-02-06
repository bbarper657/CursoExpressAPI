package org.daw2.beatriz.CursoExpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.student.name.notEmpty}")
    @Size(max = 100, message = "{msg.student.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotEmpty(message = "{msg.student.name.notEmpty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.student.email.notValid}")
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotEmpty(message = "{msg.student.phone.notEmpty}")
    @Size(max = 25, message = "{msg.student.phone.size}")
    @Column(name = "phone", nullable = false, length = 25)
    private String phone;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tuition> tuitions;

    public Student(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

}
