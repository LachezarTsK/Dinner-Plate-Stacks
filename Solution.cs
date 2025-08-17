
using System;
using System.Collections.Generic;

public class DinnerPlates
{
    private static readonly int NOT_FOUND = -1;
    private static readonly int MAX_NUMBER_OF_STACKS = 1 + (int)Math.Pow(10, 5);

    private readonly int capacityPerStack;
    private readonly Stack<int>[] stacks;
    private readonly PriorityQueue<int, int> minHeapIndexesFreedPlates;
    private readonly PriorityQueue<int, int> maxHeapIndexesNotEmptyPlates;

    private int indexEmptyPlatesNeverUsed;

    public DinnerPlates(int capacityPerStack)
    {
        this.capacityPerStack = capacityPerStack;
        stacks = new Stack<int>[MAX_NUMBER_OF_STACKS];
        minHeapIndexesFreedPlates = new PriorityQueue<int, int>();
        maxHeapIndexesNotEmptyPlates = new PriorityQueue<int, int>(Comparer<int>.Create((x, y) => y - x));

        for (int i = 0; i < stacks.Length; ++i)
        {
            stacks[i] = new Stack<int>(capacityPerStack);
        }
    }

    public void Push(int value)
    {
        if (indexEmptyPlatesNeverUsed == MAX_NUMBER_OF_STACKS && minHeapIndexesFreedPlates.Count == 0)
        {
            return;
        }
        int index = indexEmptyPlatesNeverUsed;
        while (minHeapIndexesFreedPlates.Count > 0)
        {
            int i = minHeapIndexesFreedPlates.Peek();
            if (stacks[i].Count < capacityPerStack)
            {
                index = i;
                break;
            }
            minHeapIndexesFreedPlates.Dequeue();
        }

        stacks[index].Push(value);
        maxHeapIndexesNotEmptyPlates.Enqueue(index, index);

        if (stacks[index].Count < capacityPerStack)
        {
            return;
        }
        if (minHeapIndexesFreedPlates.Count > 0)
        {
            minHeapIndexesFreedPlates.Dequeue();
            return;
        }
        ++indexEmptyPlatesNeverUsed;
    }

    public int Pop()
    {
        int value = NOT_FOUND;

        while (maxHeapIndexesNotEmptyPlates.Count > 0 && value == NOT_FOUND)
        {
            int index = maxHeapIndexesNotEmptyPlates.Peek();
            if (stacks[index].Count == 0)
            {
                maxHeapIndexesNotEmptyPlates.Dequeue();
                continue;
            }

            value = stacks[index].Pop();
            minHeapIndexesFreedPlates.Enqueue(index, index);
            if (stacks[index].Count == 0)
            {
                maxHeapIndexesNotEmptyPlates.Dequeue();
            }
        }
        return value;
    }

    public int PopAtStack(int index)
    {
        if (stacks[index].Count == 0)
        {
            return NOT_FOUND;
        }
        int value = stacks[index].Pop();
        minHeapIndexesFreedPlates.Enqueue(index, index);
        return value;
    }
}
