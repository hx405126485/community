package com.hx199513.community.community.service;

import com.hx199513.community.community.mapper.UserMapper;
import com.hx199513.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //因为用户信息Accountid是不变的，所以通过AccountId查询User表中是否存在用户信息
        //dbUser用户信息
        User dbUser=userMapper.findByAccountId(user.getAccountId());
        if (dbUser==null){
            //用户信息为空，则插入用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //用户存在则更新用户信息
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
