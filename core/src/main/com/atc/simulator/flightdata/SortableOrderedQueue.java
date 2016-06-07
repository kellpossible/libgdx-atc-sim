package com.atc.simulator.flightdata;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luke on 22/04/16.
 * A queue which is able to be sorted, and have items
 * inserted into it according to their value order.
 *
 * TODO: write unit test for this
 */
public abstract class SortableOrderedQueue<T extends Comparator<T>> extends ArrayList<T>{
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
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
        lock.lock();
        try {
            super.add(0, o);
            notEmpty.signal();
        }  finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * Append at the head of the queue
     * @param o
     */
    public void append(T o) {
        lock.lock();
        try {
            super.add(o);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Add object in order in the queue using its Comparable interface
     * The queue is orded as high at tail and low at head.
     * @param o
     */
    public void addInOrder(T o)
    {
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }
    }

    /**
     * Add an ArrayList of objects in order in the queue using its Comparable interface
     * The queue is orded as high at tail and low at head.
     * @param objects
     * @param valueObject
     */
    public void addAllInOrder(ArrayList<T> objects, T valueObject)
    {
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }

    }

    /**
     * Add all items in the collection to the tail of the queue
     * @param collection
     * @return
     */
    public boolean addAll(ArrayList<T> collection) {
        lock.lock();
        try {
            ListIterator li = collection.listIterator(collection.size());
            while(li.hasPrevious())
            {
                this.add((T) li.previous());
            }
            return true;
        } finally {
            lock.unlock();
            return false;
        }

    }

    /**
     * Add all items in the collection the position specified
     * @param collection
     * @return
     */
    public boolean addAll(int i, ArrayList<T> collection) {
        lock.lock();
        try {
            ListIterator li = collection.listIterator(collection.size());
            while(li.hasPrevious())
            {
                this.add(i, (T) li.previous());
            }
            return true;
        } finally {
            lock.unlock();
            return false;
        }
    }

    @Override
    public boolean isEmpty()
    {
        return super.isEmpty();
    }

    /**
     * Retrieves and removes the head of this queue, waiting if necessary until an element becomes available.
     * @return
     */
    public T take()
    {
        lock.lock();
        try {
            //wait until this queue is not empty
            while(isEmpty())
            {
                try {
                    notEmpty.await(); //wait and wake when queue is not empty
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the specified wait time if necessary for an element to become available.
     *
     * @param timeout
     * @return
     */
    public T poll(long timeout)
    {
        lock.lock();
        try {
            long startTime = System.currentTimeMillis();
            long endTime = startTime + timeout;
            long currentTime = startTime;
            if (isEmpty() && (currentTime - startTime) < timeout) {
                try {
                    notEmpty.await(endTime - currentTime, TimeUnit.MILLISECONDS);
                    currentTime = System.currentTimeMillis();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return poll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is currently empty.
     * @return
     */
    public T poll()
    {
        lock.lock();
        try {
            int size = this.size();
            if (size == 0)
            {
                return null;
            }

            int i = size-1;
            T retVal = this.get(i);
            this.remove(i);
            return retVal;
        } finally {
            lock.unlock();
        }
    }


    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return
     */
    public T peek() {
        lock.lock();
        try {
            int size = this.size();
            if (size == 0)
            {
                return null;
            }
            int i = size - 1;
            return this.get(i);
        } finally {
            lock.unlock();
        }
    }


    /**
     * Sort the queue using the Comparator interface in T
     * The queue is orded as high at tail and low at head.
     */
    protected abstract void sort();


    /**
     * Get the number of elements in this queue
     * @return
     */
    public int size()
    {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }

    }

}
