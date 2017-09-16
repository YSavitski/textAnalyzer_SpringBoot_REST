package com.example.springbootfileupload.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TextAnalyzer {
    private int max=1;
    private final ArrayList<String> neededWords = new ArrayList<>(1);
    private final HashMap<String, WordsCounter> countsWords = new HashMap<>();

    @Value("${excludeRussianWords}")
    private String[] excludeRussianWords;

    public void putWord(String word){
        WordsCounter counter = countsWords.get(word);
        if(counter!=null){
            counter.setValue(counter.getValue()+1);

            if(counter.getValue() > max){
                neededWords.clear();
                neededWords.add(word);
                max = counter.getValue();
            } else if(counter.getValue() == max){
                neededWords.add(word);
            }
        } else {
            countsWords.put(word, new WordsCounter());
        }
    }

    /*public ArrayList<String> mostRepeatableWords(){
        return neededWords;
    }

    public int maxRepeated(){
        return max;
    }

    public void printInfo(){
        countsWords.forEach((word, counter)-> System.out.println(word + ": " + counter.getValue()));
    }*/

    private Map<String, WordsCounter> topWords(int topValue){
        return countsWords.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(new Comparator<WordsCounter>() {
                    @Override
                    public int compare(WordsCounter o1, WordsCounter o2) {
                        if(o1.getValue() == o2.getValue())
                            return 0;
                        else if(o2.getValue() > o1.getValue())
                            return 1;
                        else
                            return -1;
                    }
                }))
                .filter(map-> !Arrays.asList(excludeRussianWords).contains(map.getKey()))
                .limit(topValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (v1,v2) -> v1, LinkedHashMap::new));
    }


    public Map<String, WordsCounter> getStatisticByRepeatedWords(MultipartFile file, int topValue){
        StringBuilder sb = new StringBuilder(100);
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "CP1251"))){
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null){

                for (char ch : currentLine.toCharArray()){
                    if(Character.isLetterOrDigit(ch)){
                        sb.append(Character.toLowerCase(ch));
                    } else {
                        if(sb.length()>0){
                            putWord(sb.toString());
                            sb.setLength(0);
                        }
                    }
                }

                if(sb.length()>0){
                    putWord(sb.toString());
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return topWords(topValue);
    }
}
