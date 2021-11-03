package com.jluslda.edu.service;

import com.jluslda.edu.mapper.FollowMapper;
import com.jluslda.edu.model.Follow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    FollowMapper followMapper;

    public FollowService(FollowMapper followMapper) {
        this.followMapper = followMapper;

    }

    public void follow(Integer fromUserId, Integer toUserId) {
        followMapper.follow(fromUserId, toUserId);
    }

    public void unFollow(Integer fromUserId, Integer toUserId) {
        followMapper.unFollow(fromUserId, toUserId);
    }

    public List<Follow> getMyFollow(Integer userId, Integer beg, Integer num) {
        return followMapper.getMyFollow(userId, beg, num);
    }

    public List<Follow> getFollowMe(Integer userId, Integer beg, Integer num) {
        return followMapper.getFollowMe(userId, beg, num);
    }

    public boolean isFollow(Integer fromUserId, Integer toUserId) {
        return followMapper.isFollow(fromUserId,toUserId);
    }
}
