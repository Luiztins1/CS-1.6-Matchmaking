package com.unnamed.matchmaking.cs16_matchmaking.repository;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.repository.UserAuthRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserAuthRepositoryTest {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    UserAuthRequestDTO userAuthRequestDTO;

    @BeforeEach
    void setUp(){
        userAuthRequestDTO = createDefaultUserAuthRequestDto(createDefaultUserAuth());
    }

    @Test
    void shouldFindByLogin(){
        UserAuth userTest = testEntityManager.persistAndFlush(createDefaultUserAuth());
        testEntityManager.clear();

        Optional<UserAuth> user = userAuthRepository.findByLogin(userTest.getLogin());

        assertThat(user).isNotNull();
        assertThat(user.get().getLogin()).isEqualTo(userTest.getLogin());
    }

    @Test
    void shouldReturnWhenFindByLoginIsNull(){

        Optional<UserAuth> user = userAuthRepository.findByLogin("Marcus");

        assertThat(user).isEmpty();
        assertThat(user).isNotNull();
    }

    @Test
    void shouldExistsByLogin(){
        UserAuth user = testEntityManager.persistAndFlush(createDefaultUserAuth());
        testEntityManager.clear();

        boolean userExists = userAuthRepository.existsByLogin(user.getLogin());

        assertThat(userExists).isTrue();
    }

    @Test
    void shouldReturnNotExistsByLogin(){
        boolean userExists = userAuthRepository.existsByLogin("Marcus");

        assertThat(userExists).isFalse();
    }

    @Test
    void shouldDeleteByLogin(){
        UserAuth userDelete = testEntityManager.persistAndFlush(createDefaultUserAuth());
        testEntityManager.clear();

        userAuthRepository.deleteByLogin(userDelete.getLogin());

        UserAuth userFind = testEntityManager.find(UserAuth.class, userDelete.getId());

        assertThat(userFind).isNull();
    }

    @Test
    void shouldReturnNotExistForDeleteByLogin(){
        Optional<UserAuth> userNotExistsForDelete = userAuthRepository.findByLogin("Marcus");

        assertThat(userNotExistsForDelete).isEmpty();

        userAuthRepository.deleteByLogin("Marcus");

        Optional<UserAuth> userAfterDelete = userAuthRepository.findByLogin("Marcus");

        assertThat(userAfterDelete).isEmpty();

    }

    @Test
    void shouldPersistAndRetrievePlayerCorrectly(){
        UserAuth user = testEntityManager.persistAndFlush(createDefaultUserAuth());
        testEntityManager.clear();

        UserAuth userFind = testEntityManager.find(UserAuth.class, user.getId());

        assertThat(userFind).isNotNull();
        assertThat(userFind.getId()).isEqualTo(user.getId());
        assertThat(userFind.getLogin()).isEqualTo("admin");
        assertThat(userFind.getPassword()).isEqualTo("admin123");
        assertThat(userFind.getRoles()).containsExactly("ADMIN");
    }

    @Test
    void shouldFailWhenLoginIsNull(){
        UserAuth user = createDefaultUserAuth();
        user.setLogin(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(user))
                .isInstanceOf(org.hibernate.exception.ConstraintViolationException.class);

    }

    @Test
    void shouldFailWhenPasswordIsNull(){
        UserAuth user = createDefaultUserAuth();
        user.setPassword(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(user))
                .isInstanceOf(org.hibernate.exception.ConstraintViolationException.class);

    }

    @Test
    void shouldFailWhenRolesIsNull(){
        UserAuth user = createDefaultUserAuth();
        user.setRoles(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(user))
                .isInstanceOf(ConstraintViolationException.class);

    }


    private UserAuthRequestDTO createDefaultUserAuthRequestDto(UserAuth userAuth){
        if(userAuth == null) throw new ResourceNotFoundException("Usuário não encontrado.");
        return new UserAuthRequestDTO(
                userAuth.getId(),
                userAuth.getLogin(),
                userAuth.getPassword(),
                userAuth.getRoles()
        );
    }

    private UserAuth createDefaultUserAuth(){
        return new UserAuth(
               null,
                "admin",
                "admin123",
                List.of("ADMIN")
        );
    }
}
