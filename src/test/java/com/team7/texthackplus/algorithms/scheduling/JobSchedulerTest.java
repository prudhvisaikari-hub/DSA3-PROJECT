package com.team7.texthackplus.algorithms.scheduling;

import com.team7.texthackplus.structures.array.DynamicArray;

public class JobSchedulerTest {

    public static void main(String[] args) {
        testScheduling();
        System.out.println("JobScheduler tests passed!");
    }

    private static void testScheduling() {
        JobScheduler.Job[] jobs = new JobScheduler.Job[] {
            new JobScheduler.Job(1, 10, 5),
            new JobScheduler.Job(2, 20, 2),
            new JobScheduler.Job(3, 10, 2),
            new JobScheduler.Job(4, 5, 10)
        };

        JobScheduler.SearchResult result = JobScheduler.schedule(jobs);
        DynamicArray<Integer> order = result.getOrder();

        assert order.size() == 4 : "Expected 4 jobs scheduled";
        
        // Priority 20
        assert order.get(0) == 2 : "Expected job 2 first";
        
        // Priority 10, shorter duration (2)
        assert order.get(1) == 3 : "Expected job 3 second";
        
        // Priority 10, longer duration (5)
        assert order.get(2) == 1 : "Expected job 1 third";
        
        // Priority 5
        assert order.get(3) == 4 : "Expected job 4 fourth";
    }
}
