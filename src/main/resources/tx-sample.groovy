import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.EventStrategy
import pluradj.titan.tinkerpop3.example.ExampleTransactionStatusListener
import pluradj.titan.tinkerpop3.example.ExampleMutationListener

// an init script that returns a Map allows explicit setting of global bindings.
def globals = [:]

def txsListener = new ExampleTransactionStatusListener(graph)
def mutListener = new ExampleMutationListener()
def evtStrategy = EventStrategy.build().addListener(mutListener).create()

// defines a sample LifeCycleHook that prints some output to the Gremlin Server console.
// note that the name of the key in the "global" map is unimportant.
globals << [hook : [
        onStartUp: { ctx ->
            ctx.logger.info("Executed once at startup of Gremlin Server.")
            ctx.logger.info("Added transaction listener.")
            graph.tx().addTransactionListener(txsListener)
        },
        onShutDown: { ctx ->
            ctx.logger.info("Executed once at shutdown of Gremlin Server.")
            ctx.logger.info("Removed transaction listener.")
            graph.tx().removeTransactionListener(txsListener)
        }
] as LifeCycleHook]

// define the default TraversalSource to bind queries to - this one will be named "g".
// build it with an EventStrategy with the mutation listener.
globals << [g : GraphTraversalSource.build().with(evtStrategy).create(graph)]
