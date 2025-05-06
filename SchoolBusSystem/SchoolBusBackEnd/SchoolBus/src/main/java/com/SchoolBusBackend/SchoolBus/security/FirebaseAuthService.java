package com.SchoolBusBackend.SchoolBus.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {

    /**
     * Verifica um token ID do Firebase
     *
     * @param idToken Token a ser verificado
     * @return O token decodificado
     * @throws FirebaseAuthException Se o token for inválido
     */
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    /**
     * Obtém detalhes de um usuário pelo UID
     *
     * @param uid UID do usuário
     * @return Informações do usuário
     * @throws FirebaseAuthException Se o usuário não for encontrado
     */
    public UserRecord getUserDetails(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    /**
     * Cria um usuário no Firebase Authentication
     *
     * @param email    Email do usuário
     * @param password Senha do usuário
     * @return O registro do usuário criado
     * @throws FirebaseAuthException Se houver erro na criação
     */
    public UserRecord createUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setEmailVerified(false);

        return FirebaseAuth.getInstance().createUser(request);
    }

    /**
     * Atualiza propriedades de um usuário existente
     *
     * @param uid         UID do usuário
     * @param displayName Nome de exibição
     * @return O registro atualizado do usuário
     * @throws FirebaseAuthException Se houver erro na atualização
     */
    public UserRecord updateUserProfile(String uid, String displayName) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setDisplayName(displayName);

        return FirebaseAuth.getInstance().updateUser(request);
    }

}
