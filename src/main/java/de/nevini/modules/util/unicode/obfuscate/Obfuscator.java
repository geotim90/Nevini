package de.nevini.modules.util.unicode.obfuscate;

import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Obfuscator {

    private static final Map<Character, char[]> map;
    private static final Map<Character, Character> pam;

    static {
        map = new HashMap<>();
        // see: https://github.com/etanzapinsky/unicode-obfuscation/blob/master/main.py
        map.put('a', new char[]{'ª', '∀', '⟑', 'α', '@'});
        map.put('b', new char[]{'฿', 'В', 'ь', 'β'});
        map.put('c', new char[]{'©', '∁', '⊂', '☪', '¢'});
        map.put('d', new char[]{'∂', '⫒', 'ძ'});
        map.put('e', new char[]{'ℇ', '℮', '∃', '∈', '∑', '⋿', '€', 'ϱ'});
        map.put('f', new char[]{'⨍', '⨗', '⫭', '៛', 'ϝ'});
        map.put('g', new char[]{'₲', 'ց', 'Ԍ'});
        map.put('h', new char[]{'ℏ', '⫲', '⫳', '₶'});
        map.put('i', new char[]{'ℹ', '⫯', 'ι', 'ї'});
        map.put('j', new char[]{'⌡', 'ϳ', 'ј'});
        map.put('k', new char[]{'₭', 'κ', 'Ϗ'});
        map.put('l', new char[]{'∟', '₤', 'լ'});
        map.put('m', new char[]{'≞', '⋔', '⨇', '⩋', '⫙', '₥'});
        map.put('n', new char[]{'∏', '∩', 'η'});
        map.put('o', new char[]{'º', '⦿', '☉', 'ο', 'օ'});
        map.put('p', new char[]{'℗', '♇', '₱', 'ρ', 'բ'});
        map.put('q', new char[]{'ԛ', 'զ', 'գ', '৭', 'ҩ'});
        map.put('r', new char[]{'®', 'Я', 'Ւ'});
        map.put('s', new char[]{'∫', '$', 'ѕ'});
        map.put('t', new char[]{'⊺', '⟙', '✝', '♱', '♰', 'τ', 'է'});
        map.put('u', new char[]{'µ', '∪', '∐', '⨃'});
        map.put('v', new char[]{'∨', '√', '⩔'});
        map.put('w', new char[]{'⨈', '⩊', '⫝', '₩', 'ω'});
        map.put('x', new char[]{'×', '⨯', '☓', '✗'});
        map.put('y', new char[]{'¥', '⑂', 'Ⴤ', 'ӱ'});
        map.put('z', new char[]{'Ꙁ', 'Ⴠ', 'Հ'});
        // reverse map
        pam = new HashMap<>();
        map.forEach((k, v) -> {
            for (char c : v) {
                pam.put(c, k);
            }
        });
    }

    public static String obfuscate(String input) {
        Random rng = new Random();
        StringBuilder output = new StringBuilder(input.length());
        input.chars().map(c -> {
            char[] choice = map.get((char) Character.toLowerCase(c));
            if (choice == null) return c;
            return choice[rng.nextInt(choice.length)];
        }).forEach(c -> output.append((char) c));
        return output.toString();
    }

    public static String deobfuscate(String input) {
        StringBuilder output = new StringBuilder(input.length());
        input.chars().forEach(c -> output.append(ObjectUtils.defaultIfNull(pam.get((char) c), (char) c)));
        return output.toString();
    }

}
