
#include <cmath>
#include <deque>
#include <queue>
#include <vector>
using namespace std;

class DinnerPlates {

    inline static int NOT_FOUND = -1;
    inline static int MAX_NUMBER_OF_STACKS = 1 + pow(10, 5);

    vector<deque<int>> stacks;
    priority_queue<int, vector<int>, greater<int>> minHeapIndexesFreedPlates;
    priority_queue<int> maxHeapIndexesNotEmptyPlates;

    int capacityPerStack = 0;
    int indexEmptyPlatesNeverUsed = 0;

public:
    DinnerPlates(int capacityPerStack) :capacityPerStack{ capacityPerStack } {
        stacks.resize(MAX_NUMBER_OF_STACKS);
    }

    void push(int value) {
        if (indexEmptyPlatesNeverUsed == MAX_NUMBER_OF_STACKS && minHeapIndexesFreedPlates.empty()) {
            return;
        }
        int index = indexEmptyPlatesNeverUsed;
        while (!minHeapIndexesFreedPlates.empty()) {
            int i = minHeapIndexesFreedPlates.top();
            if (stacks[i].size() < capacityPerStack) {
                index = i;
                break;
            }
            minHeapIndexesFreedPlates.pop();
        }

        stacks[index].push_back(value);
        maxHeapIndexesNotEmptyPlates.push(index);

        if (stacks[index].size() < capacityPerStack) {
            return;
        }
        if (!minHeapIndexesFreedPlates.empty()) {
            minHeapIndexesFreedPlates.pop();
            return;
        }
        ++indexEmptyPlatesNeverUsed;
    }

    int pop() {
        int value = NOT_FOUND;

        while (!maxHeapIndexesNotEmptyPlates.empty() && value == NOT_FOUND) {
            int index = maxHeapIndexesNotEmptyPlates.top();
            if (stacks[index].empty()) {
                maxHeapIndexesNotEmptyPlates.pop();
                continue;
            }

            value = stacks[index].back();
            stacks[index].pop_back();
            minHeapIndexesFreedPlates.push(index);
            if (stacks[index].empty()) {
                maxHeapIndexesNotEmptyPlates.pop();
            }
        }
        return value;
    }

    int popAtStack(int index) {
        if (stacks[index].empty()) {
            return NOT_FOUND;
        }
        int value = stacks[index].back();
        stacks[index].pop_back();
        minHeapIndexesFreedPlates.push(index);
        return value;
    }
};
