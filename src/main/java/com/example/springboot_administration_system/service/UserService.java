package com.example.springboot_administration_system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot_administration_system.entity.User;
import com.example.springboot_administration_system.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public boolean saveUser(User user) {
        try {
            return saveOrUpdate(user);
        } catch (Exception e) {
            return false;
        }
    }

}
