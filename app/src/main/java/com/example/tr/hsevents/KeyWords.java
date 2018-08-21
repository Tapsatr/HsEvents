package com.example.tr.hsevents;

/**
 * Created by T.R on 02/12/2017.
 */

public class KeyWords {

    String keyword;
    String id;


    public KeyWords(String keyword, String id ) {
        this.keyword=keyword;
        this.id = id;

    }

    public String getKeyword() {
        return keyword;
    }
    public String getId() {
        return id;
    }
    public String toString() {return keyword;}


}
