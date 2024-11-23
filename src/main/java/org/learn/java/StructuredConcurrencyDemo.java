package org.learn.java;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.StructuredTaskScope;

/**
 * This demonstrates a simple usage of StructuredConcurrency where multiple threads can be spawned and managed.
 * The output of below code execution is as
 * <p>
 1732375918545: Callables created in thread Thread[#1,main,5,main]
 1732375918554: Subtasks created for all callables, going to wait for join: Thread[#1,main,5,main]
 1732375918554: Second callable getting executed on VirtualThread[#22]/runnable@ForkJoinPool-1-worker-2
 1732375918554: First callable getting executed on VirtualThread[#20]/runnable@ForkJoinPool-1-worker-1
 1732375919561: Second callable completed on VirtualThread[#22]/runnable@ForkJoinPool-1-worker-1
 1732375919562: First callable completed on VirtualThread[#20]/runnable@ForkJoinPool-1-worker-2
 1732375919563: value of task 1 : 1000
 1732375919563: value of task 2 : 1000
 1732375919569: System going to exit: Thread[#1,main,5,main]
 * <p>
 *
 * Please note to work this out you need to enable preview while compile and execute
 */
public class StructuredConcurrencyDemo {
    @SuppressWarnings("preview")
    public static void main(String[] args) {
        Callable<Integer> c1 = () -> {
            int millis = 1000;
            try{
                System.out.println(STR."\{Instant.now().toEpochMilli()}: First callable getting executed on \{Thread.currentThread()}");
                Thread.sleep(millis);
                System.out.println(STR."\{Instant.now().toEpochMilli()}: First callable completed on \{Thread.currentThread()}");
            }
            catch (InterruptedException ignore){}
            return millis;
        };

        Callable<Integer> c2 = () -> {
            int millis = 1000;
            try{
                System.out.println(STR."\{Instant.now().toEpochMilli()}: Second callable getting executed on \{Thread.currentThread()}");
                Thread.sleep(millis);
                System.out.println(STR."\{Instant.now().toEpochMilli()}: Second callable completed on \{Thread.currentThread()}");
            }
            catch (InterruptedException ignore){}
            return millis;
        };
        System.out.println(STR."\{Instant.now().toEpochMilli()}: Callables created in thread \{Thread.currentThread()}");
        try( var scope = new StructuredTaskScope<Integer>()){
            StructuredTaskScope.Subtask<Integer> fork1 = scope.fork(c1);
            StructuredTaskScope.Subtask<Integer> fork2 = scope.fork(c2);
            System.out.println(STR."\{Instant.now().toEpochMilli()}: Subtasks created for all callables, going to wait for join: \{Thread.currentThread()}");
            scope.join();
            System.out.println(STR."\{Instant.now().toEpochMilli()}: value of task 1 : \{fork1.get()}");
            System.out.println(STR."\{Instant.now().toEpochMilli()}: value of task 2 : \{fork2.get()}");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(STR."\{Instant.now().toEpochMilli()}: System going to exit: \{Thread.currentThread()}");
    }
}
