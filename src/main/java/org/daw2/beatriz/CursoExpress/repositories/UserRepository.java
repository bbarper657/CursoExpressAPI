package org.daw2.beatriz.CursoExpress.repositories;

import org.daw2.beatriz.CursoExpress.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repositorio para la entidad User que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad User.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar.
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.username= :username")
    Long getIdByUsername(String username);
}
