package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Follow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FollowMapper {


    @Insert("INSERT INTO `follow` (from_user_id, to_user_id) VALUES (#{fromUserId}, #{toUserId})")
    void follow(Integer fromUserId, Integer toUserId);

    @Delete("DELETE FROM `follow` WHERE from_user_id=#{fromUserId} AND to_user_id=#{toUserId}")
    void unFollow(Integer fromUserId, Integer toUserId);

    @Select("SELECT * FROM `follow` WHERE from_user_id=#{userId} LIMIT #{beg},#{num}")
    List<Follow> getMyFollow(Integer userId, Integer beg, Integer num);

    @Select("SELECT * FROM `follow` WHERE to_user_id=#{userId} LIMIT #{beg},#{num}")
    List<Follow> getFollowMe(Integer userId, Integer beg, Integer num);

    @Select("SELECT COUNT(*)>0 FROM `follow` WHERE from_user_id=#{fromUserId} AND to_user_id=#{toUserId}")
    boolean isFollow(Integer fromUserId, Integer toUserId);
}
