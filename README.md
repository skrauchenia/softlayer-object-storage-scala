Softlayer Object Storage Scala Client
==============================

This client is intended to using for accessing the Softlayer Object Storage API

Please be aware that currently client is under development


### Authentication

```scala
import objectstorage._

val connection = Connection(userName, apiKey)
```

### Create container (folder)

```scala
import objectstorage._

val connection = Connection(userName, apiKey)
val api = Api(connection)
val response = api create StorageContainer("test7")
```

### Delete container

```scala
val response = api delete StorageContainer("test7")
```
