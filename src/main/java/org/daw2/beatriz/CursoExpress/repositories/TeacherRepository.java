package org.daw2.beatriz.CursoExpress.repositories;

import org.iesalixar.daw2.beatriz.CursoExpress.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Page<Teacher> findAll(Pageable pageable);

    Page<Teacher> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);
}
