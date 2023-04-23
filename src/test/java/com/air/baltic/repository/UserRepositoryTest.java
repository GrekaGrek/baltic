package com.air.baltic.repository;

import com.air.baltic.domain.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * For these tests execution DB should be running up with Docker.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/postgres"})
class UserRepositoryTest {
    public static final String EMAIL = "12Test!K@mail.com";

    @Autowired
    private UserRepository repository;

    @Test
    void canFindUserByEmail() {
        var userData0 = new UserEntity()
                .setLastName("Test")
                .setFirstName("Test")
                .setEmail("12Test!K@mail.com");

        repository.save(userData0);

        var userData1 = new UserEntity()
                .setLastName("Chest")
                .setFirstName("Test")
                .setEmail("test@frogner.com");

        repository.save(userData1);

        var foundPerson = repository.findByEmail(EMAIL);

        assertThat(foundPerson)
                .isPresent()
                .isEqualTo(Optional.of(userData0));
    }

    @Test
    void findUserByEmailReturnNoRecords() {
        var userData0 = new UserEntity()
                .setLastName("Test")
                .setFirstName("Test")
                .setEmail("test@test.com");

        repository.save(userData0);

        var userData1 = new UserEntity()
                .setLastName("Test")
                .setFirstName("Test")
                .setEmail("test@test.com");

        repository.save(userData1);

        var foundPerson = repository.findByEmail(EMAIL);

        assertThat(foundPerson).isEmpty();
    }
}