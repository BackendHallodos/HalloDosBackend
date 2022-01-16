package com.backend.hallodos.services;


import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    com.backend.hallodos.repository.TokenRepository tokenRepository;

    public void saveConfirmationToken(AuthToken authToken) {
        tokenRepository.save(authToken);
    }

    public AuthToken getToken(Mahasiswa user) {
        return tokenRepository.findByUser(user);
    }
    
}
