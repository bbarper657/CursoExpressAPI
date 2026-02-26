package org.daw2.beatriz.CursoExpress.repositories;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.daw2.beatriz.CursoExpress.entities.Tuition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuitionRepository extends JpaRepository<Tuition, Long> {

    Page<Tuition> findAll(Pageable pageable);

    Page<Tuition> findByObservationContainingIgnoreCase(String obserbation, Pageable pageable);

    long countByObservationContainingIgnoreCase(String observation);

    boolean existsTuitionByCode(String code);

    boolean existsByCodeAndIdNot(@NotEmpty(message = "{msg.tuition.code.notEmpty}") @Size(max = 2, message = "{msg.tuition.code.size}") String code, Long id);
}
