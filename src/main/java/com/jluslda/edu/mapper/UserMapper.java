package com.jluslda.edu.mapper;

import com.jluslda.edu.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM `user` WHERE email=#{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM `user` WHERE id=#{id}")
    User getUserById(Integer id);

    @Update("UPDATE `user` SET update_time=NOW()")
    void updateTime(Integer id);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({"INSERT INTO `user` (nickname, email, password, create_time, update_time, signature, status, extra)",
            "VALUES (#{nickname}, #{email}, #{password}, #{createTime}, #{updateTime}, #{signature}, #{status}, #{extra})"})
    void addUser(User user);

    @Insert("UPDATE `user` SET nickname=#{nickname}, email=#{email}, password=#{password}, create_time=#{createTime}, update_time=#{updateTime}, signature=#{signature}, status=#{status}, extra=#{extra} WHERE id=#{id}")
    void updateUser(User user);
}
