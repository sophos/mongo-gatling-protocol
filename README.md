# Mongo Gatling Protocol
*Lets keep this simple*

## Summary
In order to test mongo with specific finds, updates, findAndModifies, etc you need
to test with your specific data model.  This protocol utilizes the db.runCommand 
method so that all types are mongo queries are easily consumed.  Expression language
is usable throughout the query so it can work with any kind of gatling feeder.

## Setup Protocol
```
# hostname is a csv list of mongo hosts
# username is the client user name
# password is the client password
# database is the authentication database
# readFromSecondary is a way to force reads to the secondary
val mongoProtocol = mongo.hostName("localhost:27017").userName("").password("").databaseName("admin")
    .readFromSecondary(false)
```

## Make a Query
```
val queryScenario = scenario("MakeAQuery")
    .feed(someKinOfFeeder) // If you want to use a feeder
    .exec(makeRequest("Make A Query",
      "{ find: \"collectionName\", filter: { $and: [ { name: { $eq: ${name} } }, { age: { $eq: ${age} } } ] }, limit: 1, singleBatch: true }"))
// assuming name and age are available in the feeder

```

## Putting it together in the setUp
```
setUp(
   // all of your scenarios or a scenarioBuilder List
  ).protocols(mongoProtocol)
```

## Project archive:

This repository has been formally archived, the project supporting it has been closed and no future updates will be made.
The code will remain available for a period of time until and unless the owner decides to withdraw it.
