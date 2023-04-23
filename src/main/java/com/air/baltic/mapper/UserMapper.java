package com.air.baltic.mapper;

import com.air.baltic.domain.UserEntity;
import com.air.baltic.model.UserDTO;
import com.air.baltic.repository.UserRepository;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    private final UserRepository repository;

    public UserMapper(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO mapToDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }

    public UserEntity mapFromDTO(UserDTO item) {
        if (item == null) {
            return null;
        }
        return new UserEntity()
                .setFirstName(item.firstName())
                .setLastName(item.lastName())
                .setEmail(item.email());
    }

    public UserEntity mapUpdateFromDTO(Long id, UserDTO item) {
        var entityToUpdate = repository.getReferenceById(id);

        entityToUpdate.setId(id);
        entityToUpdate.setFirstName(item.firstName());
        entityToUpdate.setLastName(item.lastName());
        entityToUpdate.setEmail(item.email());

        return entityToUpdate;
    }
}
