package org.daw2.beatriz.CursoExpress.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.daw2.beatriz.CursoExpress.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsByEmail(@NotEmpty(message = "{msg.student.name.notEmpty}") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.student.email.notValid}") @Size(max = 100) String email);

    boolean existsByEmailAndIdNot(@NotEmpty(message = "{msg.student.name.notEmpty}") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.student.email.notValid}") @Size(max = 100) String email, Long id);
}
