package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {

    @Select("SELECT name FROM `tag` WHERE `object` = #{object} GROUP BY `name` ORDER BY COUNT(id) DESC LIMIT #{num}")
    List<String> getTag(String object, Integer num);

    @Insert("INSERT INTO `tag` (object, object_id, name) VALUES (#{object}, #{objectId}, #{name})")
    void addTag(Tag tag);
}
