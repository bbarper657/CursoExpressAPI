package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.ModuleCourseDTO;
import org.daw2.beatriz.CursoExpress.entities.ModuleCourse;
import org.springframework.stereotype.Component;

@Component
public class ModuleCourseMapper {
    public ModuleCourseDTO toDTO(ModuleCourse moduleCourse){
        ModuleCourseDTO dto = new ModuleCourseDTO();
        dto.setId(moduleCourse.getId());
        dto.setCode(moduleCourse.getCode());
        dto.setName(moduleCourse.getName());
        dto.setDescription(moduleCourse.getDescription());
        dto.setDuration(moduleCourse.getDuration());
        dto.setPublicationDate(moduleCourse.getPublicationDate());
        if (moduleCourse.getCourse() != null){
            dto.setCourseId(moduleCourse.getCourse().getId());
        }
        return dto;
    }

    public ModuleCourse toEntity(ModuleCourseDTO dto){
        ModuleCourse moduleCourse = new ModuleCourse();
        moduleCourse.setId(dto.getId());
        moduleCourse.setCode(dto.getCode());
        moduleCourse.setName(dto.getName());
        moduleCourse.setDescription(dto.getDescription());
        moduleCourse.setDuration(dto.getDuration());
        moduleCourse.setPublicationDate(dto.getPublicationDate());
        return moduleCourse;
    }

    public ModuleCourse toEntity(ModuleCourseCreateDTO createDTO){
        ModuleCourse moduleCourse = new ModuleCourse();
        moduleCourse.setCode(createDTO.getCode());
        moduleCourse.setName(createDTO.getName());
        moduleCourse.setDescription(createDTO.getDescription());
        moduleCourse.setDuration(createDTO.getDuration());
        moduleCourse.setPublicationDate(createDTO.getPublicationDate());
        return moduleCourse;
    }
}
