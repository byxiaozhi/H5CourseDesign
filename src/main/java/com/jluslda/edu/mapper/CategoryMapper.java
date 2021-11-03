package com.jluslda.edu.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT name FROM `category` WHERE object=#{object}")
    List<String> getCategory(String object);
}
