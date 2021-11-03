package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Letter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LetterMapper {

    @Select({"<script>", "SELECT * FROM `letter`", "WHERE user_id=#{userId}",
            "<if test='startId!=null'>", "and id>#{startId}", "</if>",
            "ORDER BY id DESC", "</script>"})
    List<Letter> getLetterList(Integer userId, Integer startId);

    @Insert({"INSERT INTO `letter` (user_id, from_user_id, to_user_id, content, datetime, status)",
            "VALUES (#{userId}, #{fromUserId}, #{toUserId}, #{content}, #{datetime}, #{status})"})
    void addLetter(Letter letter);
}
