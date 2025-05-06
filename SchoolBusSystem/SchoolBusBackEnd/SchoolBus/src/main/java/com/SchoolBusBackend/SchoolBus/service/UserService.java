package com.SchoolBusBackend.SchoolBus.service;

import com.SchoolBusBackend.SchoolBus.Repository.UserRepository;
import com.SchoolBusBackend.SchoolBus.dto.AuthResponse;
import com.SchoolBusBackend.SchoolBus.dto.RegisterRequest;
import com.SchoolBusBackend.SchoolBus.dto.UserDto;
import com.SchoolBusBackend.SchoolBus.model.UserEntity;
import com.SchoolBusBackend.SchoolBus.security.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FirebaseAuthService firebaseAuthService;

    @Autowired
    public UserService(UserRepository userRepository, FirebaseAuthService firebaseAuthService) {
        this.userRepository = userRepository;
        this.firebaseAuthService = firebaseAuthService;
    }

    /**
     * Registra um novo usuário no sistema
     *
     * @param request Dados do registro
     * @return Resposta contendo o resultado do registro
     * @throws FirebaseAuthException Se ocorrer erro na criação do usuário no Firebase
     */
    @Transactional
    public AuthResponse registerUser(RegisterRequest request) throws FirebaseAuthException {
        // Verifica se o email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        // Cria o usuário no Firebase Authentication
        UserRecord userRecord = firebaseAuthService.createUser(request.getEmail(), request.getPassword());

        // Atualiza o nome de exibição
        if (request.getDisplayName() != null && !request.getDisplayName().isEmpty()) {
            userRecord = firebaseAuthService.updateUserProfile(userRecord.getUid(), request.getDisplayName());
        }

        // Salva o usuário no banco de dados
        UserEntity userEntity = new UserEntity(
                userRecord.getUid(),
                userRecord.getEmail(),
                userRecord.getDisplayName(),
                "USER" // Papel padrão para novos usuários
        );

        userRepository.save(userEntity);

        // Cria a resposta
        UserDto userDto = new UserDto(
                userEntity.getUid(),
                userEntity.getEmail(),
                userEntity.getDisplayName(),
                userEntity.getRole()
        );

        return new AuthResponse(true, "Usuário registrado com sucesso", userDto);
    }

    /**
     * Verifica um token de usuário
     *
     * @param token Token do Firebase
     * @return Resposta contendo o resultado da verificação
     * @throws FirebaseAuthException Se o token for inválido
     */
    public AuthResponse verifyUserToken(String token) throws FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(token);
        String uid = decodedToken.getUid();

        // Busca o usuário no banco de dados
        Optional<UserEntity> userOpt = userRepository.findById(uid);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            // Atualiza o último login
            user.setLastLogin(new Date());
            userRepository.save(user);

            UserDto userDto = new UserDto(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole()
            );

            return new AuthResponse(true, "Token válido", userDto);
        } else {
            // Se o usuário existe no Firebase mas não no banco, cria o registro no banco
            try {
                UserRecord userRecord = firebaseAuthService.getUserDetails(uid);

                UserEntity newUser = new UserEntity(
                        userRecord.getUid(),
                        userRecord.getEmail(),
                        userRecord.getDisplayName(),
                        "USER"
                );

                newUser.setLastLogin(new Date());
                userRepository.save(newUser);

                UserDto userDto = new UserDto(
                        newUser.getUid(),
                        newUser.getEmail(),
                        newUser.getDisplayName(),
                        newUser.getRole()
                );

                return new AuthResponse(true, "Token válido", userDto);
            } catch (FirebaseAuthException e) {
                throw new FirebaseAuthException("Usuário não encontrado", e.getCause());
            }
        }
    }

    /**
     * Obtém o perfil do usuário a partir do token
     *
     * @param token Token do Firebase
     * @return Dados do usuário
     * @throws FirebaseAuthException Se o token for inválido
     */
    public UserDto getUserProfile(String token) throws FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuthService.verifyToken(token);
        String uid = decodedToken.getUid();

        Optional<UserEntity> userOpt = userRepository.findById(uid);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            return new UserDto(
                    user.getUid(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRole()
            );
        } else {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }
}
