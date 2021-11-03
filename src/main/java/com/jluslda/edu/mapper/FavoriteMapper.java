package com.jluslda.edu.mapper;

import com.jluslda.edu.model.Favorite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavoriteMapper {

    @Insert("INSERT INTO `favorite` (user_id, object, object_id) VALUES (#{userId}, #{object}, #{objectId})")
    void setFavorite(Integer userId, String object, Integer objectId);

    @Delete("DELETE FROM `favorite` WHERE user_id=#{userId} AND object=#{object} AND object_id=#{objectId}")
    void unsetFavorite(Integer userId, String object, Integer objectId);

    @Select("SELECT * FROM `favorite` WHERE user_id=#{userId} LIMIT #{beg},#{num}")
    List<Favorite> getFavorites(Integer userId, Integer beg, Integer num);

    @Select("SELECT COUNT(*)>0 FROM `favorite` WHERE user_id=#{userId} AND object=#{object} AND object_id=#{objectId}")
    boolean isFavorite(Integer userId, String object, Integer objectId);
}
