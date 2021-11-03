package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Content;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContentMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({"INSERT INTO `content` (object, user_id, category, title, description, cover, content, create_time, update_time, status, extra) ",
            "VALUES (#{object}, #{userId}, #{category}, #{title}, #{description}, #{cover}, #{content}, #{createTime}, #{updateTime}, #{status}, #{extra})"})
    void addContent(Content content);

    @Select({"<script>", "SELECT * FROM `content`", "WHERE object=#{object}",
            "<if test='category!=null'>", "AND category=#{category}", "</if>",
            "<if test='tag!=null'>", "AND id IN (SELECT `object_id` FROM `tag` WHERE `object` = 'community' and `name` = #{tag})", "</if>",
            "<if test='search!=null and search!=\"\" and searchContent'>", "AND match(title, description, content) against(#{search})", "</if>",
            "<if test='search!=null and search!=\"\" and !searchContent'>", "AND match(title, description) against(#{search})", "</if>",
            "<if test='userId!=null'>", "AND user_id = #{userId}", "</if>",
            "<if test='status!=null'>", "AND status = #{status}", "</if>",
            "ORDER BY update_time DESC LIMIT #{beg},#{num}",
            "</script>"})
    List<Content> getContentList(String object, String category, String tag, String search, Boolean searchContent, Integer userId, Integer beg, Integer num, String status);

    @Select("SELECT * FROM `content` WHERE id=#{id}")
    Content getContentById(Integer id);

    @Select({"<script>", "SELECT count(*) FROM `content`", "WHERE object=#{object}",
            "<if test='category!=null'>", "AND category=#{category}", "</if>",
            "<if test='tag!=null'>", "AND id IN (SELECT `object_id` FROM `tag` WHERE `object` = 'community' and `name` = #{tag})", "</if>",
            "<if test='search!=null and search!=\"\" and searchContent'>", "AND match(title, description, content) against(#{search})", "</if>",
            "<if test='search!=null and search!=\"\" and !searchContent'>", "AND match(title, description) against(#{search})", "</if>",
            "<if test='userId!=null'>", "AND user_id = #{userId}", "</if>",
            "<if test='status!=null'>", "AND status = #{status}", "</if>",
            "</script>"})
    Integer count(String object, String category, String tag, String search, Boolean searchContent, Integer userId, String status);

    @Update("UPDATE `content` SET status=#{status} WHERE id=#{id}")
    void setContentStatus(Integer id, String status);
}
