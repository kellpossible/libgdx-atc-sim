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

    public void add(DelayedWorkQueueItem newItem)
    {
        if (buffer.size == bufferLength)
        {
            DelayedWorkQueueItem replacedItem = buffer.removeLast();
            listener.onItemReplaced(replacedItem, newItem);
        } else {
            listener.onItemAdded(newItem);
        }
        buffer.addLast(newItem);
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
