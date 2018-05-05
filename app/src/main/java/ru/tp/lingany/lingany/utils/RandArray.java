package ru.tp.lingany.lingany.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandArray {

    private static Random rand = new Random();

    public static List<Integer> getRandIndexes(int size, int min, int max, int indexToAvoid) {
        List<Integer> arr = new ArrayList<>();
        while (arr.size() < size) {
            Integer idx = rand.nextInt(max - min + 1) + min;//random between min and max including both
            if (!arr.contains(idx) && idx != indexToAvoid) {
                arr.add(idx);
            }
        }

        return arr;
    }

    public static Integer getRandIndex(int min, int max) {
        return rand.nextInt(max - min + 1) + min;//random between min and max including both
    }

    public static Integer getRandIndex() {
        return rand.nextInt();//random between min and max including both
    }
}