package com.streamlinity.ct.restService;

import com.streamlinity.ct.model.Item;
import com.streamlinity.ct.restService.challenge.SearchSvcInterface;

import junitparams.JUnitParamsRunner;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class SearchServiceUnitTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    SearchSvcInterface searchSvc;

    // See  https://github.com/Pragmatists/junitparams-spring-integration-example/blob/master/README.md
    // for an explanation on running this with JunitParams under spring

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    // 0 Item file
    @Test
    public void testSearchSvcOItems() throws Exception {
        final String itemFileName = "classpath:0Item.json";

        final File itemFile = applicationContext.getResource(itemFileName).getFile();
        searchSvc.init(itemFile);

        List<Item> allItems = searchSvc.getItems();
        List<Item> itemsInCategoryC = searchSvc.getItems("C");
        List<Item> itemWithShortnameC1 = searchSvc.getItems("C1");

        assertEquals(String.format("getItems list size: %d", allItems.size()), allItems.size(),
                0);

        assertEquals(String.format("getItems(C) list size: %d", itemsInCategoryC.size()), itemsInCategoryC.size(),
                0);

        assertEquals(String.format("getItems(C1) list size: %d", itemWithShortnameC1.size()), itemWithShortnameC1.size(),
                0);


    }

    // 3 Items file
    @Test
    public void testSearchSvc3Items() throws Exception {
        final String itemFileName = "classpath:3Items.json";

        final File itemFile = applicationContext.getResource(itemFileName).getFile();
        searchSvc.init(itemFile);

        List<Item> allItems = searchSvc.getItems();
        List<Item> itemsInCategoryC = searchSvc.getItems("C");
        List<Item> itemsInCategoryD = searchSvc.getItems("D"); // should not find any matches
        List<Item> itemsInCategoryS = searchSvc.getItems("S");  // should not find any matches
        List<Item> itemsInCategorySSS = searchSvc.getItems("SSS");  // should not find any matches
        List<Item> itemsInCategorySS = searchSvc.getItems("SS");  // should find 2 matches


        List<Item> itemWithShortnameC1 = searchSvc.getItem("C1"); // should not find any matches
        List<Item> itemWithShortnameC4 = searchSvc.getItem("C4");
        List<Item> itemWithShortnameSS3 = searchSvc.getItem("SS3");
        List<Item> itemWithShortnameSS2 = searchSvc.getItem("SS2");


        // All items

        assertEquals(String.format("getItems list size: %d", allItems.size()), 3,
                allItems.size());

        // Search by category - positives

        assertEquals(String.format("getItems(C) list size: %d", itemsInCategoryC.size()), itemsInCategoryC.size(),
                1);

        assertEquals(String.format("getItems(SS) list size: %d", itemsInCategorySS.size()), itemsInCategorySS.size(),
                2);

        // Search by category - should not find any matches

        assertEquals(String.format("getItems(D) list size: %d", itemsInCategoryD.size()), itemsInCategoryD.size(),
                0);

        assertEquals(String.format("getItems(SSS) list size: %d", itemsInCategorySSS.size()), itemsInCategorySSS.size(),
                0);

        assertEquals(String.format("getItems(S) list size: %d", itemsInCategoryS.size()), itemsInCategoryS.size(),
                0);

        // Search by short name - positive

        assertEquals(String.format("getItems(SS1) list size: %d", itemWithShortnameSS3.size()), itemWithShortnameSS3.size(),
                1);

        assertEquals(String.format("getItems(C4) list size: %d", itemWithShortnameC4.size()), itemWithShortnameC4.size(),
                1);

        assertEquals(String.format("getItems(SS2) list size: %d", itemWithShortnameSS2.size()), itemWithShortnameSS2.size(),
                1);

        // Search by short name - should not fine any matches

        assertEquals(String.format("getItems(C1) list size: %d", itemWithShortnameC1.size()), itemWithShortnameC1.size(),
                0); // not found


    }
}
