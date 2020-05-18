package com.streamlinity.ct.restService.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamlinity.ct.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Provide your implementation of the SearchSvcImpl here.
 * Also annotate your methods with Rest end point wrappers as required in the problem statement
 *
 * You can create any auxiliary classes or interfaces in this package if you need them.
 *
 * Do NOT add annotations as a Bean or Component or Service.   This is being handled in the custom Config class
 * PriceAdjustConfiguration
 */

public class SearchSvcImpl implements SearchSvcInterface {

    Logger logger = LoggerFactory.getLogger(SearchSvcImpl.class);

    File itemJsonFIle;
    List<Item> itemList;
    @Override
    public void init(String itemPriceJsonFileName) {
        itemJsonFIle=  new File(itemPriceJsonFileName);
    }

    @Override
    public void init(File itemPriceJsonFile) {
        ObjectMapper mapper = new ObjectMapper();

        try{
            Item []itemsArray = mapper.readValue(itemPriceJsonFile,Item[].class);
            itemList = Arrays.asList(itemsArray);

        }catch (IOException ex){
            logger.error("Exception occur while read file {} ",ex);
        }


    }

    @Override
    public List<Item> getItems() {
        return itemList;
    }

    @Override
    public List<Item> getItems(String category) {

        if(null == category){
            return  new ArrayList<>();
        }
        return itemList.stream()
                .filter(c -> Objects.nonNull(c.getCategory_short_name()))
                .filter(c->c.getCategory_short_name().equalsIgnoreCase(category)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItem(String itemShortName) {
        if(null == itemShortName){
            return  new ArrayList<>();
        }
        return itemList.stream()
                .filter(c -> Objects.nonNull(c.getShort_name()))
                .filter(c->c.getShort_name().equalsIgnoreCase(itemShortName)).collect(Collectors.toList());
    }
}
