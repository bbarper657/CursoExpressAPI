package org.daw2.beatriz.CursoExpress.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.beatriz.CursoExpress.entities.Tuition;
import org.iesalixar.daw2.beatriz.CursoExpress.repositories.TuitionRepository;
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
@RequestMapping("/tuitions")
public class TuitionController {
    private static final Logger logger = LoggerFactory.getLogger(TuitionController.class);

    @Autowired
    private TuitionRepository tuitionRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String listTuitions(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(required = false) String search,
                              @RequestParam(required = false) String sort,
                              Model model) {
        logger.info("Solicitando la lista de todas las Matrículas..." + search);
        Pageable pageable = PageRequest.of(page - 1, 5, getSort(sort));
        Page<Tuition> tuitions;
        int totalPages = 0;
        if (search != null && !search.isBlank()) {
            tuitions = tuitionRepository.findByObservationContainingIgnoreCase(search, pageable);
            totalPages = (int) Math.ceil((double) tuitionRepository.countByObservationContainingIgnoreCase(search) / 5);
        } else {
            tuitions = tuitionRepository.findAll(pageable);
            totalPages = (int) Math.ceil((double) tuitionRepository.count() / 5);
        }
        logger.info("Se han cargado {} Matrículas.", tuitions.toList().size());
        model.addAttribute("listTuitions", tuitions.toList());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        return "tuition";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva Matrícula...");
        model.addAttribute("tuition", new Tuition());
        return "tuition-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la Matrícula con ID {}", id);
        Optional<Tuition> tuitionOpt = tuitionRepository.findById(id);
        if (!tuitionOpt.isPresent()) {
            logger.warn("No se encontró la matrícula con ID {}", id);
        }
        model.addAttribute("tuition", tuitionOpt.get());
        return "tuition-form";
    }

    @PostMapping("/insert")
    public String insertTuition(@Valid @ModelAttribute("course") Tuition tuition, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nueva matrícula con código {}", tuition.getCode());
        if (result.hasErrors()) {
            return "tuition-form";
        }
        if (tuitionRepository.existsTuitionByCode(tuition.getCode())) {
            logger.warn("El código de la matrícula {} ya existe.", tuition.getCode());
            String errorMessage = messageSource.getMessage("El código de la matrícula ya existe", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/tuitions/new";
        }

        tuitionRepository.save(tuition);
        logger.info("Matrículas {} insertados con éxito.", tuition.getCode());
        return "redirect:/tuitions";
    }

    @PostMapping("/update")
    public String updateTuition(@Valid @ModelAttribute("course") Tuition tuition, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando Matrícula con ID {}", tuition.getId());
        if (result.hasErrors()) {
            return "tuition-form";
        }
        try {
            tuitionRepository.save(tuition);
            redirectAttributes.addFlashAttribute("successMessage", "Matrícula actualizada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar la matrícula");
        }
        return "redirect:/tuitions";
    }

    @PostMapping("/delete")
    public String deleteTuition(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando Matrícula con ID {}", id);
        try {
            tuitionRepository.deleteById(id);
            logger.info("Matrícula con ID {} eliminada con éxito.", id);
            redirectAttributes.addFlashAttribute("successMessage", "Matrícula eliminada con éxito");
        } catch (Exception e) {
            logger.error("No se pudo eliminar la Matrícula con ID {}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar la matrícula");
        }
        return "redirect:/tuitions";
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
