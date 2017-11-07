package com.jenifly.zpqb.utils;

/**
 * Created by Jenifly on 2017/8/30.
 */

public class ItemKeyUtils {
    public int index;
    public int nextIndex;
    public int size;
    public String text;
    public boolean show = false;
    public boolean isOpen = false;

    public ItemKeyUtils(int index, int nextIndex, int size, String text, boolean show, boolean isOpen) {
        this.index = index;
        this.nextIndex = nextIndex;
        this.size = size;
        this.text = text;
        this.show = show;
        this.isOpen = isOpen;
    }
}
