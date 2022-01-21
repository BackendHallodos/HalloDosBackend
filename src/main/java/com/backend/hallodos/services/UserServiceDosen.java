package com.backend.hallodos.services;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import com.backend.hallodos.dto.ResponseDto;
import com.backend.hallodos.dto.SignInDto;
import com.backend.hallodos.dto.SignInResponseDto;
import com.backend.hallodos.dto.SignupDto;
import com.backend.hallodos.exceptions.AuthFailException;
import com.backend.hallodos.exceptions.CustomExceptoon;
import com.backend.hallodos.model.AuthTokenDos;
import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.DosenRepository;
import com.backend.hallodos.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service

public class UserServiceDosen {
    
    @Autowired
    private MahasiswaRepository mahaRepo;
    @Autowired
    private DosenRepository dosenRepo;

    @Autowired
    AuthService authService;


    //RegisterServiceMDosen
    @Transactional
    public ResponseDto signUp(SignupDto signupDto,Dosen dosen) {
        
        // check if user is already
       Mahasiswa maha = mahaRepo.findByEmail_mahasiswa(dosen.getEmail_dosen());
        if (Objects.nonNull(dosenRepo.findByEmail_dosen(signupDto.getEmail())) || dosen != null) {
            throw new CustomExceptoon("User Already Present");
        }

        // hash the password
        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Dosen dosenuser = new Dosen (
            dosen.getId(),
            dosen.getUsername(),
            dosen.getEmail_dosen(),
            null,
            dosen.getSecurity_question(),
            dosen.getSecurity_answer(),
            encryptedpassword,
            dosen.getGraduateFrom(),
            dosen.getMajor(),
            dosen.getAffiliate(),
            null, null, null, null,0,0,0, null,0,null,null,dosen.getTopicId());
        dosenRepo.save(dosenuser);

        // create token
        final AuthTokenDos authToken = new AuthTokenDos(dosenuser);
        authService.saveConfirmationTokenDos(authToken);

        ResponseDto responseDto = new ResponseDto("success", "CREATED SUCCESS!");
        return responseDto;
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }
    public SignInResponseDto signIn(SignInDto signInDto) {
        //find user by email
       Dosen user = dosenRepo.findByEmail_dosen(signInDto.getEmail());

        if (Objects.isNull(user)) {
            throw new AuthFailException("User Is Not Valid!");
        }
        //hash the pass
        try {
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
                throw new AuthFailException("wrong password!");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //compare pass in DB

        //if pass match
        AuthTokenDos token = authService.getTokenDos(user);

        //retrive token
        if (Objects.isNull(token)) {
            throw new CustomExceptoon("token is not present!");
        }
        return new SignInResponseDto("success", token.getToken());
    }
}
