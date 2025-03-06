package ru.app.instagramstoriesmetric.dto.hiker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class HikerApiResponse {

    @JsonProperty("pk")
    private String id;

    private String url;

    @JsonProperty("taken_at")
    private String takenAt;

    private List<HikerApiResponseMentions> mentions;

    private List<HikerApiResponseLinks> links;

    private List<HikerApiResponseHashtag> hashtags;

    private List<HikerApiResponseLocations> locations;
}
