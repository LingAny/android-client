package ru.tp.lingany.lingany.utils;

import ru.tp.lingany.lingany.R;

public class FlagColorGenerator {

    private int counter;

    public FlagColorGenerator() {
        counter = 0;
    }

    public int getColorResId() {
        counter++;
//        if (counter == 1) {
//            return R.color.flag_cyan_500;
//        } else if (counter == 2) {
//            return R.color.flag_teal_500;
//        } else {
//            counter = 0;
//            return R.color.flag_light_blue_500;
//        }
//        if (counter == 1) {
//            return R.color.flag_indigo_500;
//        } else if (counter == 2) {
//            return R.color.flag_blue_500;
//        } else {
//            counter = 0;
//            return R.color.flag_deep_purple_500;
//        }

        if (counter == 1) {
            return R.color.flag_indigo_500;
        } else if (counter == 2) {
            return R.color.flag_blue_500;
        } else if (counter == 3) {
            return R.color.flag_deep_purple_500;
        } else if (counter == 4) {
            return R.color.flag_blue_grey_500;
        } else if (counter == 5) {
            return R.color.flag_cyan_500;
        } else {
            counter = 0;
            return R.color.flag_teal_500;
        }

    }
}
