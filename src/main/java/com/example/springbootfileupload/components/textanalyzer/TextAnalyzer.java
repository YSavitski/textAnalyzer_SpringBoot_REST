package com.example.springbootfileupload.components.textanalyzer;

import com.example.springbootfileupload.util.ExcludedWordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TextAnalyzer {
    private final Logger logger = LoggerFactory.getLogger(TextAnalyzer.class);

    private int max=1;
    private final ArrayList<String> neededWords = new ArrayList<>(1);
    private final HashMap<String, WordsCounter> countsWords = new HashMap<>();

    /**
     * Putting original word
     * @param word - original word
     */
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

    /**
     * Method returns sorted Map of the words by count repeats
     * @param topValue - count returned of top number positions of words
     * @return sorted by count repeats of words Map
     */
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
                .filter(map-> !ExcludedWordUtil.exludedRussianWords().contains(map.getKey()))
                .limit(topValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (v1,v2) -> v1, LinkedHashMap::new));
    }

    /**
     * Reading user's file and counting repetitions
     * @param file - user's input file
     * @return - top10 common words with their sum of repetitions
     */
    public Map<String, WordsCounter> getStatisticByRepeatedWords(MultipartFile file){
        StringBuilder sb = new StringBuilder(100);

        logger.info(String.format("Started reading file %s for text analysis", file.getOriginalFilename()));
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
        logger.info(String.format("Finish reading file %s for text analysis", file.getOriginalFilename()));

        return topWords(10);
    }
}
