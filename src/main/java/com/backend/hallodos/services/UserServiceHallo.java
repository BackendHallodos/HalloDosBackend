package com.backend.hallodos.services;

import com.backend.hallodos.model.entities.Mahasiswa;
import com.backend.hallodos.model.entities.UserDetailsHallo;
import com.backend.hallodos.model.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceHallo implements UserDetailsService {

    @Autowired
    private MahasiswaRepository repoMaha;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mahasiswa mahasiswa =repoMaha.findById(username).get();
        if(mahasiswa == null){
        throw new UsernameNotFoundException("User Name Belum Terdaftar");
        }
        return new UserDetailsHallo(mahasiswa);
    }
}
