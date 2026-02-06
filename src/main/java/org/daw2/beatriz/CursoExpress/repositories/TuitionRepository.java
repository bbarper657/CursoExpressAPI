package org.daw2.beatriz.CursoExpress.repositories;

import org.daw2.beatriz.CursoExpress.entities.Tuition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuitionRepository extends JpaRepository<Tuition, Long> {

    Page<Tuition> findAll(Pageable pageable);

    Page<Tuition> findByObservationContainingIgnoreCase(String obserbation, Pageable pageable);

    long countByObservationContainingIgnoreCase(String observation);

    boolean existsTuitionByCode(String code);
}
