package com.jluslda.edu.service;

import com.jluslda.edu.mapper.UserMapper;
import com.jluslda.edu.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    public void addUser(User user) {
        userMapper.addUser(user);
    }

    public void updateTime(Integer id) {
        userMapper.updateTime(id);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }
}
