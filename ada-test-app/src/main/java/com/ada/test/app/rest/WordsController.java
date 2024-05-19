package com.ada.test.app.rest;

import com.ada.test.core.service.WordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.ada.test.common.router.Router.WordsAPI.*;


@Slf4j
@RestController
@RequestMapping(ROOT)
public class WordsController {
    private final WordsService service;

    public WordsController(WordsService service) {
        this.service = service;
    }
    @GetMapping(FIND)
    public String checkWord(@RequestParam String word) {
        log.info("WordsController::checkWord  --[{}] ", word);
        List<Integer> positions = service.findWord(word);
        if (positions.isEmpty()) {
            return "No se puede formar la palabra con los caracteres disponibles.";
        } else {
            return "Se puede escribir la palabra utilizando las posiciones: " + positions.toString();
        }
    }
}
