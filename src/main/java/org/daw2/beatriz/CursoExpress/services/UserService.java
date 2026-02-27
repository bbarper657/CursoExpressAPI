package org.daw2.beatriz.CursoExpress.services;

import org.daw2.beatriz.CursoExpress.dtos.UserDTO;
import org.daw2.beatriz.CursoExpress.entities.User;
import org.daw2.beatriz.CursoExpress.mappers.UserMapper;
import org.daw2.beatriz.CursoExpress.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            return userMapper.toDTO(userOpt.get());
        }

        throw new RuntimeException("El usuario con ID " + id + " no existe");
    }

    public Long getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }
}
