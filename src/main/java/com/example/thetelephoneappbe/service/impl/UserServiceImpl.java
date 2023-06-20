package com.example.thetelephoneappbe.service.impl;

import com.example.thetelephoneappbe.model.User;
import com.example.thetelephoneappbe.repository.UserRepository;
import com.example.thetelephoneappbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User creatUser(User user) {
        return userRepository.save(user);
    }
}
