package ru.app.instagramstoriesmetric.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.app.instagramstoriesmetric.entity.StoryEntity;

@Repository
public interface StoryRepository extends CrudRepository<StoryEntity, String> {

    StoryEntity findByUrl(String url);

    Boolean existsByUrl(String url);

}
