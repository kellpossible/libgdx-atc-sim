package com.atc.simulator.flightdata;
import java.util.*;

/**
 * Created by luke on 22/04/16.
 * A queue which is able to be sorted, and have items
 * inserted into it according to their value order.
 *
 * TODO: write unit test for this
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
    public synchronized boolean add(T o) {
        super.add(0, o);
        this.notifyAll();
        return true;
    }

    /**
     * Append at the head of the queue
     * @param o
     * @return
     */
    public synchronized void append(T o) {
        super.add(o);
        this.notifyAll();
    }

    /**
     * Add object in order in the queue using its Comparable interface
     * The queue is orded as high at tail and low at head.
     * @param o
     */
    public synchronized void addInOrder(T o)
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
    public synchronized void addAllInOrder(ArrayList<T> objects, T valueObject)
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
    public synchronized boolean addAll(ArrayList<T> collection) {
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
    public synchronized boolean addAll(int i, ArrayList<T> collection) {
        ListIterator li = collection.listIterator(collection.size());
        while(li.hasPrevious())
        {
            this.add(i, (T) li.previous());
        }
        return true;
    }

    @Override
    public synchronized boolean isEmpty()
    {
        return super.isEmpty();
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
     * @return
     */
    public synchronized T take()
    {
        //wait until this queue is not empty
        while(isEmpty())
        {
            try {
                this.wait(); //wait and wake on any events which occur to this queue
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return poll();
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the specified wait time if necessary for an element to become available.
     *
     * @param timeout
     * @return
     */
    public synchronized T poll(long timeout)
    {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeout;
        long currentTime = startTime;
        if (isEmpty() && (currentTime-startTime) < timeout)
        {
            try {
                this.wait(endTime-currentTime);
                currentTime = System.currentTimeMillis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return poll();
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is currently empty.
     * @return
     */
    public synchronized T poll()
    {
        int size = this.size();
        if (size == 0)
        {
            return null;
        }

        int i = size-1;
        T retVal = this.get(i);
        this.remove(i);
        this.notifyAll();
        return retVal;
    }


    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return
     */
    public synchronized T peek() {
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
     * ensure that this is always synchronised made it final
     */
    public synchronized final void sort()
    {
        doSort();
    }


    /**
     * Sort the queue using the Comparator interface in T
     * The queue is orded as high at tail and low at head.
     */
    protected abstract void doSort();


    /**
     * Get the number of elements in this queue
     * @return
     */
    public synchronized int size()
    {
        return super.size();
    }

}
