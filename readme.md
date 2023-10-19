# Library Manager Service

### How To Run

    mvn clean install 
    java -jar library-manager-service-1.0.0.jar

### API endpoints

    Borrow item with itemId
    POST
    api/v1/borrow/{userId}/{itemId}
    
    Borrow item with uniqueId
    POST
    api/v1/borrowUnique/{userId}/{uniqueId}

    Return item with uniqueId
    POST
    api/v1/return/{userId}/{uniqueId}

    Get inventory
    GET
    api/v1/getInventory

    Get all overdue items
    GET
    api/v1/getAllOverdueItems

    Get borrowed items for user
    GET
    api/v1/getBorrowedItemsForUser/{userId}

    Is item available 
    GET
    api/v1/isItemAvailable/{itemId}

### Approach

I have split the application out into 5 areas. 

1. Model: contains the classes which define how data is represented for each library item and each user. 
2. Controller: defines the REST endpoints used to interact with the application.
3. Repository: contains the interfaces that define how to interact with our data, along with implementations for in memory data structures.
4. Service: defines the service player where we implement the business logic for the library service. 
5. Util: defines data loaders used to load in data from the provided CSV files.

This approach is modular, which allows for ease of maintenance and clarity. It is also highly extensible. We use interfaces where it is beneficial, allowing us to change the implementations of our data structures and loaders in the future easily if required.

I have used thread-safe data structures, and also made sure to use appropriate thread safety techniques during write operations so that these operations are thread-safe.

### Assumptions
 
- As it is a small dataset, we will not have many users or items. Integer is used to store unique IDs. We also assume each ID we import is going to be unique. 
- A user can borrow as many items as they want. They can also borrow duplicate items.
- When we retrieve all items, usually we would want to be able to filter the results. Our design assumes this is not required as the dataset is small.
- Data is loaded using a @PostConstruct at start-up, meaning that data refreshes are not dynamic. 

