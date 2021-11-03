package com.jluslda.edu.service;

import com.jluslda.edu.mapper.FavoriteMapper;
import com.jluslda.edu.model.Favorite;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    public void setFavorite(Integer userId, String object, Integer objectId){
        favoriteMapper.setFavorite(userId,object,objectId);
    }

    public void unsetFavorite(Integer userId, String object, Integer objectId){
        favoriteMapper.unsetFavorite(userId,object,objectId);
    }

    public List<Favorite> getFavorites(Integer userId, Integer beg, Integer num){
        return favoriteMapper.getFavorites(userId, beg, num);
    }

    public boolean isFavorite(Integer userId, String object, Integer objectId){
        return favoriteMapper.isFavorite(userId,object,objectId);
    }

}
