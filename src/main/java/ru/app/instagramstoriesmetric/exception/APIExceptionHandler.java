package ru.app.instagramstoriesmetric.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.app.instagramstoriesmetric.dto.StoryCheckResponse;
import ru.app.instagramstoriesmetric.feature.RequestMetrics;

@ControllerAdvice
public class APIExceptionHandler {

    private final RequestMetrics requestMetrics;

    @Autowired
    public APIExceptionHandler(RequestMetrics requestMetrics) {
        this.requestMetrics = requestMetrics;
    }

    @ExceptionHandler(StoryNotFoundException.class)
    public ResponseEntity<StoryCheckResponse> handleStoryNotFoundException() {
        StoryCheckResponse response = new StoryCheckResponse();

        response.setError("Story has expired or been deleted");
        response.setExists(false);

        requestMetrics.incrementErrorCount();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<StoryCheckResponse> handleException() {
//        requestMetrics.incrementErrorCount();
//        StoryCheckResponse response = new StoryCheckResponse();
//
//        response.setError("Something went wrong");
//
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
