package com.santander.game.quiz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {

  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(10);
  }
}
