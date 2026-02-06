package org.daw2.beatriz.CursoExpress.repositories;

import org.iesalixar.daw2.beatriz.CursoExpress.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);
}
