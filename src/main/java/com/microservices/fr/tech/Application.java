package com.microservices.fr.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);

        /**
         String input = "caabbaabccc";
         Map<Character, Integer> data = new LinkedHashMap<>();
         // Loop thourgh chars
         for( int i = 0; i < input.length(); i++ ) {
         char letter = input.charAt(i);
         int counts = 1;
         if( data.containsKey(letter) ) {
         counts += data.get(letter);
         }
         data.put(letter, counts);
         }
         // Sorted map
         Map<Character, Integer> sortedCounts = new HashMap<>();
         // Sort map based on key
         data.entrySet().stream().sorted(Map.Entry.comparingByKey())
         .forEachOrdered(sortedMap -> sortedCounts.put(sortedMap.getKey(), sortedMap.getValue()));
         // Loop through sortedCounts
         sortedCounts.forEach((key, value) -> System.out.print(value + String.valueOf(key)));
         */
    }

}