package com.streamlinity.ct.restService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamlinity.ct.model.Item;
import com.streamlinity.ct.restService.challenge.SearchSvcInterface;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

// Run a real server
@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, 
				classes = com.streamlinity.ct.SpringRestChallengeApplication.class)

public class SearchRestControllerIntegrationTests {

     Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private SearchSvcInterface searchSvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    ApplicationContext appContext;


    private final String itemPriceJsonFileName = "classpath:itemPrices.json";
    private File itemPriceFile;
    private List<Item> itemsFromItemPriceFile;
    private ObjectMapper mapper = new ObjectMapper();
    private List<Item> itemsFromRestEndpoint;

    
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    @Before
    public void init() {

        try {
            itemPriceFile = appContext.getResource(itemPriceJsonFileName).getFile();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            itemsFromItemPriceFile = mapper.readValue(itemPriceFile, new TypeReference<List<Item>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Exception in init", false);
        }

    }

    @Test
    public void testGetAllItems() throws Exception {

        searchSvc.init(itemPriceFile);

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/item", String.class);

        assertEquals(String.format("URI: %s, Status Code: %s", testRestTemplate.getRootUri(), responseEntity.getStatusCode()), HttpStatus.OK,
                responseEntity.getStatusCode());

        itemsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Item>>() {
        });



        assertTrue("Items not equal", areListsIdentical(itemsFromItemPriceFile, itemsFromRestEndpoint));


    }

    @Test
    @TestCaseName("testGetItemByShortname: {2}")
    @Parameters({
            "C1, classpath:C1OriginalFromRestaurantService.json, Existing ShortName",
            "SX5, classpath:0Item.json, Non-existing ShortName"
    })
    public void testGetItemByShortname(String shortName, String referenceFileName, String displayName) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity( "/item/" + shortName, String.class);

        assertEquals(String.format("URI: %s, Status Code: %s", testRestTemplate.getRootUri(), responseEntity.getStatusCode()), HttpStatus.OK,
                responseEntity.getStatusCode());

        itemsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Item>>() {
        });

        File referenceDataFile = appContext.getResource(referenceFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Item> itemsFromReferenceDataFile =
                mapper.readValue(referenceDataFile, new TypeReference<List<Item>>() {
                });


        assertTrue("Items not equal", areListsIdentical(itemsFromReferenceDataFile, itemsFromRestEndpoint));

    }

    @Test
    public void testGetItemsByCategory() throws Exception {

        ObjectMapper mapper = new ObjectMapper();


        // Make the call - check for success
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/item" + "?category=" + "C", String.class);
        assertEquals(String.format("URI: %s, Status Code: %s", testRestTemplate.getRootUri(), responseEntity.getStatusCode()), HttpStatus.OK,
                responseEntity.getStatusCode());

        // Process C4 returned payload

        itemsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Item>>() {
        });

        assertTrue(String.format("Error: C4 category search list size 0!"),
                itemsFromRestEndpoint.size() > 0);

        //Category SX should not find any items

        responseEntity = testRestTemplate.getForEntity("/item" + "?category=" + "SX", String.class);
        itemsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Item>>() {
        });

        assertTrue(String.format("Error: SX category search list size not 0!"),
                itemsFromRestEndpoint.size() == 0);


        //Category S should not find any items  - it is a substring of SS but should not match.

        responseEntity = testRestTemplate.getForEntity("/item"  + "?category=" + "S", String.class);
        itemsFromRestEndpoint = mapper.readValue(responseEntity.getBody(), new TypeReference<List<Item>>() {
        });

        assertTrue(String.format("Error: S category search list size not 0!"),
                itemsFromRestEndpoint.size() == 0);


    }

    private boolean areListsIdentical(List<Item> expectedItems, List<Item> actualItems) {

        boolean isIdentical = true;

        if (expectedItems.size() != actualItems.size()) {
            logger.info(String.format("Expected size: %d; Actual size: %d", expectedItems.size(), actualItems.size()));
            return false;
        }

        for (int i=0; i<expectedItems.size(); i++) {
            if (!expectedItems.get(i).getShort_name().matches(actualItems.get(i).getShort_name()) ||
                expectedItems.get(i).getPrice_large() != actualItems.get(i).getPrice_large()) {
                logger.info(String.format("Items differ at index: %d Shortname: %s", i, expectedItems.get(i).getShort_name()));
                return false;
            }
        }

        return  isIdentical;
    }

}
