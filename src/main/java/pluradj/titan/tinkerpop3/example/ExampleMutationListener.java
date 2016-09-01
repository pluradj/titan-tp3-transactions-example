package pluradj.titan.tinkerpop3.example;

import org.apache.tinkerpop.gremlin.process.traversal.step.util.event.MutationListener;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.util.StringFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pluradj on 8/31/16.
 */
public class ExampleMutationListener implements MutationListener {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ExampleMutationListener.class);

    /**
     * Raised when a new {@link Vertex} is added.
     *
     * @param vertex the {@link Vertex} that was added
     */
    @Override
    public void vertexAdded(Vertex vertex) {
        LOGGER.info("vertexAdded " + StringFactory.vertexString(vertex));
    }

    /**
     * Raised after a {@link Vertex} was removed from the graph.
     *
     * @param vertex the {@link Vertex} that was removed
     */
    @Override
    public void vertexRemoved(Vertex vertex) {
        LOGGER.info("vertexRemoved " + StringFactory.vertexString(vertex));
    }

    /**
     * Raised after the property of a {@link Vertex} changed.
     *
     * @param element                 the {@link Vertex} that changed
     * @param oldValue
     * @param setValue                the new value of the property
     * @param vertexPropertyKeyValues
     */
    @Override
    public void vertexPropertyChanged(Vertex element, Property oldValue, Object setValue, Object... vertexPropertyKeyValues) {
        LOGGER.info("vertexPropertyChanged " + StringFactory.vertexString(element) + " " + StringFactory.propertyString(oldValue) + " " + setValue);
    }

    /**
     * Raised after a {@link VertexProperty} was removed from the graph.
     *
     * @param vertexProperty the {@link VertexProperty} that was removed
     */
    @Override
    public void vertexPropertyRemoved(VertexProperty vertexProperty) {
        LOGGER.info("vertexPropertyChanged " + ((vertexProperty.element() != null) ? StringFactory.vertexString(vertexProperty.element()) : null) + " " + StringFactory.propertyString(vertexProperty));
    }

    /**
     * Raised after a new {@link Edge} is added.
     *
     * @param edge the {@link Edge} that was added
     */
    @Override
    public void edgeAdded(Edge edge) {
        LOGGER.info("edgeAdded " + StringFactory.edgeString(edge));
    }

    /**
     * Raised after an {@link Edge} was removed from the graph.
     *
     * @param edge the {@link Edge} that was removed.
     */
    @Override
    public void edgeRemoved(Edge edge) {
        LOGGER.info("edgeRemoved " + StringFactory.edgeString(edge));
    }

    /**
     * Raised after the property of a {@link Edge} changed.
     *
     * @param element  the {@link Edge} that changed
     * @param oldValue
     * @param setValue the new value of the property
     */
    @Override
    public void edgePropertyChanged(Edge element, Property oldValue, Object setValue) {
        LOGGER.info("edgePropertyChanged " + ((element != null) ? StringFactory.edgeString(element) : null) + " " + ((oldValue != null) ? StringFactory.propertyString(oldValue) : null) + " " + setValue);
    }

    /**
     * Raised after an {@link Property} property was removed from an {@link Edge}.
     *
     * @param element
     * @param property the {@link Property} that was removed
     */
    @Override
    public void edgePropertyRemoved(Edge element, Property property) {
        LOGGER.info("edgePropertyRemoved " + ((element != null) ? StringFactory.edgeString(element) : null) + " " + ((property != null) ? StringFactory.propertyString(property) : null));
    }

    /**
     * Raised after the property of a {@link VertexProperty} changed.
     *
     * @param element  the {@link VertexProperty} that changed
     * @param oldValue
     * @param setValue the new value of the property
     */
    @Override
    public void vertexPropertyPropertyChanged(VertexProperty element, Property oldValue, Object setValue) {
        LOGGER.info("vertexPropertyPropertyChanged " + ((element != null && element.element() != null) ? StringFactory.vertexString(element.element()) : null) + " " + ((oldValue != null) ? StringFactory.propertyString(oldValue) : null) + " " + setValue);
    }

    /**
     * Raised after an {@link Property} property was removed from a {@link VertexProperty}.
     *
     * @param element
     * @param property the {@link Property} that removed
     */
    @Override
    public void vertexPropertyPropertyRemoved(VertexProperty element, Property property) {
        LOGGER.info("vertexPropertyPropertyRemoved " + ((element != null && element.element() != null) ? StringFactory.vertexString(element.element()) : null) + " " + ((element != null) ? StringFactory.propertyString(element) : null) + " " + ((property != null) ? StringFactory.propertyString(property) : null));
    }
}
