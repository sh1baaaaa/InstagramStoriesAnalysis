package ru.app.instagramstoriesmetric.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash(value = "stories", timeToLive = 3600L)
@Data
public class StoryEntity implements Serializable {

    @Id
    private String id;

    private String url;

    private String expiresIn;

    private Boolean isActive;

    private List<String> mentions;

    private List<String> locations;

    private List<String> hashtags;

    private List<String> links;

    private String expiresAt;

    private String createdAt;

}
