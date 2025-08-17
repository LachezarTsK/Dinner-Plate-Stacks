
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;

public class DinnerPlates {

    private static final int NOT_FOUND = -1;
    private static final int MAX_NUMBER_OF_STACKS = 1 + (int) Math.pow(10, 5);

    private final int capacityPerStack;
    private final Deque<Integer>[] stacks;
    private final PriorityQueue<Integer> minHeapIndexesFreedPlates;
    private final PriorityQueue<Integer> maxHeapIndexesNotEmptyPlates;

    private int indexEmptyPlatesNeverUsed;

    public DinnerPlates(int capacityPerStack) {
        this.capacityPerStack = capacityPerStack;
        stacks = new ArrayDeque[MAX_NUMBER_OF_STACKS];
        minHeapIndexesFreedPlates = new PriorityQueue<>();
        maxHeapIndexesNotEmptyPlates = new PriorityQueue<>((x, y) -> y - x);

        for (int i = 0; i < stacks.length; ++i) {
            stacks[i] = new ArrayDeque<>(capacityPerStack);
        }
    }

    public void push(int value) {
        if (indexEmptyPlatesNeverUsed == MAX_NUMBER_OF_STACKS && minHeapIndexesFreedPlates.isEmpty()) {
            return;
        }
        int index = indexEmptyPlatesNeverUsed;
        while (!minHeapIndexesFreedPlates.isEmpty()) {
            int i = minHeapIndexesFreedPlates.peek();
            if (stacks[i].size() < capacityPerStack) {
                index = i;
                break;
            }
            minHeapIndexesFreedPlates.poll();
        }

        stacks[index].push(value);
        maxHeapIndexesNotEmptyPlates.add(index);

        if (stacks[index].size() < capacityPerStack) {
            return;
        }
        if (!minHeapIndexesFreedPlates.isEmpty()) {
            minHeapIndexesFreedPlates.poll();
            return;
        }
        ++indexEmptyPlatesNeverUsed;
    }

    public int pop() {
        int value = NOT_FOUND;

        while (!maxHeapIndexesNotEmptyPlates.isEmpty() && value == NOT_FOUND) {
            int index = maxHeapIndexesNotEmptyPlates.peek();
            if (stacks[index].isEmpty()) {
                maxHeapIndexesNotEmptyPlates.poll();
                continue;
            }

            value = stacks[index].pop();
            minHeapIndexesFreedPlates.add(index);
            if (stacks[index].isEmpty()) {
                maxHeapIndexesNotEmptyPlates.poll();
            }
        }
        return value;
    }

    public int popAtStack(int index) {
        if (stacks[index].isEmpty()) {
            return NOT_FOUND;
        }
        int value = stacks[index].pop();
        minHeapIndexesFreedPlates.add(index);
        return value;
    }
}
