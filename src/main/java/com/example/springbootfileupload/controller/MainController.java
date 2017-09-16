package com.example.springbootfileupload.controller;

import com.example.springbootfileupload.service.TextAnalyzer;
import com.example.springbootfileupload.service.WordsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@RestController
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private TextAnalyzer textAnalyzer;

    @PostMapping("/")
    public Map<String, WordsCounter> getTopRepeatedWords(
            @RequestParam("file")MultipartFile file,
            @RequestParam("topValue") int topValue)
    {
        return textAnalyzer.getStatisticByRepeatedWords(file, topValue);
    }


}
