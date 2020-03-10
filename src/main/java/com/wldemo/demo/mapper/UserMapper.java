package com.wldemo.demo.mapper;

import com.wldemo.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);//如果是类的话可以#{}自动放，不是类加个@Param("token")

    @Insert("insert into user (account_id, name, token, gmt_create, gmt_modified, avatar_url) values (#{accountId},#{name},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where id=#{id}")
    User findById(@Param("id") Integer id);
}
