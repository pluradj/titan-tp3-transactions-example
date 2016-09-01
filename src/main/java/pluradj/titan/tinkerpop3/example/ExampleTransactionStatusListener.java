package pluradj.titan.tinkerpop3.example;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Created by pluradj on 8/31/16.
 */
public class ExampleTransactionStatusListener implements Consumer<Transaction.Status> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ExampleTransactionStatusListener.class);

    private Graph graph;

    public ExampleTransactionStatusListener(Graph graph) {
        this.graph = graph;
    }
        /**
         * Performs this operation on the given argument.
         *
         * @param status the input argument
         */
        @Override
        public void accept(Transaction.Status status) {
            LOGGER.info("accept " + status + " " + graph.tx().toString());
        }

        // the other method on Consumer interface has a default implementation
        // default Consumer<T> andThen(Consumer<? super T> after);
}
