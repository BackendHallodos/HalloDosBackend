package com.backend.hallodos.services;

import java.util.List;

import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.repository.DosenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private DosenRepository dosenRepo;

    public List<Dosen> listDosenAll(String keyword) {
        if (keyword != null) {
            return dosenRepo.findAllDosenByWord(keyword);
        } else {
            return dosenRepo.findAll();
        }
    }
}
