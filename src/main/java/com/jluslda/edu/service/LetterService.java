package com.jluslda.edu.service;

import com.jluslda.edu.mapper.LetterMapper;
import com.jluslda.edu.model.Letter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LetterService {

    LetterMapper letterMapper;

    public LetterService(LetterMapper letterMapper) {
        this.letterMapper = letterMapper;
    }

    public List<Letter> getLetterList(Integer userId, Integer startId) {
        return letterMapper.getLetterList(userId, startId);
    }

    public void sendLetter(Integer fromUserId, Integer toUserId, String content) {
        letterMapper.addLetter(new Letter(){{
            setUserId(fromUserId);
            setFromUserId(fromUserId);
            setToUserId(toUserId);
            setContent(content);
        }});
        letterMapper.addLetter(new Letter(){{
            setUserId(toUserId);
            setFromUserId(fromUserId);
            setToUserId(toUserId);
            setContent(content);
        }});
    }

}
