package com.ada.test.common.ex;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorResponseHandler {
    private ErrorResponseHandler() {
        throw new IllegalStateException("ErrorResponseHandler");
    }
    public static ResponseEntity<Object> buildResponseEntity(String message, String debugMessage, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("debugMessage", debugMessage);
        map.put("code", httpStatus.value());
        map.put("status", httpStatus.name());
        map.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(map, httpStatus);
    }

    public static ResponseEntity<Object> buildResponseEntity(String message, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("debugMessage", "");
        map.put("code", httpStatus.value());
        map.put("status", httpStatus.name());
        map.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(map, httpStatus);
    }
}
