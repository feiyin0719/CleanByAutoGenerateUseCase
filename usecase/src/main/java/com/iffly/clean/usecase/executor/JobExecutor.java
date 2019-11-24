package com.iffly.clean.usecase.executor;



import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Decorated {@link ThreadPoolExecutor}
 */

public class JobExecutor implements ThreadExecutor {
  private final ThreadPoolExecutor threadPoolExecutor;
  private int poolNum=3;

  JobExecutor() {
    this.threadPoolExecutor = new ThreadPoolExecutor(poolNum,2*poolNum-1, 10, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(), new JobThreadFactory());
  }
  JobExecutor(int poolNum) {
    this.poolNum=poolNum;
    this.threadPoolExecutor = new ThreadPoolExecutor(poolNum,2*poolNum-1, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), new JobThreadFactory());
  }
  @Override
  public void execute(Runnable runnable) {
    this.threadPoolExecutor.execute(runnable);
  }

  private static class JobThreadFactory implements ThreadFactory {
    private int counter = 0;

    @Override
    public Thread newThread(Runnable runnable) {
      return new Thread(runnable, "android_" + counter++);
    }
  }
}
