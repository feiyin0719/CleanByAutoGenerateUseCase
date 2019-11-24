/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iffly.clean.usecase.executor;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.Executor;


/**
 * Global executor pools for the whole application.
 * <p>
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
public class AppExecutors {
    private static AppExecutors INSTANCE;
    public static AppExecutors getINSTANCE(){
        if(INSTANCE==null){
            synchronized (AppExecutors.class){
                if(INSTANCE==null)
                    INSTANCE=new AppExecutors();

            }

        }
        return INSTANCE;
    }
    private static final int THREAD_COUNT = 3;

    private final ThreadExecutor diskIO;

    private final ThreadExecutor networkIO;

    private final ThreadExecutor mainThread;

    private AppExecutors(ThreadExecutor diskIO, ThreadExecutor networkIO, ThreadExecutor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    private AppExecutors() {
        this(new JobExecutor(), new JobExecutor(THREAD_COUNT),
                new MainThreadExecutor());
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements ThreadExecutor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
