package com.example.springbootfileupload.components.brackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class BracketsAnalyzer {
    private final Logger logger = LoggerFactory.getLogger(BracketsAnalyzer.class);

    @Autowired
    private BracketsChecker checker;

    /**
     * Reading user's file and check correct query of brackets
     * @param file - user's input file
     * @return correct of incorrect query of brackets
     */
    public boolean checkBrackets(InputStream fileStream){
        boolean flag = true;
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, "CP1251"))){
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

        checker.clearStack();
        return flag;

    }
}
