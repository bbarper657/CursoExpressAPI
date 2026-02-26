package org.daw2.beatriz.CursoExpress.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.daw2.beatriz.CursoExpress.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAll(Pageable pageable);

    Page<Course> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsCourseByCode(String code);

    boolean existsByCodeAndIdNot(@NotEmpty(message = "{msg.tuition.code.notEmpty}") @Size(max = 2, message = "{msg.tuition.code.size}") String code, Long id);
}
