package pl.sii;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PopularWords {


    private String readFileToString() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src\\main\\resources\\3esl.txt")));
    }

    private ArrayList<String> splitWordsIntoArray(String content){
        return new ArrayList<>(Arrays.asList( content.split("[-]+|[ ]+|(?=')+|[\n]+|[\r]+")));
    }

    private HashMap<String, Long> countOccurrences(ArrayList<String> words){
        HashMap<String, Long> wordsCollection = new HashMap<>();
        words.stream()
                .filter(word -> word.matches("([^ ])([^ ]*)([^\\n]*)([^']+)"))
                .forEach(word ->{
                    Long value = wordsCollection.get(word.toLowerCase());
                    if(value!=null){
                        value++;
                        wordsCollection.put(word.toLowerCase(), value);
                    }else{
                        wordsCollection.put(word.toLowerCase(), 1L);
                    }
                });

        return wordsCollection.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(1000)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1, LinkedHashMap::new));

    }

    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();
        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();
        result.entrySet().forEach(System.out::println);
    }

    public Map<String, Long> findOneThousandMostPopularWords() {
        try{
            String content = this.readFileToString();
            return countOccurrences(splitWordsIntoArray(content));
        }catch(Exception e){
            System.out.println("Error with file loading: " + e.toString());
            return new HashMap<>();
        }

    }
}
