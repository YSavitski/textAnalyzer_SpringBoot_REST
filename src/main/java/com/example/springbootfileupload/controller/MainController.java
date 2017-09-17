package com.example.springbootfileupload.controller;

import com.example.springbootfileupload.components.brackets.BracketsAnalyzer;
import com.example.springbootfileupload.components.textanalyzer.TextAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {
    @Autowired
    private TextAnalyzer textAnalyzer;

    @Autowired
    private BracketsAnalyzer bracketsAnalyzer;

    @PostMapping("/")
    public Object getTopRepeatedWords(
            @RequestParam("file")MultipartFile file,
            @RequestParam("checkType") String checkType)
    {
        if(checkType.equals("textAnalyzer")){
            return textAnalyzer.getStatisticByRepeatedWords(file);
        } else {
            return bracketsAnalyzer.checkBrackets(file) ? "correct brackets query" : "correct brackets query";
        }
    }


}
