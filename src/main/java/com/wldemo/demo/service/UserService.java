package com.wldemo.demo.service;

import com.wldemo.demo.mapper.UserMapper;
import com.wldemo.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        User dbUser = userMapper.findByAccountId(user.getAccountId());//先去数据库里找用户存在不
        if(dbUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //这些全是可以更新的值
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setToken(user.getToken());//更新token
            userMapper.update(dbUser);
        }

    }
}
