package de.nevini.modules.util.unicode.morse;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MorseEncoder {

    private static final BiMap<Character, String> map;

    static {
        map = HashBiMap.create(40);
        // http://www.csgnetwork.com/morsecodechrtbl.html
        map.put('a', ".-");
        map.put('b', "-...");
        map.put('c', "-.-.");
        map.put('d', "-..");
        map.put('e', ".");
        map.put('f', "..-.");
        map.put('g', "--.");
        map.put('h', "....");
        map.put('i', "..");
        map.put('j', ".---");
        map.put('k', "-.-");
        map.put('l', ".-..");
        map.put('m', "--");
        map.put('n', "-.");
        map.put('o', "---");
        map.put('p', ".--.");
        map.put('q', "--.-");
        map.put('r', ".-.");
        map.put('s', "...");
        map.put('t', "-");
        map.put('u', "..-");
        map.put('v', "...-");
        map.put('w', ".--");
        map.put('x', "-..-");
        map.put('y', "-.--");
        map.put('z', "--..");
        map.put('0', "-----");
        map.put('1', ".----");
        map.put('2', "..---");
        map.put('3', "...--");
        map.put('4', "....-");
        map.put('5', ".....");
        map.put('6', "-....");
        map.put('7', "--...");
        map.put('8', "---..");
        map.put('9', "----.");
        map.put('.', ".-.-.-");
        map.put(',', "--..--");
        map.put('=', "-...-");
        map.put('?', "..--..");
        map.put(' ', "+");
    }

    public static String encode(String input) {
        return input.chars()
                .mapToObj(c -> ObjectUtils.defaultIfNull(map.get((char) Character.toLowerCase(c)), " "))
                .collect(Collectors.joining(" "));
    }

    public static String decode(String input) {
        return Arrays.stream(input.split(" ")).map(s -> {
            Character c = map.inverse().get(s);
            if (c == null) return s;
            return c.toString();
        }).collect(Collectors.joining());
    }

}
