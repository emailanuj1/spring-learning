Welcome to the Streamlinity Coding Test. You will be building a Search
Service and a Search REST Controller that will retrieve data from
provided JSON files and expose the required end points.

Price Adjust specific instructions:

1. For the Search Service, the API that needs to be supported is at
   SearchSvcInterface.
   
2. For the Search Controller, please see the comments in
   SearchRestControllerImpl to understand the end-point variants your 
   solution needs to provide
   
3. Please note that the provided tests may call your Search service's
   init method with other files (beyond the "official" one in
   src/main/resources/itemPrices.json). Please implement your file
   handling in a flexible way to increase your chances for success with
   the tests.
   
4. In addition to the tests, you can also start up a server by running
   "mvn clean install" or by running
   SpringRestChallengeApplication
   in your IDE as an application. Please note that this starts up your
   Search server at localhost:8080 by default (so you can test your code
   in the running server with localhost:8080/item for example)