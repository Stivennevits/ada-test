package com.ada.test.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WordsService {
    private final char[] characters = {
            'w', 'e', 'r', 'f', 'b', 'h', 'j', 'i', 'u', 'y', 't', 'r', 'e', 'd', 'f', 'g', 'u', 't', 'r', 'e', 's', 'd', 'f', 'g', 'y', 'u', 'i', 'o', 'l', 'k', 'm', 'n', 'b', 'v', 'f', 'r', 'e', 'w', 's', 'x', 'f', 'g', 'y', 'u', 'i', 'k', 'm', 'n', 'b', 'v', 'f', 'r', 'e', 'w', 'w', 'r', 't', 'y', 'u', 'i', 'o', 'k', 'm', 'n', 'b', 'v', 'd', 'w', 's', 'x', 'c', 'f', 'g', 'h', 'u', 'i', 'o', 'p', 'l', 'k', 'n', 'b', 'v', 'f', 'd', 'e', 'w', 'a', 'z', 'x', 'c', 'g', 'h', 'u', 'i', 'o', 'p', 'u', 'y', 't', 'r', 'e', 'w', 'q', 's', 'd', 'f', 'g', 'k', 'j', 'b', 'v', 'c', 'x'
    };

    public List<Integer> findWord(String word) {
        word = word.replaceAll("\\s", "").toLowerCase();
        List<Integer> positions = new ArrayList<>();
        boolean[] used = new boolean[characters.length];

        for (char c : word.toCharArray()) {
            boolean found = false;
            for (int i = 0; i < characters.length; i++) {
                if (!used[i] && characters[i] == c) {
                    positions.add(i);
                    used[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return new ArrayList<>();
            }
        }

        return positions;
    }
}
