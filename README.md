## Introduction 

This project is a Java demo project meant to demonstrate the implementation
and integration of different aspects of GraphQL in a Spring Boot project (using [Spring GraphQL](https://github.com/spring-projects/spring-graphql)):

* Schema to controller wiring
* Schema documentation
* Fetching of data, using different DTO and entity models (queries)
* Efficient fetching of relational data by preventing innecessary selections and joins
* Prevention of N+1 problem when querying data
* Exception handling
* Usage of extended, more complex data types
* Creation and persistence of new data (mutations)

![Fetching delivery orders](https://user-images.githubusercontent.com/23218676/156672772-57248fcb-5353-4a57-8859-882e046def22.gif)

![Fetching and creating items](https://user-images.githubusercontent.com/23218676/156672784-e8c21f50-42ea-4001-8bdc-7d80afe244bd.gif)

## Installation and usage

1. Clone the project: `git clone `
2. Navigate to directory containing the cloned project
3. Install the dependencies, build and run:
`.\gradlew bootRun -D org.gradle.java.home='<your java 17 path>'`.
If you're using another JDK version, please change the Java target and source
compatibility properties in `build.gradle.kts`
4. Navigate to URL `http://localhost:8080/graphiql`
5. You can now query all delivery orders for example:
```graphql
{
  deliveryOrders {
    id
    customerName
    address
    deliveryOrderLines {
      item {
        name
        description
      }
      quantity
    }
  }
}
```



## Project structure

(inside `src/main`)

```
.
├── java                    
│   └── be.nazarimilad.springbootgraphqlquerydsldemo
|       ├── config              # Configuration settings
│       ├── controller          # Controllers, request entry points
│       ├── dto                 # DTO's, models of data to return to the user
│       ├── entity              # Entities, models of data used internally for business logic
│       ├── mapper              # Mappers, responsible for mapping DTO <> entity
│       └── repository          # Classes responsible for communication with database
└── resources
    ├── application.yml         # Configuration for the application
    └── graphql                 # GraphQL schema's
```

## Data flow

Let's take the use case of querying all delivery orders with their delivery order lines, as an example.

1. User queries data using the following query:
```graphql
{
  deliveryOrders {
    id
    customerName
    address
    deliveryOrderLines {
      quantity
    }
  }
}
```
2. In `DeliveryOrderController`, through query mapping,
the function `deliveryOrders` is called.
   1. The selection of all the selected fields (id, customer name, etc) is captured
   2. This field selection is passed to a function `findDeliveryOrderList`
      of repository `DeliveryOrderRepository`
      1. This delivery order repository creates a new QueryDSL JPA query meant to select
         only the direct fields of the delivery order: id, customer name and address
      2. The repository checks if there are indirect properties in the selected fields.
         This is currently the case: the delivery order lines. So a join is added and the expansion
         of the query (more selectied fields and possibly more joins) is delegated to the repository
         responsible for delivery order lines: `DeliveryOrderLineRepository`. Note that
         since the `item` property of the delivery order lines is not queried, the `ItemRepository`
         will not be used, nor will there be any joins with the Item table be made
      3. The completed query is executed and the data is fetched and returned
   3. The fetched delivery order line entity objects are mapped into DTO objects and returned
3. User receives the result of the query

## Data model

* A delivery order has one or more delivery order lines
* Each delivery order line has one delivery order, and one item
* An item can belong to multiple delivery order lines
