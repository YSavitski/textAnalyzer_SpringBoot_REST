package com.example.springbootfileupload.controller;

import com.example.springbootfileupload.components.brackets.BracketsAnalyzer;
import com.example.springbootfileupload.components.textanalyzer.TextAnalyzer;
import com.example.springbootfileupload.components.textanalyzer.WordsCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    @Autowired
    private TextAnalyzer textAnalyzer;

    @Autowired
    private BracketsAnalyzer bracketsAnalyzer;

    private byte[] tempFileBytes;

    @GetMapping("/textAnalyzer")
    public Map<String, WordsCounter> topTenWords(){
        return textAnalyzer.getStatisticByRepeatedWords(new ByteArrayInputStream(tempFileBytes));
    }

    @GetMapping("/bracketsAnalyzer")
    public Map<String, String> checkBrackets(){
        Map<String, String> map = new HashMap<>();
        String message = bracketsAnalyzer.checkBrackets(new ByteArrayInputStream(tempFileBytes)) ?  "correct brackets query" : "incorrect brackets query";
        map.put("Result of check is: ", message);
        return map;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
            saveFile(file);
        return new ResponseEntity("Successfully uploaded - " +
                file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    private void saveFile(MultipartFile file) throws IOException {
        tempFileBytes = file.getBytes();
    }

}
