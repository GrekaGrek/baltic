package com.air.baltic.service;

import com.air.baltic.domain.UserEntity;
import com.air.baltic.mapper.UserMapper;
import com.air.baltic.model.UserDTO;
import com.air.baltic.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "Testor";
    public static final String EMAIL = "personal@yahahoo.com";

    @Mock
    private UserMapper mockMapper;
    @Mock
    private UserRepository mockRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void successfullyAddUser() {
        var entity = createEntity();
        var item = createDTO();

        when(mockMapper.mapFromDTO(item)).thenReturn(entity);
        when(mockRepository.save(entity)).thenReturn(entity);

        service.addUser(item);

        verify(mockMapper).mapFromDTO(item);
        verify(mockRepository).save(entity);
    }

    @Test
    void addUserThrowExceptionCauseRecordWithProvidedEmailExistInDB() {
        var entity = createEntity();
        var item = createDTO();

        when(mockRepository.findByEmail(entity.getEmail())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.addUser(item))
                .isInstanceOf(EntityExistsException.class);

        verify(mockRepository).findByEmail(entity.getEmail());
        verify(mockMapper, never()).mapFromDTO(item);
        verify(mockRepository, never()).save(entity);
    }

    @Test
    void findUserById() {
        var entity = createEntity();
        var item = createDTO();

        when(mockRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));
        when(mockMapper.mapToDTO(entity)).thenReturn(item);

        var filterResult = service.findUserById(1L);

        assertThat(filterResult)
                .isNotNull()
                .hasValue(item);

        verify(mockRepository).findById(any(Long.class));
        verify(mockMapper).mapToDTO(entity);
    }

    @Test
    void userNotFoundById() {
        var entity = createEntity();

        assertThatThrownBy(() -> service.findUserById(entity.getId()))
                .isInstanceOf(EntityNotFoundException.class);

        verify(mockMapper, never()).mapToDTO(entity);
    }

    @Test
    void fetchUsers() {
        List<UserEntity> persons = new ArrayList<>();

        when(mockRepository.findAll()).thenReturn(persons);

        List<UserDTO> actualResult = service.fetchAllUsers();

        assertThat(actualResult)
                .isNotNull()
                .isEqualTo(persons);

        verify(mockRepository).findAll();
    }

    @Test
    void updateUser() {
        var id = 1L;
        var entity = createEntity();
        entity.setId(id);
        var item = createDTO();

        when(mockMapper.mapUpdateFromDTO(id, item)).thenReturn(entity);
        when(mockRepository.save(entity)).thenReturn(entity);
        when(mockMapper.mapToDTO(entity)).thenReturn(item);

        var actualUpdate = service.updateUser(item, id);

        assertThat(actualUpdate)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(item);

        verify(mockMapper).mapUpdateFromDTO(id, item);
        verify(mockRepository).save(entity);
        verify(mockMapper).mapToDTO(entity);
    }

    @Test
    void updateUserFailedIdIsNull() {
        var item = UserDTO.builder().build();

        when(mockMapper.mapUpdateFromDTO(null, item)).thenThrow(new EntityNotFoundException());

        assertThatThrownBy(() -> service.updateUser(item, null))
                .isInstanceOf(EntityNotFoundException.class);

        verify(mockMapper).mapUpdateFromDTO(null, item);
        verify(mockRepository, never()).save(any(UserEntity.class));
        verify(mockMapper, never()).mapToDTO(any(UserEntity.class));
    }

    @Test
    void deleteUser() {
        var entity = createEntity();
        entity.setId(1L);

        service.deleteUser(entity.getId());

        verify(mockRepository).deleteById(entity.getId());
    }

    private UserEntity createEntity() {
        return new UserEntity()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setEmail(EMAIL);
    }

    private UserDTO createDTO() {
        return UserDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }
}