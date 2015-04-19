
# NSMC JDBC Client Samples

## Prerequisites

- MongoDB Instance
- Apache Spark Instance
  - set up with examples from ...
- Shark Instance

## Configuring Shark to use NSMC

### Create a configuration file (say **nsmc.conf**)

    spark.nsmc.connection.host	localhost
    spark.nsmc.connection.port	27017
    # omit the next two lines if you're not using MongoDB authentication, 
    # otherwise provide ana ppropriate username and password
    spark.nsmc.user		      <yourMongoDBUserName>
    spark.nsmc.password		      yourMongoDBPassword
    spark.executor.extraClassPath     /home/spiro/Downloads/spark-mongodb-connector-assembly-0.5.2.jar


## Building the example

You can build the project using either Apache Maven or IntelliJ Idea.

## Running the example

You can run the **Demo** class main() method using either Apache Maven or IntelliJ Idea. To run it using Maven, use:

    mvn exec:java -Dexec.mainClass="Demo"

## Sample Output

## Relationship to NSMC Releases and Spark Releases

NSMC release prior to 0.5.1 and Sprak Releases prior to 1.3.0 are not supported. 

| Branch of this project | NSMC Release | Apache Spark Release |
|------------------------|--------------|----------------------|
| depends-v0.5.2 / master | 0.5.2 | 1.3.0 |

