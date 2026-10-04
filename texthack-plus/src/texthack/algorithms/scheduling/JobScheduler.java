package texthack.algorithms.scheduling;

import texthack.datastructures.MyArrayList;

/**
 * Task-scheduling illustration for NP-hard-style problems: job sequencing
 * with deadlines to maximize profit, where each job takes one unit of time
 * and can be scheduled any time at or before its deadline.
 *
 * Two solvers are provided on purpose, to illustrate the theory/practice
 * gap the abstract talks about:
 *  - greedySchedule(): the standard polynomial-time greedy (sort by profit,
 *    slot each job as late as possible before its deadline) - this is the
 *    known-optimal approach for THIS particular problem variant.
 *  - bruteForceSchedule(): exhaustive search over all subsets/orderings,
 *    which is exponential and only practical for small n - included to
 *    demonstrate what "NP-hard-style" brute force actually costs, and to
 *    verify the greedy result on small inputs.
 */
public class JobScheduler {

    public static class Job {
        public final String id;
        public final int deadline;
        public final int profit;
        public Job(String id, int deadline, int profit) {
            this.id = id; this.deadline = deadline; this.profit = profit;
        }
    }

    public static class ScheduleResult {
        public final MyArrayList<Job> scheduledJobs;
        public final int totalProfit;
        public ScheduleResult(MyArrayList<Job> scheduledJobs, int totalProfit) {
            this.scheduledJobs = scheduledJobs;
            this.totalProfit = totalProfit;
        }
    }

    /** Polynomial-time greedy: sort by profit desc, place each job in the latest free slot <= deadline. */
    public static ScheduleResult greedySchedule(MyArrayList<Job> jobs) {
        MyArrayList<Job> sorted = new MyArrayList<>();
        for (int i = 0; i < jobs.size(); i++) sorted.add(jobs.get(i));
        sorted.sort((a, b) -> Integer.compare(b.profit, a.profit));

        int maxDeadline = 0;
        for (int i = 0; i < sorted.size(); i++) maxDeadline = Math.max(maxDeadline, sorted.get(i).deadline);

        Job[] slots = new Job[maxDeadline + 1]; // slot 0 unused
        int profit = 0;
        MyArrayList<Job> result = new MyArrayList<>();

        for (int i = 0; i < sorted.size(); i++) {
            Job job = sorted.get(i);
            for (int slot = Math.min(job.deadline, maxDeadline); slot >= 1; slot--) {
                if (slots[slot] == null) {
                    slots[slot] = job;
                    profit += job.profit;
                    result.add(job);
                    break;
                }
            }
        }
        return new ScheduleResult(result, profit);
    }

    /** Exponential brute force: tries every subset, keeps feasible ones, picks the best profit. */
    public static ScheduleResult bruteForceSchedule(MyArrayList<Job> jobs) {
        int n = jobs.size();
        int bestProfit = -1;
        MyArrayList<Job> best = new MyArrayList<>();

        long subsetCount = 1L << n; // 2^n subsets
        for (long mask = 0; mask < subsetCount; mask++) {
            MyArrayList<Job> subset = new MyArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1L << i)) != 0) subset.add(jobs.get(i));
            }
            if (feasible(subset)) {
                int profit = 0;
                for (int i = 0; i < subset.size(); i++) profit += subset.get(i).profit;
                if (profit > bestProfit) {
                    bestProfit = profit;
                    best = subset;
                }
            }
        }
        return new ScheduleResult(best, Math.max(bestProfit, 0));
    }

    /** A subset is feasible if its jobs can be assigned distinct slots each <= its own deadline. */
    private static boolean feasible(MyArrayList<Job> subset) {
        MyArrayList<Job> sorted = new MyArrayList<>();
        for (int i = 0; i < subset.size(); i++) sorted.add(subset.get(i));
        sorted.sort((a, b) -> Integer.compare(a.deadline, b.deadline));
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).deadline < i + 1) return false; // can't fit i+1 jobs by this deadline
        }
        return true;
    }
}
