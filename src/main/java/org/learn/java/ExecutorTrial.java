package org.learn.java;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
/*
* Demo to show that A virtual thread is bound to a task, but can be mounted on different
* Platform threads a.k.a. Worker Thread in the sample below
* */
public class ExecutorTrial {
    /*Result of below code looks like
    *
        Submission of new tasks in virtual threads
        1731138389129 Started for 1 with VirtualThread[#34]/runnable@ForkJoinPool-1-worker-1
        1731138389129 Started for 7 with VirtualThread[#33]/runnable@ForkJoinPool-1-worker-2
        1731138389129 Started for 9 with VirtualThread[#30]/runnable@ForkJoinPool-1-worker-5
        1731138389129 Started for 5 with VirtualThread[#29]/runnable@ForkJoinPool-1-worker-6
        1731138389129 Started for 6 with VirtualThread[#31]/runnable@ForkJoinPool-1-worker-4
        1731138389129 Started for 3 with VirtualThread[#32]/runnable@ForkJoinPool-1-worker-7
        1731138389129 Started for 2 with VirtualThread[#27]/runnable@ForkJoinPool-1-worker-3
        1731138389129 Started for 4 with VirtualThread[#28]/runnable@ForkJoinPool-1-worker-8
        1731138391143 	Completed for 1 with VirtualThread[#34]/runnable@ForkJoinPool-1-worker-1
        1731138391143 	Completed for 9 with VirtualThread[#30]/runnable@ForkJoinPool-1-worker-7
        1731138391144 	Completed for 7 with VirtualThread[#33]/runnable@ForkJoinPool-1-worker-4
        1731138391145 	Completed for 5 with VirtualThread[#29]/runnable@ForkJoinPool-1-worker-1
        1731138391145 	Completed for 3 with VirtualThread[#32]/runnable@ForkJoinPool-1-worker-4
        1731138391145 	Completed for 2 with VirtualThread[#27]/runnable@ForkJoinPool-1-worker-1
        1731138391145 	Completed for 6 with VirtualThread[#31]/runnable@ForkJoinPool-1-worker-4
        1731138391145 	Completed for 4 with VirtualThread[#28]/runnable@ForkJoinPool-1-worker-4
        1731138391145 Started for 10 with VirtualThread[#44]/runnable@ForkJoinPool-1-worker-7
        1731138391145 Started for 8 with VirtualThread[#45]/runnable@ForkJoinPool-1-worker-8
        1731138393149 	Completed for 10 with VirtualThread[#44]/runnable@ForkJoinPool-1-worker-8
        1731138393149 	Completed for 8 with VirtualThread[#45]/runnable@ForkJoinPool-1-worker-1
        Count of executions with null 10
* */
    public static void main(String[] args) {
        System.out.println("Submission of new tasks in virtual threads");
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            long counted = IntStream.range(1, 11).parallel().mapToObj(i -> executorService.submit(() -> {
                try {
                    System.out.println(Instant.now().toEpochMilli() + " Started for " + i + " with " + Thread.currentThread());
                    Thread.sleep(2_000);
                    System.out.println(Instant.now().toEpochMilli() + " \tCompleted for " + i + " with " + Thread.currentThread());
                } catch (InterruptedException ignore) {
                }
            })).map(f -> {
                try {
                    return f.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).filter(Objects::isNull).count();
            System.out.println("Count of executions with null " + counted);
        } catch (Exception ignore) {

        }

    }
}
