package ru.app.instagramstoriesmetric.service;

import ru.app.instagramstoriesmetric.dto.StoryCheckRequest;
import ru.app.instagramstoriesmetric.dto.StoryCheckResponse;

public interface StoryCheckService {

    StoryCheckResponse fetchStoryData(StoryCheckRequest request);


}
