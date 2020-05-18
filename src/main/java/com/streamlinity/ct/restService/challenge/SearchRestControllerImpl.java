package com.streamlinity.ct.restService.challenge;

import com.streamlinity.ct.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

/*
 * This controller needs to expose the following rest endpoints.  You need to fill in the implementation here
 *
 * Required REST Endpoints
 *
 *      /item                       Get all items
 *      /item?category=C            Get all items in category specified by Category shortName
 *      /item/{itemShortName}       Get item that matches the specified Item shortName
 */

@Profile("default")
@RestController
@RequestMapping(value = "/item")
public class SearchRestControllerImpl {

    @Autowired
    SearchSvcInterface searchSvcInterface;


    @RequestMapping(method = RequestMethod.GET)
    public List<Item> getItems(){
        return searchSvcInterface.getItems();
    }

    @RequestMapping(params = "category", method = RequestMethod.GET)
    public List<Item> getItemByCategory(@RequestParam String category) {
        return searchSvcInterface.getItems(category);
    }

    @RequestMapping(path = "/{itemShortName}", method = RequestMethod.GET)
    public List<Item> getItemByItemShortName(@PathVariable(value = "itemShortName") String itemShortName) {
        return searchSvcInterface.getItem(itemShortName);
    }


}
