package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert({"INSERT INTO `comment` (user_id, father_id, object, object_id, content, create_time, status, extra) ",
            "VALUES (#{userId}, #{fatherId}, #{object}, #{objectId}, #{content}, #{createTime}, #{status}, #{extra})"})
    void addComment(Comment comment);

    @Select({"<script>", "SELECT * FROM `comment` WHERE 1 = 1",
            "<if test='object!=null'>", "AND object=#{object}", "</if>",
            "<if test='objectId!=null'>", "AND object_id=#{objectId}", "</if>",
            "<if test='userId!=null'>", "AND user_id = #{userId}", "</if>",
            "ORDER BY create_time DESC LIMIT #{beg}, #{num}",
            "</script>"})
    List<Comment> getCommentList(String object, Integer objectId, Integer userId, Integer beg, Integer num);

    @Select({"<script>", "SELECT COUNT(*) FROM `comment` WHERE 1 = 1",
            "<if test='object!=null'>", "AND object=#{object}", "</if>",
            "<if test='objectId!=null'>", "AND object_id=#{objectId}", "</if>",
            "<if test='userId!=null'>", "AND user_id = #{userId}", "</if>",
            "</script>"})
    Integer count(String object, Integer objectId, Integer userId);
}
