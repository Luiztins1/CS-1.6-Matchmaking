package com.unnamed.matchmaking.cs16_matchmaking.service;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.repository.UserAuthRepository;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.service.UserAuthService;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LoginNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.UserAuthNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserAuthServiceTest {

    @InjectMocks
    UserAuthService userAuthService;

    @Mock
    UserAuthRepository userAuthRepository;

    @Captor
    private ArgumentCaptor<UserAuth> authArgumentCaptor;

    UserAuthRequestDTO userAuthRequestDTO;
    UserAuth userAuth;

    @BeforeEach()
    void setUp(){
        userAuth = createDefaultUserAuth();
        userAuthRequestDTO = createDefaultUserAuthRequestDto(userAuth);
    }

    @Test
    void shouldRegisterUserAuth(){
        when(userAuthRepository.save(Mockito.any(UserAuth.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserAuth user = userAuthService.registerUserAuth(userAuthRequestDTO);

        verify(userAuthRepository, times(1))
                .save(authArgumentCaptor.capture());

        UserAuth userCaptor = authArgumentCaptor.getValue();

        assertThat(userCaptor).isNotNull();
        assertThat(userCaptor.getLogin()).isNotBlank();
        assertThat(userCaptor.getPassword()).isNotBlank();
        assertThat(userCaptor.getRoles()).isNotEmpty();

    }

    @Test
    void shouldReturnWhenDtoResourceNotFoundException(){
        assertThrows(ResourceNotFoundException.class, () -> {
           userAuthService.registerUserAuth(null);
        }, "Dto está vazio.");
    }

    @Test
    void shouldReturnWhenDtoUserAuthNotFoundException(){

        UserAuthRequestDTO request = new UserAuthRequestDTO(
                null,
                "Luiz",
                "luiz123",
                List.of("USER")
        );

        assertThrows(UserAuthNotFoundException.class, () -> {
            userAuthService.registerUserAuth(request);
        }, "Dto está vazio.");
    }

    @Test
    void shouldReturnWhenDtoDuplicateException(){
        UserAuthRequestDTO requestDTO = createDefaultUserAuthRequestDto(userAuth);

        when(userAuthRepository.existsByLogin(requestDTO.login()))
                .thenReturn(true);

        assertThrows(DuplicateException.class, () -> {
            userAuthService.registerUserAuth(requestDTO);
        }, "Dto está vazio.");

        verify(userAuthRepository, times(1))
                .existsByLogin(requestDTO.login());
    }

    @Test
    void shouldFindAll(){
        when(userAuthRepository.findAll())
                .thenReturn(List.of(userAuth));

        List<UserAuth> list = userAuthService.findAll();

        assertThat(list)
                .isNotEmpty()
                .doesNotContainNull();

        assertThat(list).allSatisfy(userAuth -> assertThat(userAuth).isNotNull());

        verify(userAuthRepository, times(1))
                .findAll();
    }

    @Test
    void shouldReturnListEmpty(){
        when(userAuthRepository.findAll())
                .thenReturn(List.of());

        List<UserAuth> list = userAuthService.findAll();

        assertThat(list).isEmpty();

        verify(userAuthRepository, times(1))
                .findAll();
    }

    @Test
    void shouldUpdateUserAuth(){
        when(userAuthRepository.findById(Mockito.eq(userAuth.getId())))
                .thenReturn(Optional.of(userAuth));

        when(userAuthRepository.save(Mockito.any(UserAuth.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserAuthRequestDTO request = new UserAuthRequestDTO(
                userAuth.getId(),
                "Luiz",
                "Luiz",
                List.of("MANAGER")
        );

        Optional<UserAuth> update = userAuthService.updateUserAuth(userAuth.getId(), request);

        verify(userAuthRepository, times(1))
                .save(authArgumentCaptor.capture());

        UserAuth userCaptor = authArgumentCaptor.getValue();

        assertThat(userCaptor).isNotNull();

        assertThat(userCaptor.getId()).isEqualTo(update.get().getId());
        assertThat(userCaptor.getLogin()).isEqualTo(update.get().getLogin());
        assertThat(userCaptor.getPassword()).isEqualTo(update.get().getPassword());

       verify(userAuthRepository, times(1))
                .findById(userAuth.getId());
    }

    @Test
    void shouldReturnWhenResourceNotFoundException(){
        assertThrows(ResourceNotFoundException.class, () ->{
            userAuthService.updateUserAuth(userAuth.getId(), null);
        }, "Dto está vazio.");
    }

    @Test
    void shouldReturnWhenLoginNotFoundException(){
        assertThrows(LoginNotFoundException.class, () ->{
            userAuthService.updateUserAuth(userAuth.getId(), userAuthRequestDTO);
        }, "Usuário não encontrado.");
    }

    @Test
    void shouldCancelUserAuth(){
        when(userAuthRepository.findByLogin(Mockito.eq(userAuth.getLogin())))
                .thenReturn(Optional.of(userAuth));

        userAuthService.cancelUserAuth(userAuth.getLogin());

        verify(userAuthRepository, times(1))
                .findByLogin(userAuth.getLogin());

        verify(userAuthRepository, times(1))
                .deleteByLogin(userAuth.getLogin());
    }

    @Test
    void shouldReturnLoginNotFoundException(){
        assertThrows(LoginNotFoundException.class, () -> {
            userAuthService.cancelUserAuth(userAuth.getLogin());
        }, "Usuário não encontrado.");
    }

    @Test
    void shouldFindById(){
        when(userAuthRepository.findById(Mockito.eq(userAuth.getId())))
                .thenReturn(Optional.of(userAuth));

        UserAuth user = userAuthService.findById(userAuth.getId())
                .orElseThrow(() -> new UserAuthNotFoundException("Usuário não encontrado."));

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userAuth.getId());
        assertThat(user.getLogin()).isEqualTo(userAuth.getLogin());

        verify(userAuthRepository, times(1))
                .findById(userAuth.getId());
    }

    @Test
    void shouldReturnUserAuthNotFoundException(){
        assertThrows(UserAuthNotFoundException.class, () -> {
            userAuthService.findById(userAuth.getId())
                    .orElseThrow(() -> new UserAuthNotFoundException("Usuário não encontrado."));
        }, "Usuário não encontrado.");
    }

    @Test
    void shouldFindByLogin(){
        when(userAuthRepository.findByLogin(Mockito.eq(userAuth.getLogin())))
                .thenReturn(Optional.of(userAuth));

        UserAuth user = userAuthService.findByLogin(userAuth.getLogin())
                .orElseThrow(() -> new LoginNotFoundException("Usuário não encontrado."));

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userAuth.getId());
        assertThat(user.getLogin()).isEqualTo(userAuth.getLogin());
    }

    @Test
    void shouldReturnLogiNotFoundException(){
        assertThrows(LoginNotFoundException.class, () ->{
           userAuthService.findByLogin(userAuth.getLogin())
                   .orElseThrow(() -> new LoginNotFoundException("Usuário não encontrado."));
        }, "Usuário não encontrado.");;
    }

    private UserAuth createDefaultUserAuth(){
        return new UserAuth(
                UUID.randomUUID(),
                "admin",
                "admin123",
                List.of("ADMIN")
        );
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
}
