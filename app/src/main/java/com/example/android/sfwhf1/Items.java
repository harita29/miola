package com.example.android.sfwhf1;

/**
 * Created by archanayadawa on 11/16/17.
 */

public class Items
{
    private String name;
    private Integer cal;
    public Items (String aName, Integer bCal) {
        name = aName;
        cal = bCal;
    }
    public String getName()
    {return name; }

    public int getCal()
    {return cal;}
}
