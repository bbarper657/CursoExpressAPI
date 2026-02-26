package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.TuitionCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.TuitionDTO;
import org.daw2.beatriz.CursoExpress.entities.Tuition;
import org.springframework.stereotype.Component;

@Component
public class TuitionMapper {

    public TuitionDTO toDTO(Tuition tuition){
        TuitionDTO dto = new TuitionDTO();
        dto.setId(tuition.getId());
        dto.setCode(tuition.getCode());
        dto.setDate(tuition.getDate());
        dto.setObservation(tuition.getObservation());
        if (tuition.getStudent() != null){
            dto.setStudentId(tuition.getStudent().getId());
        }
        if (tuition.getCourse() != null) {
            dto.setCourseId(tuition.getCourse().getId());
        }
        return dto;
    }

    public Tuition toEntity(TuitionDTO dto){
        Tuition tuition = new Tuition();
        tuition.setId(dto.getId());
        tuition.setCode(dto.getCode());
        tuition.setDate(dto.getDate());
        tuition.setObservation(dto.getObservation());
        return tuition;
    }

    public Tuition toEntity(TuitionCreateDTO createDTO){
        Tuition tuition = new Tuition();
        tuition.setCode(createDTO.getCode());
        tuition.setDate(createDTO.getDate());
        tuition.setObservation(createDTO.getObservation());
        return tuition;
    }
}
