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
import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private MahasiswaRepository repoMaha;

    @Autowired
    AuthService authService;

    // @Autowired private BCryptPasswordEncoder encoder;
    // @Autowired private MahasiswaRepository mahaRepo;

    // public void save(Mahasiswa maha){

    //     maha.setPassword(encoder.encode(maha.getPassword()));
    //     mahaRepo.save(maha);
    // }

    //RegisterService
    @Transactional
    public ResponseDto signUp(SignupDto signupDto,Mahasiswa maha) {
        
        // check if user is already
        if (Objects.nonNull(repoMaha.findByEmail_mahasiswa(signupDto.getEmail()))) {
            throw new CustomExceptoon("User Already Present");
        }

        // hash the password
        String encryptedpassword = signupDto.getPassword();

        try {
            encryptedpassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Mahasiswa user = new Mahasiswa (
            signupDto.getUsername(),
            encryptedpassword,null,
            maha.getSecurity_question(),maha.getSecurity_answer(),null,null,
            signupDto.getEmail(), 
            null,null,null,null,null);

        repoMaha.save(user);

        // create token
        final AuthToken authToken = new AuthToken(user);
        authService.saveConfirmationToken(authToken);

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
       Mahasiswa user = repoMaha.findByEmail_mahasiswa(signInDto.getEmail());

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
        AuthToken token = authService.getToken(user);

        //retrive token
        if (Objects.isNull(token)) {
            throw new CustomExceptoon("token is not present!");
        }
        return new SignInResponseDto("success", token.getToken());
    }
}
