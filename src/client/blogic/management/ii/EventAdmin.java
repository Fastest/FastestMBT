package client.blogic.management.ii;

import java.util.*;
import java.util.concurrent.locks.*;

import common.repository.AbstractRepository;
import common.repository.ConcreteRepository;
import common.repository.AbstractIterator;
import client.presentation.ClientUI;
import client.blogic.management.ii.events.Event_;

import java.net.URL;

/**
 * Administrates the implicit invocation events of the system. Allows the reading
 * of the implicit invocation configuration's file to build the event table. Also,
 * the instances (unique, in fact) of this class announce events and call the
 * methods interested in the announced events.
 * @author Pablo Rodriguez Monetti
 */
public class EventAdmin {

    private ClientUI myClientUI;
    private static EventAdmin eventAdmin;
    private AbstractRepository<Subscriptor> eventTable;
    public Lock myLock;
    Map<String, IIComponent> componentMap;

    /**
     * Creates instances of EventAdmin. It can only be called from the 
     * getInstance() method.
     * @param file the configuration file
     * @param clientUI
     */
    private EventAdmin(ClientUI clientUI) {
        this.myClientUI = clientUI;
        this.myLock = new ReentrantLock();
        componentMap = new HashMap<String, IIComponent>();
        readFile();
    }

    /**
     * Gets the unique instance of this class, after creating it.
     * @param file
     * @param clientUI
     * @return
     * @throws java.lang.IllegalAccessException if the instance was created 
     * before.
     */
    public static EventAdmin getInstance(ClientUI clientUI)
            throws IllegalAccessException {

        if (eventAdmin != null) {
            throw new IllegalAccessException();
        } else {
            eventAdmin = new EventAdmin(clientUI);
        }
        return eventAdmin;
    }

    /**
     * Gets the unique instance of this class.
     * @return
     * @throws java.lang.IllegalAccessException if the instance was not created
     * before.
     */
    public static EventAdmin getInstance()
            throws IllegalAccessException {

        if (eventAdmin == null) {
            throw new IllegalAccessException();
        } else {
            return eventAdmin;
        }
    }

    /**
     * Annnounces an implicit invocation event, calling every method interested
     * in the announced event.
     * @param event_
     */
    public void announceEvent(Event_ event_) {
        myLock.lock();
        AbstractIterator<Subscriptor> it = eventTable.createIterator();
        String eventName = event_.getEventName();
        while (it.hasNext()) {
            Subscriptor subscriptor = it.next();
            String subscriptorEventName = subscriptor.getEventName();
            if (subscriptorEventName.equals(eventName)) {
                IIOrder iIOrder = (IIOrder) subscriptor.getIIOrder().clone();
                iIOrder.setExplicitParam(event_);
                // We run the IIOrder in a new thread
                (new Thread(iIOrder, "")).start();
            }
        }

        myLock.unlock();
    }

    /**
     * Parse the event's configuration file and fills the event table.
     * 
     */
    private void readFile() {

        eventTable = new ConcreteRepository<Subscriptor>();
        Properties props = new Properties();

        try {
            URL url = Event_.class.getResource("events.properties");
            if (url != null) {
                props.load(url.openStream());
                Set<String> nameSet = props.stringPropertyNames();

                Iterator<String> namesIt = nameSet.iterator();
                while (namesIt.hasNext()) {
                    String eventName = namesIt.next();
                    String componentNameList = (String) props.get(eventName);
                    String methodName = "manageEvent";
                    if (componentNameList.startsWith("(")
                            && componentNameList.endsWith(")")) {
                        componentNameList = componentNameList.substring(1,
                                componentNameList.length() - 1);

                        String componentNames[] = componentNameList.split(";");
                        for (int i = 0; i < componentNames.length; i++) {
                            String componentName = componentNames[i];
                            IIComponent component_ = componentMap.get(componentName);
                            if (component_ == null) {
                                Class componentClass = Class.forName(componentName);
                                component_ = (IIComponent) componentClass.newInstance();
                                componentMap.put(componentName, component_);
                            }
                            component_.setMyClientUI(myClientUI);
                            // We extract the method-to-invoke name from the file
                            IIOrder iIOrder = new IIOrder(component_, methodName);
                            //We build a new subscriptor and add it to the event table
                            Subscriptor subscriptor = new Subscriptor(eventName, iIOrder);
                            eventTable.addElement(subscriptor);
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }
}
