package pluradj.titan.tinkerpop3.example;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.graphdb.database.StandardTitanGraph;
import com.thinkaurelius.titan.graphdb.transaction.StandardTitanTx;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.event.MutationListener;
import org.apache.tinkerpop.gremlin.process.traversal.strategy.decoration.EventStrategy;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.AbstractTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class JavaExample {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(JavaExample.class);

    private StandardTitanGraph graph;
    private GraphTraversalSource g;
    private Consumer<Transaction.Status> listener;
    private MutationListener mutationListener;
    private EventStrategy eventStrategy;

    public JavaExample() {
        String dataDir = "./data";
        deleteDataDir(dataDir);
        graph = (StandardTitanGraph) TitanFactory.open("berkeleyje:" + dataDir);
        listener = new ExampleTransactionStatusListener(graph);
        mutationListener = new ExampleMutationListener();

        // adding a transaction listener to listen for commits and rollbacks
        // (class hierarchy) TitanBlueprintsGraph.GraphTransaction (C) -> AbstractTransaction (A) -> Transaction (I)
        // this thing is some sort of transaction container...
        Transaction tx = (AbstractTransaction) graph.tx();
        LOGGER.info("tx add listener " + tx.toString());
        tx.addTransactionListener(listener);

        // adding mutation listener as a graph traversal event strategy
        eventStrategy = EventStrategy.build().addListener(mutationListener).create();
        g = GraphTraversalSource.build().with(eventStrategy).create(graph);
        LOGGER.info("tx rollback " + tx.toString());
        graph.tx().rollback();
    }

    private void deleteDataDir(String dataDir) {
        try {
            Path rootPath = Paths.get(dataDir);
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
        }
    }

    private StandardTitanTx startTx() {
        // (class hierarchy) StandardTitanTx (C) -> TitanBlueprintsTransaction (A) -> TitanTransaction (I) -> TitanGraphTransaction (I) -> Graph (I)
        // (call stack) TitanBlueprintsGraph.getCurrentThreadTx() -> TitanBlueprintsGraph.getAutoStartTx()
        StandardTitanTx stx = (StandardTitanTx) graph.getCurrentThreadTx();
        LOGGER.info("StandardTitanTx " + (stx instanceof Transaction));
        return stx;
    }

    public void init() {
        LOGGER.info("init " + graph.tx().toString());
        graph.tx().rollback();

        // see org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory.generateClassic()
        // need to leverage GraphTraversalSource for mutation listener to kick in
        final Vertex marko = g.addV().property("name", "marko").property("age", 29).next();
        final Vertex vadas = g.addV().property("name", "vadas").property("age", 27).next();
        final Vertex lop = g.addV().property("name", "lop").property("lang", "java").next();
        final Vertex josh = g.addV().property("name", "josh").property("age", 32).next();
        final Vertex ripple = g.addV().property("name", "ripple").property("lang", "java").next();
        final Vertex peter = g.addV().property("name", "peter").property("age", 35).next();
        // note: mid-traversal V() is not available in Titan 1.0/TP 3.0.1
        g.V(marko.id()).as("marko").V(vadas.id()).as("vadas").addE("knows").from("marko").to("vadas").property("weight", 0.5f).iterate();
        g.V(marko.id()).as("marko").V(josh.id()).as("josh").addE("knows").from("marko").to("josh").property("weight", 1.0f).iterate();
        g.V(marko.id()).as("marko").V(lop.id()).as("lop").addE("created").from("marko").to("lop").property("weight", 0.4f).iterate();
        g.V(josh.id()).as("josh").V(ripple.id()).as("ripple").addE("created").from("josh").to("ripple").property("weight", 1.0f).iterate();
        g.V(josh.id()).as("josh").V(lop.id()).as("lop").addE("created").from("josh").to("lop").property("weight", 0.4f).iterate();
        g.V(peter.id()).as("peter").V(lop.id()).as("lop").addE("created").from("peter").to("lop").property("weight", 0.2f).iterate();

        graph.tx().commit();
    }

    public void traverse() {
        LOGGER.info("traverse " + graph.tx().toString());
        graph.tx().rollback();

        List list = g.V().has("name", "marko").valueMap(true).toList();
        LOGGER.info(list.toString());

        list = g.V().has("name", "peter").outE().valueMap(true).toList();
        LOGGER.info(list.toString());

        graph.tx().commit();
    }

    public static void main(String[] args) {
        JavaExample ex = new JavaExample();
        ex.init();
        ex.traverse();
        System.exit(0);
    }
}
