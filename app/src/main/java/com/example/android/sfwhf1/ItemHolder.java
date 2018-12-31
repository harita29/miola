package com.example.android.sfwhf1;

/**
 * Created by archanayadawa on 11/7/17.
 */

public class ItemHolder
{
    public String itemName;
    public Integer cal;

    public ItemHolder(String itemName1, Integer cal1)
    {
        itemName = itemName1;
        cal = cal1;
    }

    public void setName(String itemName1)
    {
        itemName = itemName1;
    }


    public void setCalAmount(Integer cal1)
    {
        cal = cal1;
    }


}
