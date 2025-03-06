package ru.app.instagramstoriesmetric.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "Тело запроса для проверки сториса")
@Data
public class StoryCheckRequest {

    @Schema(description = "Ссылка на сторис")
    @Pattern(regexp = "^https://instagram\\.com/stories/.*")
    private String url;

    @Schema(description = "ID сториса (ОПЦИОНАЛЬНО)")
    private String id;
}
