package com.texthackplus.algorithms.scheduling;

import com.texthackplus.structures.heap.MaxHeap;
import com.texthackplus.structures.array.DynamicArray;

/**
 * Greedy Job Scheduler using a MaxHeap.
 * Jobs are scheduled based on priority.
 *
 * Simulates a batch processor for the text analysis engine where
 * different text mining tasks have different priorities.
 */
public class JobScheduler {

    public static class Job implements Comparable<Job> {
        private final int id;
        private final int priority;
        private final int duration;

        public Job(int id, int priority, int duration) {
            this.id = id;
            this.priority = priority;
            this.duration = duration;
        }

        public int getId() { return id; }
        public int getPriority() { return priority; }
        public int getDuration() { return duration; }

        @Override
        public int compareTo(Job other) {
            // Higher priority first.
            // If priorities are equal, shorter duration first (SJF).
            if (this.priority != other.priority) {
                return Integer.compare(this.priority, other.priority);
            }
            // Note: shorter duration is "greater" in priority for max heap
            return Integer.compare(other.duration, this.duration);
        }
    }

    public static class SearchResult {
        private final DynamicArray<Integer> order;
        private final int comparisons;
        private final long timeNs;
        private final String algorithm = "Priority Job Scheduler";
        private final String complexity = "O(n log n)";

        public SearchResult(DynamicArray<Integer> order, int comparisons, long timeNs) {
            this.order = order;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public DynamicArray<Integer> getOrder() { return order; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Schedules jobs using the greedy priority heuristic.
     *
     * @param jobs The list of jobs to schedule.
     * @return a {@link SearchResult} containing the ordered job IDs.
     */
    public static SearchResult schedule(Job[] jobs) {
        long start = System.nanoTime();
        int comparisons = 0;

        MaxHeap<Job> heap = new MaxHeap<>();
        for (int i = 0; i < jobs.length; i++) {
            heap.insert(jobs[i]);
            comparisons++; // One insert operation represents some work
        }

        DynamicArray<Integer> order = new DynamicArray<>();
        while (heap.size() > 0) {
            Job job = heap.extractMax();
            order.add(job.getId());
            comparisons++; // One extract operation represents some work
        }

        long end = System.nanoTime();
        return new SearchResult(order, comparisons, end - start);
    }
}
