
package main
import (
    "container/heap"
    "math"
)

const NOT_FOUND = -1
const MIN_HEAP_TYPE = "MIN"
const MAX_HEAP_TYPE = "MAX"

var MAX_NUMBER_OF_STACKS = 1 + int(math.Pow(10.0, 5.0))

type DinnerPlates struct {
    capacityPerStack             int
    stacks                       [][]int
    minHeapIndexesFreedPlates    PriorityQueue
    maxHeapIndexesNotEmptyPlates PriorityQueue
    indexEmptyPlatesNeverUsed    int
}

func Constructor(capacityPerStack int) DinnerPlates {
    dinnerPlates := DinnerPlates{
        capacityPerStack:             capacityPerStack,
        stacks:                       make([][]int, MAX_NUMBER_OF_STACKS),
        minHeapIndexesFreedPlates:    createPriorityQueue(MIN_HEAP_TYPE),
        maxHeapIndexesNotEmptyPlates: createPriorityQueue(MAX_HEAP_TYPE),
        indexEmptyPlatesNeverUsed:    0,
    }
    for i := range dinnerPlates.stacks {
        dinnerPlates.stacks[i] = make([]int, 0)
    }
    return dinnerPlates
}

func (this *DinnerPlates) Push(value int) {
    if this.indexEmptyPlatesNeverUsed == MAX_NUMBER_OF_STACKS && this.minHeapIndexesFreedPlates.Len() == 0 {
        return
    }
    index := this.indexEmptyPlatesNeverUsed
    for this.minHeapIndexesFreedPlates.Len() > 0 {
        i := this.minHeapIndexesFreedPlates.Peek()
        if len(this.stacks[i]) < this.capacityPerStack {
            index = i
            break
        }
        heap.Pop(&this.minHeapIndexesFreedPlates)
    }

    this.stacks[index] = append(this.stacks[index], value)
    heap.Push(&this.maxHeapIndexesNotEmptyPlates, index)

    if len(this.stacks[index]) < this.capacityPerStack {
        return
    }
    if this.minHeapIndexesFreedPlates.Len() > 0 {
        heap.Pop(&this.minHeapIndexesFreedPlates)
        return
    }
    this.indexEmptyPlatesNeverUsed++
}

func (this *DinnerPlates) Pop() int {
    value := NOT_FOUND

    for this.maxHeapIndexesNotEmptyPlates.Len() > 0 && value == NOT_FOUND {
        index := this.maxHeapIndexesNotEmptyPlates.Peek()
        if len(this.stacks[index]) == 0 {
            heap.Pop(&this.maxHeapIndexesNotEmptyPlates)
            continue
        }

        value = this.stacks[index][len(this.stacks[index]) - 1]
        this.stacks[index] = this.stacks[index][0 : len(this.stacks[index]) - 1]
        heap.Push(&this.minHeapIndexesFreedPlates, index)
        if len(this.stacks[index]) == 0 {
            heap.Pop(&this.maxHeapIndexesNotEmptyPlates)
        }
    }
    return value
}

func (this *DinnerPlates) PopAtStack(index int) int {
    if len(this.stacks[index]) == 0 {
        return NOT_FOUND
    }
    value := this.stacks[index][len(this.stacks[index]) - 1]
    this.stacks[index] = this.stacks[index][0 : len(this.stacks[index]) - 1]
    heap.Push(&this.minHeapIndexesFreedPlates, index)
    return value
}

type PriorityQueue struct {
    values     []int
    comparator func(PriorityQueue, int, int) bool
}

func createPriorityQueue(heapType string) PriorityQueue {
    priorityQueue := PriorityQueue{
        values: make([]int, 0),
    }
    if heapType == MIN_HEAP_TYPE {
        priorityQueue.comparator = func(pq PriorityQueue, first int, second int) bool { return pq.values[first] < pq.values[second] }
    } else if heapType == MAX_HEAP_TYPE {
        priorityQueue.comparator = func(pq PriorityQueue, first int, second int) bool { return pq.values[first] > pq.values[second] }
    }
    return priorityQueue
}

func (pq PriorityQueue) Len() int {
    return len(pq.values)
}

func (pq PriorityQueue) Less(first int, second int) bool {
    return pq.comparator(pq, first, second)
}

func (pq PriorityQueue) Swap(first int, second int) {
    pq.values[first], pq.values[second] = pq.values[second], pq.values[first]
}

func (pq *PriorityQueue) Push(object any) {
    pq.values = append(pq.values, object.(int))
}

func (pq *PriorityQueue) Pop() any {
    value := pq.values[pq.Len() - 1]
    pq.values = pq.values[0 : pq.Len() - 1]
    return value
}

func (pq PriorityQueue) Peek() int {
    return pq.values[0]
}
