package com.santander.game.quiz;

import java.io.Serial;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSum {

  // Custom task class for Fork/Join framework
  static class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000; // Threshold for splitting tasks
    @Serial private static final long serialVersionUID = 4460536508222230693L;
    private int[] array;
    private int start, end;

    SumTask(int[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    protected Long compute() {
      if (end - start <= THRESHOLD) {
        // Direct computation if task is small enough
        long sum = 0;
        for (int i = start; i < end; i++) {
          sum += array[i];
        }
        return sum;
      } else {
        // Split task if it's too large
        int mid = (start + end) / 2;
        SumTask leftTask = new SumTask(array, start, mid);
        SumTask rightTask = new SumTask(array, mid, end);

        // Fork left task
        leftTask.fork();
        // Directly compute right task and join results
        long rightResult = rightTask.compute();
        long leftResult = leftTask.join();

        return leftResult + rightResult;
      }
    }
  }

  public static void main(String[] args) {
    // Sample array
    int[] array = new int[10000];
    for (int i = 0; i < array.length; i++) {
      array[i] = i + 1;
    }

    // Create ForkJoinPool and invoke the main task
    ForkJoinPool pool = new ForkJoinPool();
    SumTask task = new SumTask(array, 0, array.length);
    long result = pool.invoke(task);

    // Print the result
    System.out.println("Sum: " + result);
  }
}
