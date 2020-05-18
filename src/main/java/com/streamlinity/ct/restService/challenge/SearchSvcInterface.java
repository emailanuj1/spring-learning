package com.streamlinity.ct.restService.challenge;


/*
 * Do not edit this interface.   Your SearchSvcInterface solution must implement this interface
 */

import java.io.File;
import java.util.List;

import com.streamlinity.ct.model.Item;

public interface SearchSvcInterface {

    /*
     * init method
     *
     * @param itemAdjustedPriceJsonFileName:  Fully qualified file name that contains the  adjusted prices for
     * restaurant menu items.
     *
     * Your searchSvc should read this file to get the data.
     */

    public void init(String itemPriceJsonFileName);



    //  Variant that takes a file instead of a fully qualified path name.
    public void init(File itemPriceJsonFile);

    /*
     * getItems:  all items
     */

    public List<Item> getItems();

    /*
     * getItems:  filtered by category ShortName
     */

    public List<Item> getItems(String category);

    /*
     * getItem:  filtered by Item's shortName
     */

    public List<Item> getItem(String itemShortName);
}

