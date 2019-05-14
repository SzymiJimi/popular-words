package pl.sii;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PopularWordsTest {
    private static final PopularWords testee = new PopularWords();

    @Test
    public void shouldReturnOneThousandMostPopularWords() {
        //given
        Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff = getWordsFrequencyListCreatedByAdamKilgarriff();

        //when
        Map<String, Long> result = testee.findOneThousandMostPopularWords();

        //then
        assertFalse(result.isEmpty());
        assertEquals(1000, result.size());
        compareWordListsFrequency(wordsFrequencyListCreatedByAdamKilgarriff, result);
    }

    private void compareWordListsFrequency(Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff, Map<String, Long> result) {
        long totalFrequencyByKilgarriff = wordsFrequencyListCreatedByAdamKilgarriff.values().stream().reduce(0L, Long::sum);
        long totalFrequencyInAResult = result.values().stream().reduce(0L, Long::sum);
        System.out.println("totalFrequencyByKilgarriff = " + totalFrequencyByKilgarriff);
        System.out.println("totalFrequencyInAResult = " + totalFrequencyInAResult);

        result.forEach((key, value) -> {
            BigDecimal valueUsagePercentage = calculatePercentage(value, totalFrequencyInAResult);
            Long frequency  = wordsFrequencyListCreatedByAdamKilgarriff.get(key);
            if(frequency==null)
                frequency = 0L;
            BigDecimal kilgarriffUsagePercentage = calculatePercentage(frequency, totalFrequencyByKilgarriff);
            BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
            System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);
        });
    }

    private BigDecimal calculatePercentage(double obtained, double total) {
        return BigDecimal.valueOf(obtained * 100 / total).setScale(4, RoundingMode.HALF_UP);
    }

    private String readFileToString()  {
        try {
            return new String(Files.readAllBytes(Paths.get("src\\test\\resources\\all.num")));
        } catch (IOException e) {
            System.out.println("Error with file loading!" + e.toString());
            return "";
        }
    }

    private Map<String, Long> loadWordsIntoMap(String file){
        if(file.equals(""))
            return new HashMap<>();

        ArrayList<String> lines =  new ArrayList<>(Arrays.asList( file.split("[\n]")));
        HashMap<String, Long> wordsFrequency = new HashMap<>();
        lines.stream()
                .skip(1)
                .forEach(line -> {
                     var wordsInLine = line.split("[ ]");
                     wordsFrequency.put(wordsInLine[1], Long.valueOf( wordsInLine[0]));
                });
        return wordsFrequency;
    }

    private Map<String, Long> getWordsFrequencyListCreatedByAdamKilgarriff() {
        return loadWordsIntoMap(readFileToString());
    }
}
