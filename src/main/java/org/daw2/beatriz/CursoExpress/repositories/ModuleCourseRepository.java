package org.daw2.beatriz.CursoExpress.repositories;

import org.iesalixar.daw2.beatriz.CursoExpress.entities.ModuleCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleCourseRepository extends JpaRepository<ModuleCourse, Long> {

    Page<ModuleCourse> findAll(Pageable pageable);

    Page<ModuleCourse> findByNameContainingIgnoreCase(String name, Pageable pageable);

    long countByNameContainingIgnoreCase(String name);

    boolean existsModuleCourseByCode(String code);
}
