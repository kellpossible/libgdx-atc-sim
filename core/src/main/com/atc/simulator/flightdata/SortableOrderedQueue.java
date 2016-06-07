package com.atc.simulator.flightdata;
import java.util.*;

/**
 * Created by luke on 22/04/16.
 * A queue which is able to be sorted, and have items
 * inserted into it according to their value order.
 * Not thread safe
 *
 * TODO: write unit test for this
 * TODO: make a blocking/threadsafe version of this
 */
public abstract class SortableOrderedQueue<T extends Comparator<T>> extends ArrayList<T>{

    public SortableOrderedQueue()
    {
        super();
    }

    /**
     * Insert at the tail of the Queue
     * @param o
     * @return
     */
    @Override
    public boolean add(T o) {
        super.add(0, o);
        return true;
    }

    /**
     * Append at the head of the queue
     * @param o
     * @return
     */
    public void append(T o) {
        super.add(o);
    }

    /**
     * Add object in order in the queue using its Comparable interface
     * The queue is orded as high at tail and low at head.
     * @param o
     */
    public void addInOrder(T o)
    {
        int size = this.size();
        if (size == 0)
        {
            this.add(o);
            return;
        }

        for(int i=0; i<size; i++)
        {
            T e = get(i);
            if (o.compare(o, e) > 0) {
                this.add(i, o);
                return;
            }
        }

        this.append(o);
        return;
    }

    /**
     * Add an ArrayList of objects in order in the queue using its Comparable interface
     * The queue is orded as high at tail and low at head.
     * @param objects
     * @param valueObject
     */
    public void addAllInOrder(ArrayList<T> objects, T valueObject)
    {
        int size = this.size();
        if (size == 0)
        {
            this.addAll(objects);
            return;
        }

        for(int i=0; i<size; i++)
        {
            T e = get(i);
            if (valueObject.compare(valueObject, e) > 0) {
                this.addAll(i, objects);
                return;
            }
        }

        this.addAll(objects);
        return;
    }

    /**
     * Add all items in the collection to the tail of the queue
     * @param collection
     * @return
     */
    public boolean addAll(ArrayList<T> collection) {
        ListIterator li = collection.listIterator(collection.size());
        while(li.hasPrevious())
        {
            this.add((T) li.previous());
        }
        return true;
    }

    /**
     * Add all items in the collection the position specified
     * @param collection
     * @return
     */
    public boolean addAll(int i, ArrayList<T> collection) {
        ListIterator li = collection.listIterator(collection.size());
        while(li.hasPrevious())
        {
            this.add(i, (T) li.previous());
        }
        return true;
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     * @return
     */
    public T poll()
    {
        int size = this.size();
        if (size == 0)
        {
            return null;
        }

        int i = size-1;
        T retVal = this.get(i);
        this.remove(i);
        return retVal;
    }


    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return
     */
    public T peek() {
        int size = this.size();
        if (size == 0)
        {
            return null;
        }
        int i = size - 1;
        return this.get(i);
    }


    /**
     * Sort the queue using the Comparator interface in T
     * The queue is orded as high at tail and low at head.
     */
    public abstract void sort();

}
