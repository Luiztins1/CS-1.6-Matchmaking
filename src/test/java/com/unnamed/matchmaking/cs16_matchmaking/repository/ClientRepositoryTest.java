package com.unnamed.matchmaking.cs16_matchmaking.repository;

import com.unnamed.matchmaking.cs16_matchmaking.client.dto.ClientRequestDto;
import com.unnamed.matchmaking.cs16_matchmaking.client.entity.Client;
import com.unnamed.matchmaking.cs16_matchmaking.client.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.hibernate.exception.ConstraintViolationException;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldFindByClientLogin(){
        Client client = testEntityManager.persistAndFlush(createDefaultClient());
        testEntityManager.clear();

        Optional<Client> recovered = clientRepository.findByClientLogin(client.getClientLogin());
        assertThat(recovered).isNotNull();
    }

    @Test
    void shouldWhenFindByClientLoginDoesNotExists(){
        Optional<Client> recovered = clientRepository.findByClientLogin("NotFound");
        assertThat(recovered).isEmpty();
    }

    @Test
    void shouldExistsByClientLoginOrIdIsTrue(){
        Client client = testEntityManager.persistAndFlush(createDefaultClient());
        testEntityManager.clear();

        boolean recovered = clientRepository.existsByClientLoginOrId(
                client.getClientLogin(),
                client.getId());

        assertThat(client).isNotNull();
        assertThat(recovered).isTrue();
    }

    @Test
    void shouldExistsByClientLoginOrIdIsFalse(){
        boolean recovered = clientRepository.existsByClientLoginOrId(
                "Test",
                UUID.randomUUID());

        assertThat(recovered).isFalse();
    }

    @Test
    void shouldPersistAndRetrieveClientCorrectly(){
        Client client = testEntityManager.persistAndFlush(createDefaultClient());
        testEntityManager.clear();

        Client foundClient = testEntityManager.find(Client.class, client.getId());

        assertThat(foundClient).isNotNull();
        assertThat(foundClient.getClientLogin()).isEqualTo("Test");
        assertThat(foundClient.getClientPassword()).isEqualTo("test123");
        assertThat(foundClient.getRedirectUri()).isEqualTo("testuri");
        assertThat(foundClient.getScope()).isEqualTo("ADMIN");
    }

    @Test
    void shouldFailWhenClientLoginIsNull(){
        Client client = createDefaultClient();
        client.setClientLogin(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(client))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenClientPasswordIsNull(){
        Client client = createDefaultClient();
        client.setClientPassword(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(client))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenRedirectUriIsNull(){
        Client client = createDefaultClient();
        client.setRedirectUri(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(client))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldFailWhenScopeIsNull(){
        Client client = createDefaultClient();
        client.setScope(null);

        assertThatThrownBy(() -> testEntityManager.persistAndFlush(client))
                .isInstanceOf(ConstraintViolationException.class);
    }

    private Client createDefaultClient(){
        return new Client(
                null,
                "Test",
                "test123",
                "testuri",
                "ADMIN"
        );
    }

    private ClientRequestDto createDefaultRequestDto(Client client){
        return new ClientRequestDto(
                client.getId(),
                client.getClientLogin(),
                client.getClientPassword(),
                client.getRedirectUri(),
                client.getScope()
        );
    }
}

