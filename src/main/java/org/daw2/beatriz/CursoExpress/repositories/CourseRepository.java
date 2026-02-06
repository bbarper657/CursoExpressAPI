package org.daw2.beatriz.CursoExpress.repositories;

import org.iesalixar.daw2.beatriz.CursoExpress.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAll(Pageable pageable);

    Page<Course> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsCourseByCode(String code);
}
