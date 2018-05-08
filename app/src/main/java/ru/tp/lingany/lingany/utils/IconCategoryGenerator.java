package ru.tp.lingany.lingany.utils;

import ru.tp.lingany.lingany.R;

public class IconCategoryGenerator {

    private int counter;

    public IconCategoryGenerator() {
        counter = 0;
    }


    public int getIconResId() {
        counter++;
        if (counter == 1) {
            return R.drawable.ic_category_1;
        } else if (counter == 2) {
            return R.drawable.ic_category_2;
        } else if (counter == 3) {
            return R.drawable.ic_category_3;
        } else if (counter == 4) {
            return R.drawable.ic_category_4;
        } else if (counter == 5) {
            return R.drawable.ic_category_5;
        } else {
            counter = 0;
            return R.drawable.ic_category_6;
        }
    }
}
