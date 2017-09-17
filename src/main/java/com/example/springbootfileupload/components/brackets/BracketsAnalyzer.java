package com.example.springbootfileupload.components.brackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class BracketsAnalyzer {
    private final Logger logger = LoggerFactory.getLogger(BracketsAnalyzer.class);

    @Autowired
    private BracketsChecker checker;

    public boolean checkBrackets(MultipartFile file){
        boolean flag = true;
        logger.info(String.format("Started reading file %s for brackets analysis", file.getOriginalFilename()));
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "CP1251"))){
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null){
                if(checker.checkChars(currentLine))
                    continue;
                else
                    flag = false;
                break;
            }

            if(!checker.checkIsEmptyStack()){
                flag = false;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        logger.info(String.format("Finish reading file %s for brackets analysis", file.getOriginalFilename()));

        return flag;

    }
}
