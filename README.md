
# NSMC JDBC Client Samples

This project demonstrates how to use the [Natife Spark MongoDB Conenctor (NSMC)](https://github.com/spirom/spark-mongodb-connector) from a Java/JDBC program via the Apache Hive JDBC driver and Apache Spark's Thrift JDBC server.  

## Prerequisites

- [MongoDB Instance](http://docs.mongodb.org/manual/installation/)
- [Apache Spark Instance](https://spark.apache.org/downloads.html)
- Native Spark MongoDB Connector (NSMC) assembly JAR  [available here](https://github.com/spirom/spark-mongodb-connector/releases)
  - Set up with the [MongoDB example collection from the NSMC examples](http://github.com/spirom/spark-mongodb-examples) -- only necessary to run the class `PopulateTestCollection`.
- Apache Spark Thrift [JDBC Server instance](https://spark.apache.org/docs/latest/sql-programming-guide.html#running-the-thrift-jdbcodbc-server)

## Configuring the Thrift JDBC server to use NSMC

### Create a configuration file (say **nsmc.conf**)

    spark.nsmc.connection.host	    localhost
    spark.nsmc.connection.port	    27017
    # omit the next two lines if you're not using MongoDB authentication, 
    # otherwise provide ana ppropriate username and password
    spark.nsmc.user                 <yourMongoDBUserName>
    spark.nsmc.password             <yourMongoDBPassword>
    spark.executor.extraClassPath   <downloads>/spark-mongodb-connector-assembly-0.5.3.jar

### Running the Thrift server

You need to let the Thrift JDBC server know the location of the Spark master, the above configuration file, and also the path 
tp the NSMC assmebly file. 

    <apache spark install path>/sbin/start-thriftserver.sh \
      --master <your masster url> \
      --driver-class-path <downloads>/spark-mongodb-connector-assembly-0.5.3.jar \
      --properties-file <your vonfig location>/nsmc.conf 

## Building the example

You can build the project using either Apache Maven or IntelliJ Idea.

    mvn compile

## Running the example

You can run the **Demo** class main() method using either Apache Maven or IntelliJ Idea. To run it using Maven, use:

    mvn exec:java -Dexec.mainClass="Demo"

## Sample Output

    *** Cleaning up

    *** Registering the table

    *** Showing tables

    Running: show tables
    datatable, true

    Running: describe dataTable
    _id	string
    billingAddress : struct<state:string,zip:string>
    custid : string
    discountCode : int
    orders : array<struct<itemid:string,orderid:string,quantity:int>>
    shippingAddress : struct<state:string,zip:string>

    Running: DELECT custid, billingAddress, orders FROM dataTable

    *** Column metadata:
    total of 3 columns
    Column 1 : custid
      Label : custid
      Class Name : java.lang.String
      Type : 12
      Type Name : string
    Column 2 : billingAddress
      Label : billingAddress
      Class Name : java.lang.String
      Type : 2002
      Type Name : struct
    Column 3 : orders
      Label : orders
      Class Name : java.lang.String
      Type : 2003
      Type Name : array

    *** Data:
    Row 1
      Column 1 : 1001
      Column 2 : {"state":"NV","zip":"89150"}
      Column 3 : [{"itemid":"A001","orderid":"1000001","quantity":175},
                  {"itemid":"A002","orderid":"1000002","quantity":20}]
    Row 2
      Column 1 : 1002
      Column 2 : {"state":"CA","zip":"92093"}
      Column 3 : [{"itemid":"B012","orderid":"1000002","quantity":200}]
    Row 3
      Column 1 : 1003
      Column 2 : {"state":"AZ","zip":"85014"}
      Column 3 : [{"itemid":"A001","orderid":"1000003","quantity":175},
                  {"itemid":"B001","orderid":"1000004","quantity":10},
                  {"itemid":"A060","orderid":"1000005","quantity":12}]
    Row 4
      Column 1 : 1004
      Column 2 : null
      Column 3 : null

    Running: SELECT custid, billingAddress.zip FROM dataTable

    *** Column metadata:
    totoal of 2 columns
    Column 1 : custid
      Label : custid
      Class Name : java.lang.String
      Type : 12
      Type Name : string
    Column 2 : zip
      Label : zip
      Class Name : java.lang.String
      Type : 12
      Type Name : string

    *** Data:
    Row 1
      Column 1 : 1001
      Column 2 : 89150
    Row 2
      Column 1 : 1002
      Column 2 : 92093
    Row 3
      Column 1 : 1003
      Column 2 : 85014
    Row 4
      Column 1 : 1004
      Column 2 : null

    Running: SELECT custid, o.orderid, o.itemid, o.quantity 
             FROM dataTable LATERAL VIEW explode(orders) t AS o

    *** Column metadata:
    total of 4 columns
    Column 1 : custid
      Label : custid
      Class Name : java.lang.String
      Type : 12
      Type Name : string
    Column 2 : orderid
      Label : orderid
      Class Name : java.lang.String
      Type : 12
      Type Name : string
    Column 3 : itemid
      Label : itemid
      Class Name : java.lang.String
      Type : 12
      Type Name : string
    Column 4 : quantity
      Label : quantity
      Class Name : java.lang.Integer
      Type : 4
      Type Name : int

    *** Data:
    Row 1
      Column 1 : 1001
      Column 2 : 1000001
      Column 3 : A001
      Column 4 : 175
    Row 2
      Column 1 : 1001
      Column 2 : 1000002
      Column 3 : A002
      Column 4 : 20
    Row 3
      Column 1 : 1002
      Column 2 : 1000002
      Column 3 : B012
      Column 4 : 200
    Row 4
      Column 1 : 1003
      Column 2 : 1000003
      Column 3 : A001
      Column 4 : 175
    Row 5
      Column 1 : 1003
      Column 2 : 1000004
      Column 3 : B001
      Column 4 : 10
    Row 6
      Column 1 : 1003
      Column 2 : 1000005
      Column 3 : A060
      Column 4 : 12

    *** Dropping the table

    *** Showing tables


## Relationship to NSMC Releases and Spark Releases

NSMC release prior to 0.5.2 and Sprak Releases prior to 1.3.0 are not supported. 

| Branch of this project | NSMC Release | Apache Spark Release |
|------------------------|--------------|----------------------|
| depends-v0.5.2 / master | 0.5.2 | 1.3.0 |

