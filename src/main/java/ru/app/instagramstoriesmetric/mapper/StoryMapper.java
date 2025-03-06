package ru.app.instagramstoriesmetric.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.app.instagramstoriesmetric.dto.StoryCheckResponse;
import ru.app.instagramstoriesmetric.entity.StoryEntity;

@Mapper(componentModel = "spring")
public interface StoryMapper {

    @Mapping(target = "createdAt", source = "publicationDate")
    StoryEntity toStoryEntity(StoryCheckResponse data);

}
