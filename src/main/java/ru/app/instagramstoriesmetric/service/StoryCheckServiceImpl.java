package ru.app.instagramstoriesmetric.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import ru.app.instagramstoriesmetric.dto.hiker.HikerApiResponse;
import ru.app.instagramstoriesmetric.dto.StoryCheckRequest;
import ru.app.instagramstoriesmetric.dto.StoryCheckResponse;
import ru.app.instagramstoriesmetric.dto.hiker.HikerApiResponseLinks;
import ru.app.instagramstoriesmetric.entity.StoryEntity;
import ru.app.instagramstoriesmetric.exception.HikerAPIAuthenticationException;
import ru.app.instagramstoriesmetric.exception.BadRequestException;
import ru.app.instagramstoriesmetric.exception.StoryNotFoundException;
import ru.app.instagramstoriesmetric.mapper.StoryMapper;
import ru.app.instagramstoriesmetric.repository.StoryRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class StoryCheckServiceImpl implements StoryCheckService {

    @Value("${hiker.token}")
    private String  hikerApiToken;

    @Value("${hiker.api}")
    private String  hikerApiUrl;

    private final StoryMapper storyMapper;

    private final WebClient webClient;

    private final StoryRepository storyRepository;

    @Autowired
    public StoryCheckServiceImpl(StoryMapper storyMapper, WebClient webClient, StoryRepository storyRepository) {
        this.storyMapper = storyMapper;
        this.webClient = webClient;
        this.storyRepository = storyRepository;
    }

    @Override
    public StoryCheckResponse fetchStoryData(StoryCheckRequest request) {

        if ((request.getUrl() != null
                && StreamSupport.stream(storyRepository.findAll().spliterator(), false)
                .anyMatch(story -> story.getUrl() != null && story.getUrl().equals(request.getUrl())))
                || (request.getId() != null
                && StreamSupport.stream(storyRepository.findAll().spliterator(), false)
                .anyMatch(story -> story.getId().equals(request.getId())))) {
            log.info("Attempt to check story from database");
            return request.getId() != null
                    ? convertToResponse(StreamSupport.stream(storyRepository.findAll().spliterator(), false)
                    .filter(story-> story.getId().equals(request.getId())).findFirst()
                    .orElseThrow(StoryNotFoundException::new))
                    : convertToResponse(StreamSupport.stream(storyRepository.findAll().spliterator(), false)
                    .filter(story-> story.getUrl() != null && story.getUrl().equals(request.getUrl())).findFirst()
                    .orElseThrow(StoryNotFoundException::new));
        }

        UriComponentsBuilder builder = request.getId() != null
                ?  UriComponentsBuilder.fromHttpUrl(hikerApiUrl + "/story/by/id")
                .queryParam("id", request.getId())
                :  UriComponentsBuilder
                .fromHttpUrl(hikerApiUrl + "/story/by/url")
                .queryParam("url", request.getUrl());

        log.info("Attempt to check story by {}", request.getId() != null ? "id " + request.getId() : "url");
        return webClient.get()
                .uri(builder.toUriString())
                .header("x-access-key", hikerApiToken)
                .retrieve()
                .onStatus(code -> code == HttpStatus.NOT_FOUND,
                        response -> Mono.just(new StoryNotFoundException()))
                .onStatus(code -> code == HttpStatus.UNAUTHORIZED,
                        response -> Mono.just(new HikerAPIAuthenticationException()))
                .onStatus(code -> code == HttpStatus.BAD_REQUEST,
                        response -> Mono.just(new BadRequestException()))
                .bodyToMono(HikerApiResponse.class)
                .map(response -> convertToResponse(response, request.getUrl()))
                .block(Duration.ofSeconds(10));

    }

    private StoryCheckResponse convertToResponse(StoryEntity entity) {
        StoryCheckResponse response = new StoryCheckResponse();

        if (LocalDateTime.now().isAfter(LocalDateTime.parse(entity.getExpiresAt()))) {
            storyRepository.deleteById(entity.getId());
            throw new StoryNotFoundException();
        }

        response.setId(entity.getId());
        response.setPublicationDate(entity.getCreatedAt());
        response.setExists(entity.getIsActive());
        response.setMentions(entity.getMentions());
        response.setLinks(entity.getLinks());
        response.setHashtags(entity.getHashtags());
        response.setLocations(entity.getLocations());
        response.setExpiresIn(Duration.between(LocalDateTime.now(ZoneOffset.UTC).toLocalTime()
                        , LocalDateTime.parse(entity.getExpiresAt())).plusHours(24)
                .toHours() + " hours");

        storyRepository.deleteById(entity.getId());
        entity.setExpiresIn(Duration.between(LocalDateTime.now(ZoneOffset.UTC).toLocalTime()
                        , LocalDateTime.parse(entity.getExpiresAt())).plusHours(24)
                .toHours() + " hours");
        storyRepository.save(entity);

        return response;
    }

    private StoryCheckResponse convertToResponse(HikerApiResponse hikerApiResponse, String url) {
        StoryCheckResponse response = new StoryCheckResponse();

        response.setId(hikerApiResponse.getId());
        response.setPublicationDate(hikerApiResponse.getTakenAt());
        response.setExists(true);
        response.setMentions(hikerApiResponse.getMentions()
                .stream()
                .map(mention -> mention.getUser().getUsername())
                .toList());
        response.setLinks(hikerApiResponse.getLinks()
                .stream()
                .map(HikerApiResponseLinks::getWebUri)
                .toList());
        response.setHashtags(hikerApiResponse.getHashtags()
                .stream()
                .map(hashtag -> hashtag.getHashtag().getName())
                .toList());
        response.setLocations(hikerApiResponse.getLocations()
                .stream()
                .map(location -> location.getLocation().getName())
                .toList());
        response.setExpiresIn(Duration.between(LocalDateTime.now(ZoneOffset.UTC).toLocalTime()
                        , LocalDateTime.parse(hikerApiResponse.getTakenAt()
                                , DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")).plusHours(24))
                        .plusHours(24)
                .toHours() + " hours");

        StoryEntity entity = storyMapper.toStoryEntity(response);
        entity.setIsActive(true);
        entity.setUrl(url);
        entity.setExpiresAt(LocalDateTime.parse(hikerApiResponse.getTakenAt()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")).plusHours(24)
                .toString());

        storyRepository.save(entity);


        return response;
    }
}
