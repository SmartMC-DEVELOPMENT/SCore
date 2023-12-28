package me.imsergioh.smartcorewaterfall.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class AsyncUtilities {

  public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

  public static CompletableFuture<Void> schedule(Runnable task, long unit, TimeUnit timeUnit) {
    final var executor = CompletableFuture.delayedExecutor(unit, timeUnit, EXECUTOR_SERVICE);
    return CompletableFuture.runAsync(task, executor);
  }

  public static CompletableFuture<Void> schedule(Runnable task) {
    return CompletableFuture.runAsync(task, EXECUTOR_SERVICE);
  }

  public static <T> CompletableFuture<T> provide(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, EXECUTOR_SERVICE);
  }
}
