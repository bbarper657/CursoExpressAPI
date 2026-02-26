package org.daw2.beatriz.CursoExpress.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.daw2.beatriz.CursoExpress.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Page<Teacher> findAll(Pageable pageable);

    Page<Teacher> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsByEmail(@NotEmpty(message = "{msg.teacher.name.notEmpty}") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.agent.email.notValid}") @Size(max = 100, message = "{msg.teacher.email.size}") String email);

    boolean existsByEmailAndIdNot(@NotEmpty(message = "{msg.teacher.name.notEmpty}") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "{msg.agent.email.notValid}") @Size(max = 100, message = "{msg.teacher.email.size}") String email, Long id);
}
