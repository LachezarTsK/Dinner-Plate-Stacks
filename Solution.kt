
import kotlin.math.pow

class DinnerPlates(capacity: Int) {

    private companion object {
        const val NOT_FOUND = -1
        val MAX_NUMBER_OF_STACKS = 1 + (10.0).pow(5).toInt()
    }

    private val capacityPerStack = capacity
    private val stacks = Array<ArrayDeque<Int>>(MAX_NUMBER_OF_STACKS) { ArrayDeque<Int>() }
    private val minHeapIndexesFreedPlates = java.util.PriorityQueue<Int>()
    private val maxHeapIndexesNotEmptyPlates = java.util.PriorityQueue<Int>() { x, y -> y - x }

    private var indexEmptyPlatesNeverUsed = 0

    fun push(`value`: Int) {
        if (indexEmptyPlatesNeverUsed == MAX_NUMBER_OF_STACKS && minHeapIndexesFreedPlates.isEmpty()) {
            return
        }
        var index = indexEmptyPlatesNeverUsed
        while (!minHeapIndexesFreedPlates.isEmpty()) {
            val i = minHeapIndexesFreedPlates.peek()
            if (stacks[i].size < capacityPerStack) {
                index = i
                break
            }
            minHeapIndexesFreedPlates.poll()
        }

        stacks[index].add(value)
        maxHeapIndexesNotEmptyPlates.add(index)

        if (stacks[index].size < capacityPerStack) {
            return
        }
        if (!minHeapIndexesFreedPlates.isEmpty()) {
            minHeapIndexesFreedPlates.poll()
            return
        }
        ++indexEmptyPlatesNeverUsed
    }

    fun pop(): Int {
        var value = NOT_FOUND

        while (!maxHeapIndexesNotEmptyPlates.isEmpty() && value == NOT_FOUND) {
            val index = maxHeapIndexesNotEmptyPlates.peek()
            if (stacks[index].isEmpty()) {
                maxHeapIndexesNotEmptyPlates.poll()
                continue
            }

            value = stacks[index].removeLast() 
            minHeapIndexesFreedPlates.add(index)
            if (stacks[index].isEmpty()) {
                maxHeapIndexesNotEmptyPlates.poll()
            }
        }
        return value
    }

    fun popAtStack(index: Int): Int {
        if (stacks[index].isEmpty()) {
            return NOT_FOUND
        }
        val value = stacks[index].removeLast()   
        minHeapIndexesFreedPlates.add(index)
        return value
    }
}
