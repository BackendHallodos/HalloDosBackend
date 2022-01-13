package Hallodos.services;

import Hallodos.model.entities.User;
import Hallodos.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

}
