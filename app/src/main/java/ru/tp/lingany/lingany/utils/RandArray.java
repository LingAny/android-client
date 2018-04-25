package ru.tp.lingany.lingany.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandArray {

    private static Random rand = new Random();

    public static List<Integer> getRandIdx(int size, int min, int max) {
        List<Integer> arr = new ArrayList<>();
        while (arr.size() < size) {
            Integer idx = rand.nextInt(max - min + 1) + min;//random between min and max including both
            if (!arr.contains(idx)) {
                arr.add(idx);
            }
        }

        return arr;
    }
}