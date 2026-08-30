package com.unnamed.matchmaking.cs16_matchmaking.UserAuth.service;

import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.dto.UserAuthRequestDTO;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.entity.UserAuth;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.mapper.UserAuthMapper;
import com.unnamed.matchmaking.cs16_matchmaking.UserAuth.repository.UserAuthRepository;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.DuplicateException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.LoginNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.ResourceNotFoundException;
import com.unnamed.matchmaking.cs16_matchmaking.exceptions.UserAuthNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    //private final PasswordEnconder passwordEnconder;

    @Transactional
    public UserAuth registerUserAuth(UserAuthRequestDTO userAuthRequestDTO) {

        if(userAuthRequestDTO == null) throw new ResourceNotFoundException("Dto está vazio.");

        var userAuth = UserAuthMapper.toEntity(userAuthRequestDTO);

        if(userAuth.getId() == null)
            throw new UserAuthNotFoundException("Usuário não encontrado");


        var password = userAuth.getPassword();
        //userAuth.setPassword(passwordEncoder.encode(password));

        if(userAuthRepository.existsByLogin(userAuth.getLogin()))
            throw new DuplicateException("Usuário já cadastrado");

        return userAuthRepository.save(userAuth);
    }

    public List<UserAuth> findAll() {
        return userAuthRepository.findAll();
    }

    @Transactional
    public Optional<UserAuth> updateUserAuth(UUID id, UserAuthRequestDTO userAuthRequestDTO) {
        if(userAuthRequestDTO == null) throw new ResourceNotFoundException("Dto está vazio.");

        UserAuth userAuth = findById(id)
                .orElseThrow(() -> new LoginNotFoundException("Usuário não encontrado."));

        userAuth.setLogin(userAuthRequestDTO.login());
        userAuth.setPassword(userAuthRequestDTO.password());
        userAuth.setRoles(userAuthRequestDTO.roles());

        return Optional.of(userAuthRepository.save(userAuth));
    }

    @Transactional
    public void cancelUserAuth(String login) {
        UserAuth userAuthValidate = userAuthRepository.findByLogin(login)
                .orElseThrow(() -> new LoginNotFoundException("Usuário não encontrado."));

        userAuthRepository.deleteByLogin(userAuthValidate.getLogin());
    }

    public Optional<UserAuth> findById(UUID id) {
        return userAuthRepository.findById(id);
    }

    public Optional<UserAuth> findByLogin(String login) {
        return userAuthRepository.findByLogin(login);
    }
}
