package org.learn.java;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Thread t = Thread.ofVirtual().unstarted(() -> {
                System.out.println("Thread started : "+Thread.currentThread());
                try{
                    Thread.sleep(1_000);
                }
                catch (InterruptedException ignore){}
            });

            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i + ", thread = " + t.getName());
            threads.add(t);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}