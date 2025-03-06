package ru.app.instagramstoriesmetric.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Тело информации о сторисе")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StoryCheckResponse {

    @Schema(description = "Активность")
    private boolean exists;

    @Schema(description = "ID сториса")
    private String id;

    @Schema(description = "Дата публикации")
    @JsonProperty("publication_date")
    private String publicationDate;

    @Schema(description = "До истечения")
    private String expiresIn;

    @Schema(description = "Упоминания")
    private List<String> mentions;

    @Schema(description = "Локации")
    private List<String> locations;

    @Schema(description = "Хэштеги")
    private List<String> hashtags;

    @Schema(description = "Ссылки")
    private List<String> links;

    @Schema(description = "Текст ошибки")
    private String error;

}

