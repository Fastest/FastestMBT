/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.blogic.testing.ttree.tactics;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;

/**
 * The instance of this class (which is a singleton) contains a number of
 * instances of StdPartition.
 * @author Pablo Rodriguez Monetti
 */
public class StdPartitionsControl implements AbstractRepository<StdPartition> {

    private static StdPartitionsControl stdPartitionsControl;
    private AbstractRepository<StdPartition> repository;

    /**
     * Creates intances of StdPartitionsControl.
     */
    private StdPartitionsControl() {
        repository = new ConcreteRepository<StdPartition>();
    }

    /**
     * Gets the instance of this class, creating it if necessary.
     * @return
     */
    public static StdPartitionsControl getInstance() {
        if (stdPartitionsControl == null) {
            stdPartitionsControl = new StdPartitionsControl();
        }
        return stdPartitionsControl;
    }

    /**
     * Creates an iterator in order to be possible the traversal along this
     * structure.
     * @return
     */
    public AbstractIterator<StdPartition> createIterator() {
        return repository.createIterator();
    }

    /**
     * Add a new instance of StdPartition to this object.
     * @param stdPartition
     */
    public void addElement(StdPartition stdPartition) {
        repository.addElement(stdPartition);
    }
}
