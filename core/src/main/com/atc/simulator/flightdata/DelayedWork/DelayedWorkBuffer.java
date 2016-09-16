package com.atc.simulator.flightdata.DelayedWork;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

/**
 * Created by luke on 15/09/16.
 *
 * A buffer/queue of delayed work items.
 * When the buffer is full, new items replace the tail item of queue.
 *
 * @author Luke Frisken
 */
public class DelayedWorkBuffer {
    private Queue<DelayedWorkQueueItem> buffer;
    private DelayedWorkBufferListener listener;

    private int bufferLength;

    /**
     * Constructor for {@link DelayedWorkBuffer}
     * @param bufferLength max length/size of the buffer
     * @param listener listener which will be listening to this buffer.
     */
    public DelayedWorkBuffer(int bufferLength, DelayedWorkBufferListener listener)
    {
        buffer = new Queue<DelayedWorkQueueItem>(bufferLength);
        this.listener = listener;
        this.bufferLength = bufferLength;
    }

    /**
     * Add a new item to this buffer. If the buffer is full,
     * it will replace the final item on the tail of the queue.
     * @param item item to be added
     * @param triggerListener whether or not to trigger the listener upon adding the new item
     */
    public void add(DelayedWorkQueueItem item, boolean triggerListener)
    {
        if (buffer.size == bufferLength)
        {
            DelayedWorkQueueItem replacedItem = buffer.removeLast();

            if (triggerListener)
            {
                listener.onItemReplaced(replacedItem, item);
            }

        } else {
            if (triggerListener)
            {
                listener.onItemAdded(item);
            }
        }
        buffer.addLast(item);
    }

    /**
     * Add a new item to this buffer. If the buffer is full,
     * it will replace the final item on the tail of the queue.
     * @param item item to be added
     */
    public void add(DelayedWorkQueueItem item)
    {
        this.add(item, true);
    }

    /**
     * Remove an item from the buffer.
     * @param item item to be removed
     * @param triggerListener whether or not to trigger the listener upon removing the item
     * @return whether or not an item was in fact removed
     */
    public boolean remove(DelayedWorkQueueItem item, boolean triggerListener)
    {
        boolean removed = buffer.removeValue(item, true);
        if (triggerListener && removed)
        {
            listener.onItemRemoved(item);
        }
        return removed;
    }

    /**
     * Remove an item from the buffer.
     * @param item item to be removed
     * @return whether or not an item was in fact removed
     */
    public boolean remove(DelayedWorkQueueItem item)
    {
        return remove(item, true);
    }

    /**
     * Gets the head of the queue/buffer, the next item.
     * @return null or the next item in the buffer if there is one
     */
    public DelayedWorkQueueItem poll()
    {
        if (buffer.size > 0)
        {
            DelayedWorkQueueItem removedItem = buffer.removeFirst();
            listener.onItemRemoved(removedItem);
            return removedItem;
        } else {
            return null;
        }
    }
}
