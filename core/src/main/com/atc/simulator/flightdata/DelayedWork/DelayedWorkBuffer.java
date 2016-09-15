package com.atc.simulator.flightdata.DelayedWork;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

/**
 * Created by luke on 15/09/16.
 *
 * @author Luke Frisken
 */
public class DelayedWorkBuffer {
    private Queue<DelayedWorkQueueItem> buffer;
    private DelayedWorkBufferListener listener;

    private int bufferLength;
    public DelayedWorkBuffer(int bufferLength, DelayedWorkBufferListener listener)
    {
        buffer = new Queue<DelayedWorkQueueItem>(bufferLength);
        this.listener = listener;
        this.bufferLength = bufferLength;
    }

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

    public void add(DelayedWorkQueueItem item)
    {
        this.add(item, true);
    }

    public boolean remove(DelayedWorkQueueItem item, boolean triggerListener)
    {
        boolean removed = buffer.removeValue(item, true);
        if (triggerListener && removed)
        {
            listener.onItemRemoved(item);
        }
        return removed;
    }

    public boolean remove(DelayedWorkQueueItem item)
    {
        return remove(item, true);
    }

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
