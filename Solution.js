
class DinnerPlates {

    static NOT_FOUND = -1;
    static MAX_NUMBER_OF_STACKS = 1 + Math.pow(10, 5);

    /**
     * @param {number} capacityPerStack
     */
    constructor(capacityPerStack) {
        this.capacityPerStack = capacityPerStack;
        this.stacks = Array.from(new Array(DinnerPlates.MAX_NUMBER_OF_STACKS), () => new Array());
        this.minHeapIndexesFreedPlates = new PriorityQueue((x, y) => x - y);
        this.maxHeapIndexesNotEmptyPlates = new PriorityQueue((x, y) => y - x);
        this.indexEmptyPlatesNeverUsed = 0;
    }

    /** 
     * @param {number} value
     * @return {void}
     */
    push(value) {
        if (this.indexEmptyPlatesNeverUsed === DinnerPlates.MAX_NUMBER_OF_STACKS
                && this.minHeapIndexesFreedPlates.isEmpty()) {
            return;
        }
        let index = this.indexEmptyPlatesNeverUsed;
        while (!this.minHeapIndexesFreedPlates.isEmpty()) {
            let i = this.minHeapIndexesFreedPlates.front();
            if (this.stacks[i].length < this.capacityPerStack) {
                index = i;
                break;
            }
            this.minHeapIndexesFreedPlates.dequeue();
        }

        this.stacks[index].push(value);
        this.maxHeapIndexesNotEmptyPlates.enqueue(index);

        if (this.stacks[index].length < this.capacityPerStack) {
            return;
        }
        if (!this.minHeapIndexesFreedPlates.isEmpty()) {
            this.minHeapIndexesFreedPlates.dequeue();
            return;
        }
        ++this.indexEmptyPlatesNeverUsed;
    }

    /**
     * @return {number}
     */
    pop() {
        let value = DinnerPlates.NOT_FOUND;

        while (!this.maxHeapIndexesNotEmptyPlates.isEmpty() && value === DinnerPlates.NOT_FOUND) {
            const index = this.maxHeapIndexesNotEmptyPlates.front();
            if (this.stacks[index].length === 0) {
                this.maxHeapIndexesNotEmptyPlates.dequeue();
                continue;
            }

            value = this.stacks[index].pop();
            this.minHeapIndexesFreedPlates.enqueue(index);
            if (this.stacks[index].length === 0) {
                this.maxHeapIndexesNotEmptyPlates.dequeue();
            }
        }
        return value;
    }

    /** 
     * @param {number} index
     * @return {number}
     */
    popAtStack(index) {
        if (this.stacks[index].length === 0) {
            return DinnerPlates.NOT_FOUND;
        }
        const value = this.stacks[index].pop();
        this.minHeapIndexesFreedPlates.enqueue(index);
        return value;
    }
}
