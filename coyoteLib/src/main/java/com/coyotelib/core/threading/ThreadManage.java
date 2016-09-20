package com.coyotelib.core.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dddd on 2016/2/2.
 */
public class ThreadManage {
    public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }
}
