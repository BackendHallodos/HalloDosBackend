package com.backend.hallodos.services;


import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.AuthTokenDos;
import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.TokenDosenRepository;
import com.backend.hallodos.repository.TokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    TokenDosenRepository tokenDosRepo;

    public void saveConfirmationToken(AuthToken authToken) {
        tokenRepository.save(authToken);
    }

    public AuthToken getToken(Mahasiswa user) {
        return tokenRepository.findByUser(user);
    }
    public AuthTokenDos getTokenDos(Dosen dosen) {
        return tokenDosRepo.findByDosen(dosen);
    }

    public void saveConfirmationTokenDos(AuthTokenDos authTokenDos) {
        tokenDosRepo.save(authTokenDos);
    }
    
}
