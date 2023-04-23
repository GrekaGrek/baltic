package com.air.baltic.mapper;

import com.air.baltic.domain.UserEntity;
import com.air.baltic.model.UserDTO;
import com.air.baltic.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "Testor";
    public static final String EMAIL = "personal@yahahoo.com";

    @Mock
    private UserRepository mockRepository;

    @InjectMocks
    private UserMapper mapper;

    @Test
    void mapToDTO() {
        var entity = fetchEntity();
        var expectedResult = fetchDTO();

        UserDTO mappingResult = mapper.mapToDTO(entity);

        assertThat(mappingResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void mapFromDTO() {
        var dto = fetchDTO();
        var expectedResult = fetchEntity();

        UserEntity actualResult = mapper.mapFromDTO(dto);

        assertThat(actualResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void mapUpdateFromDTO() {
        var dto = fetchDTO();
        var expectedResult = fetchEntity();
        var id = 1L;
        expectedResult.setId(id);
        when(mockRepository.getReferenceById(1L)).thenReturn(expectedResult);

        UserEntity actualResult = mapper.mapUpdateFromDTO(id, dto);

        assertThat(actualResult)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);

        verify(mockRepository).getReferenceById(id);
    }

    private UserEntity fetchEntity() {
        return new UserEntity()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setEmail(EMAIL);
    }

    private UserDTO fetchDTO() {
        return UserDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }
}