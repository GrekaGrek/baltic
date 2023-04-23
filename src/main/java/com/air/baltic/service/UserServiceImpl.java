package com.air.baltic.service;

import com.air.baltic.mapper.UserMapper;
import com.air.baltic.model.UserDTO;
import com.air.baltic.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDTO> fetchAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void addUser(UserDTO user) {
        var email = user.email();
        log.debug("Creating new User with email: {}", email);

        if (repository.findByEmail(email).isPresent()) {
            throw new EntityExistsException("User with provided email already exist");
        }
        repository.save(mapper.mapFromDTO(user));
    }

    @Override
    public Optional<UserDTO> findUserById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(userNotFound());

        return ofNullable(mapper.mapToDTO(entity));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO user, Long id) {
        var entityToUpdate = mapper.mapUpdateFromDTO(id, user);

        repository.save(entityToUpdate);
        log.debug("Updated User with id: {}.", id);

        return mapper.mapToDTO(entityToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    private Supplier<EntityNotFoundException> userNotFound() {
        return () -> new EntityNotFoundException("By provided id User is not found");
    }
}
