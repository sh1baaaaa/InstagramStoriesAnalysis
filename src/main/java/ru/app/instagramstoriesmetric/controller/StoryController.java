package ru.app.instagramstoriesmetric.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.app.instagramstoriesmetric.dto.StoryCheckRequest;
import ru.app.instagramstoriesmetric.dto.StoryCheckResponse;
import ru.app.instagramstoriesmetric.feature.RequestMetrics;
import ru.app.instagramstoriesmetric.service.StoryCheckService;

@Tag(name = "Сторисы", description = "API для взаимодействия с сторисами")
@RequestMapping("/api/stories")
@RestController
public class StoryController {

    private final StoryCheckService storyCheckService;

    private final RequestMetrics requestMetrics;

    @Autowired
    public StoryController(StoryCheckService storyCheckService, RequestMetrics requestMetrics) {
        this.storyCheckService = storyCheckService;
        this.requestMetrics = requestMetrics;
    }

    @ApiResponse(responseCode = "404", description = "Сторис не найден или не существует")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос (отсутствуют url/id или неверный формат)")
    @ApiResponse(responseCode = "401", description = "Неверный или отсутствующий X-Hiker-Token")
    @ApiResponse(responseCode = "200", description = "Успешный запрос. Результат в теле ответа")
    @ApiResponse(responseCode = "429", description = "Превышен лимит запросов к HikerAPI (Retry-After: 60)")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервиса")
    @Operation(summary = "Проверить сторис", description = "Возвращает информацию о сторис по заданному URL")
    @PostMapping("/check")
    public ResponseEntity<StoryCheckResponse> storyCheck(@RequestBody StoryCheckRequest request) {
        requestMetrics.incrementRequestCount();
        return new ResponseEntity<>(storyCheckService.fetchStoryData(request), HttpStatus.OK);
    }

}
