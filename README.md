Titan - TinkerPop3 Transaction Example
======================================

Simple Java example demonstrating transaction listeners with Titan 1.1.0-SNAPSHOT and TinkerPop 3.1.1.

## Prerequisites

* Java 8.0 Update 40 or higher
* Apache Maven 3.3.x
* Titan 1.1.0-SNAPSHOT (`titan11` branch)

```
git clone https://github.com/thinkaurelius/titan.git
cd titan
git checkout -b titan11 -t origin/titan11
mvn clean install -DskipTests=true -Dgpg.skip=true -Paurelius-release
unzip titan-dist/titan-dist-hadoop-2/target/titan-1.1.0-SNAPSHOT-hadoop2.zip
cd titan-1.1.0-SNAPSHOT-hadoop2
export TITAN_HOME=$PWD
```

## Building

```
git clone https://github.com/pluradj/titan-tp3-transactions-example.git
cd titan-tp3-transactions-example
mvn clean package
```

## Running standalone Java example

```
mvn exec:java -Dexec.mainClass="pluradj.titan.tinkerpop3.example.JavaExample"
```

## Running with Titan Server and remote console

Start the server

```
cp ./src/main/resources/tx-gremlin-server.yaml $TITAN_HOME/conf/gremlin-server/
cp ./src/main/resources/tx-sample.groovy $TITAN_HOME/scripts/
cp ./target/transactions-example-0.1.jar $TITAN_HOME/lib/

cd $TITAN_HOME
./bin/gremlin-server.sh ./conf/gremlin-server/tx-gremlin-server.yaml
```

Output in the server logs

```
0    [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - 
         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----

118  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Configuring Gremlin Server from ./conf/gremlin-server/tx-gremlin-server.yaml
719  [main] INFO  com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration  - Set default timestamp provider MICRO
778  [main] INFO  com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration  - Generated unique-instance-id=c0a8000c40598-pluradj-mbp-local1
806  [main] INFO  com.thinkaurelius.titan.diskstorage.Backend  - Initiated backend operations thread pool of size 16
873  [main] INFO  com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog  - Loaded unidentified ReadMarker start time 2016-09-01T05:05:46.796Z into com.thinkaurelius.titan.diskstorage.log.kcvs.KCVSLog$MessagePuller@d771cc9
874  [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Graph [graph] was successfully configured via [conf/gremlin-server/titan-berkeleyje-server.properties].
874  [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized Gremlin thread pool.  Threads in pool named with pattern gremlin-*
1124 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded nashorn ScriptEngine
1450 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.ScriptEngines  - Loaded gremlin-groovy ScriptEngine
2073 [main] INFO  org.apache.tinkerpop.gremlin.groovy.engine.GremlinExecutor  - Initialized gremlin-groovy ScriptEngine with scripts/tx-sample.groovy
2073 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - Initialized GremlinExecutor and configured ScriptEngines.
2080 [main] INFO  org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor  - A GraphTraversalSource is now bound to [g] with graphtraversalsource[standardtitangraph[berkeleyje:db/berkeley], standard]
2097 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the standard OpProcessor.
2098 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the control OpProcessor.
2100 [main] INFO  org.apache.tinkerpop.gremlin.server.op.OpLoader  - Adding the session OpProcessor.
2124 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executing start up LifeCycleHook
2140 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executed once at startup of Gremlin Server.
2170 [main] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Added transaction listener.
2246 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2246 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+gryo-stringd with org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0
2359 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/vnd.gremlin-v1.0+json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0
2360 [main] INFO  org.apache.tinkerpop.gremlin.server.AbstractChannelizer  - Configured application/json with org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0
2430 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Gremlin Server configured with worker thread pool of 1, gremlin pool of 8 and boss thread pool of 1.
2431 [gremlin-server-boss-1] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Channel started at port 8182.
```

Start the console

```
cd $TITAN_HOME
./bin/gremlin.sh

         \,,,/
         (o o)
-----oOOo-(3)-oOOo-----
gremlin> :remote connect tinkerpop.server conf/remote.yaml
==>Connected - localhost/127.0.0.1:8182
gremlin> :> v=g.addV().property("name","jason").next(); w=g.addV().property("name","scott").next(); e=g.V(v.id()).as("v").V(w.id()).as("w").addE("knows").from("v").to("w").property("since",2015).next(); [v, w, e]
==>v[4176]
==>v[4232]
==>e[16y-380-1lh-39k][4176-knows->4232]
gremlin> :q
```

Output in the server logs

```
18049 [gremlin-server-exec-1] INFO  pluradj.titan.tinkerpop3.example.ExampleMutationListener  - vertexAdded v[4176]
18719 [gremlin-server-exec-1] INFO  pluradj.titan.tinkerpop3.example.ExampleMutationListener  - vertexAdded v[4232]
18744 [gremlin-server-exec-1] INFO  pluradj.titan.tinkerpop3.example.ExampleMutationListener  - edgeAdded e[16y-380-1lh-39k][4176-knows->4232]
18786 [gremlin-server-exec-1] INFO  pluradj.titan.tinkerpop3.example.ExampleTransactionStatusListener  - accept COMMIT com.thinkaurelius.titan.graphdb.tinkerpop.TitanBlueprintsGraph$GraphTransaction@5bb7643d
```

Stop the server with `Control+C`

```
^C275196 [gremlin-server-shutdown] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Shutting down OpProcessor[]
275196 [gremlin-server-shutdown] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Shutting down OpProcessor[session]
275199 [gremlin-server-shutdown] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Shutting down OpProcessor[control]
275199 [gremlin-server-shutdown] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Shutting down thread pools.
275201 [gremlin-server-stop] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executing shutdown LifeCycleHook
275201 [gremlin-server-stop] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Executed once at shutdown of Gremlin Server.
275201 [gremlin-server-stop] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Removed transaction listener.
277413 [gremlin-server-stop] WARN  com.thinkaurelius.titan.graphdb.database.StandardTitanGraph  - Failed to remove shutdown hook
java.lang.IllegalStateException: Shutdown in progress
	at java.lang.ApplicationShutdownHooks.remove(ApplicationShutdownHooks.java:82)
	at java.lang.Runtime.removeShutdownHook(Runtime.java:239)
	at com.thinkaurelius.titan.graphdb.database.StandardTitanGraph.removeHook(StandardTitanGraph.java:241)
	at com.thinkaurelius.titan.graphdb.database.StandardTitanGraph.close(StandardTitanGraph.java:175)
	at org.apache.tinkerpop.gremlin.server.GremlinServer.lambda$null$65(GremlinServer.java:307)
	at java.util.concurrent.ConcurrentHashMap.forEach(ConcurrentHashMap.java:1597)
	at org.apache.tinkerpop.gremlin.server.GremlinServer.lambda$stop$66(GremlinServer.java:304)
	at java.lang.Thread.run(Thread.java:745)
277414 [gremlin-server-stop] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Closed Graph instance [graph]
277414 [gremlin-server-stop] INFO  org.apache.tinkerpop.gremlin.server.GremlinServer  - Gremlin Server - shutdown complete
```

## Issues

* Transaction status listener is not called when using Titan Server
  * Caused by the transaction listener list initialization in [AbstractThreadLocalTransaction](https://github.com/apache/tinkerpop/blob/3.1.1-incubating/gremlin-core/src/main/java/org/apache/tinkerpop/gremlin/structure/util/AbstractThreadLocalTransaction.java#L54-L59).
  * There are 2 different threads in action during the Titan Server initialization. 1: the Groovy startup script
  evaluation which adds the listener, and 2: the server itself handles the transaction commit or rollback. Both threads
  end up calling on the ThreadLocal `initialValue()` which returns `new ArrayList()` and effectively wipes out the
  listener. 
  * Fix is to initialize the list once. Build `gremlin-core` for `3.1.1-incubating` with the fix and install it into
  the local Maven repository.
  
```
    protected final ThreadLocal<List<Consumer<Transaction.Status>>> transactionListeners = new ThreadLocal<List<Consumer<Transaction.Status>>>() {
        private List<Consumer<Transaction.Status>> list = new ArrayList<>();
        @Override
        protected List<Consumer<Transaction.Status>> initialValue() {
            return list;
        }
    };
```
