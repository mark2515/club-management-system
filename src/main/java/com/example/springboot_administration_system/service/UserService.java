package com.example.springboot_administration_system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot_administration_system.entity.User;
import com.example.springboot_administration_system.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public boolean saveUser(User user) {
        return saveOrUpdate(user);
    }

}
