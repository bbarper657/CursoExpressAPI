package org.daw2.beatriz.CursoExpress.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.beatriz.CursoExpress.entities.ModuleCourse;
import org.iesalixar.daw2.beatriz.CursoExpress.repositories.ModuleCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/modulesCourses")
public class ModuleCourseController {
    private static final Logger logger = LoggerFactory.getLogger(ModuleCourseController.class);

    @Autowired
    private ModuleCourseRepository moduleCourseRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listModulesCourses(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(required = false) String search,
                                     @RequestParam(required = false) String sort,
                                     Model model) {
        logger.info("Solicitando la lista de todos los Módulos de Cursos..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<ModuleCourse> moduleCourses;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            moduleCourses = moduleCourseRepository.findByNameContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) moduleCourseRepository.countByNameContainingIgnoreCase(search) / 5);
        } else {
            moduleCourses = moduleCourseRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) moduleCourseRepository.count() / 5);
        }
        logger.info("Se han cargado {} Módulos de Cursos.", moduleCourses.toList().size());
        model.addAttribute("listModulesCourses", moduleCourses.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "moduleCourse";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo Módulo de Cursos...");
        model.addAttribute("moduleCourse", new ModuleCourse());
        return "moduleCourse-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el Módulo de Curso con ID {}", id);
        Optional<ModuleCourse> moduleCourseOpt = moduleCourseRepository.findById(id);
        if (!moduleCourseOpt.isPresent()) {
            logger.warn("No se encontró el módulo de curso con ID {}", id);
        }
        model.addAttribute("moduleCourse", moduleCourseOpt.get());
        return "moduleCourse-form";
    }

    @PostMapping("/insert")
    public String insertModuleCourse(@Valid @ModelAttribute("moduleCourse") ModuleCourse moduleCourse, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo módulo de curso con código {}", moduleCourse.getCode());
        if (result.hasErrors()) {
            return "moduleCourse-form";
        }
        if (moduleCourseRepository.existsModuleCourseByCode(moduleCourse.getCode())) {
            logger.warn("El código del módulo de curso {} ya existe.", moduleCourse.getCode());
            String errorMessage = messageSource.getMessage("El código del Módulo del Curso ya existe", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/modulesCourses/new";
        }

        moduleCourseRepository.save(moduleCourse);
        logger.info("Módulos de Cursos {} insertados con éxito.", moduleCourse.getCode());
        return "redirect:/modulesCourses";
    }

    @PostMapping("/update")
    public String updateModuleCourse(@Valid @ModelAttribute("moduleCourse") ModuleCourse moduleCourse, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando Módulo de Curso con ID {}", moduleCourse.getId());
        if (result.hasErrors()) {
            return "moduleCourse-form";
        }
        try {
            moduleCourseRepository.save(moduleCourse);
            redirectAttributes.addFlashAttribute("successMessage", "Módulo actualizado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar el módulo");
        }
        return "redirect:/modulesCourses";
    }

    @PostMapping("/delete")
    public String deleteModuleCourse(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando Módulo de Cursos con ID {}", id);
        try {
            moduleCourseRepository.deleteById(id);
            logger.info("Módulo con ID {} eliminado con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Módulo eliminado con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar el Módulo con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el módulo");
        }
        return "redirect:/modulesCourses";
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").ascending();
        }
        return switch (sort) {
            case "nameAsc" -> Sort.by("name").ascending();
            case "nameDesc" -> Sort.by("name").descending();
            case "codeAsc" -> Sort.by("code").ascending();
            case "codeDesc" -> Sort.by("code").descending();
            case "idDesc" -> Sort.by("id").descending();
            default -> Sort.by("id").ascending();
        };
    }
}
