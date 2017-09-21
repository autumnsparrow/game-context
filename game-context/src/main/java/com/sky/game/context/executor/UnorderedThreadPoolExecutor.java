/**
 * 
 */
package com.sky.game.context.executor;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sky.game.context.event.GameEvent;


/**
 * 
 * copy from the mina projects.
 * A {@link ThreadPoolExecutor} that does not maintain the order of {@link GameEvent}s.
 * This means more than one event handler methods can be invoked at the same
 * time with mixed order.  For example, let's assume that messageReceived, messageSent,
 * and sessionClosed events are fired.
 * <ul>
 * <li>All event handler methods can be called simultaneously.
 *     (e.g. messageReceived and messageSent can be invoked at the same time.)</li>
 * <li>The event order can be mixed up.
 *     (e.g. sessionClosed or messageSent can be invoked before messageReceived
 *           is invoked.)</li>
 * </ul>
 * If you need to maintain the order of events per session, please use
 * {@link OrderedThreadPoolExecutor}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * @org.apache.xbean.XBean
 */
public class UnorderedThreadPoolExecutor extends ThreadPoolExecutor {
	
	private static final Log logger=LogFactory.getLog(UnorderedThreadPoolExecutor.class);

    private static final Runnable EXIT_SIGNAL = new Runnable() {
        public void run() {
            throw new Error("This method shouldn't be called. " + "Please file a bug report.");
        }
    };

    private final Set<Worker> workers = new HashSet<Worker>();

    private volatile int corePoolSize;

    private volatile int maximumPoolSize;

    private volatile int largestPoolSize;

    private final AtomicInteger idleWorkers = new AtomicInteger();

    private long completedTaskCount;

    private volatile boolean shutdown;

   

    public UnorderedThreadPoolExecutor() {
        this(16);
    }

    public UnorderedThreadPoolExecutor(int maximumPoolSize) {
        this(0, maximumPoolSize);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        this(corePoolSize, maximumPoolSize, 120, TimeUnit.SECONDS);
    }

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory());
    }

   

    

    public UnorderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            ThreadFactory threadFactory) {
        super(0, 1, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(), threadFactory, new AbortPolicy());
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }

        if (maximumPoolSize == 0 || maximumPoolSize < corePoolSize) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

      

        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.setRejectedExecutionHandler(new DefaultRejectHandler());
        
    }

   
   

    private void addWorker() {
        synchronized (workers) {
            if (workers.size() >= maximumPoolSize) {
            	
            	logger.warn("MaximumPoolSize Queue.size:"+getQueue().size()+" active:"+getActiveCount()+" task.count:"+getTaskCount()+" core.pool.size:"+getCorePoolSize()+" maximum.pool.size:"+getMaximumPoolSize());

                return;
            }

            Worker worker = new Worker();
            Thread thread = getThreadFactory().newThread(worker);
            idleWorkers.incrementAndGet();
            thread.start();
            workers.add(worker);

            if (workers.size() > largestPoolSize) {
                largestPoolSize = workers.size();
            }
        }
    }

    private void addWorkerIfNecessary() {
        if (idleWorkers.get() == 0) {
        	
            synchronized (workers) {
                if (workers.isEmpty() || idleWorkers.get() == 0) {
                    addWorker();
                }
            }
        }
    }

    private void removeWorker() {
        synchronized (workers) {
            if (workers.size() <= corePoolSize) {
                return;
            }
            getQueue().offer(EXIT_SIGNAL);
        }
    }

    @Override
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    @Override
    public void setMaximumPoolSize(int maximumPoolSize) {
        if (maximumPoolSize <= 0 || maximumPoolSize < corePoolSize) {
            throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
        }

        synchronized (workers) {
            this.maximumPoolSize = maximumPoolSize;
            int difference = workers.size() - maximumPoolSize;
            while (difference > 0) {
                removeWorker();
                --difference;
            }
        }
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {

        long deadline = System.currentTimeMillis() + unit.toMillis(timeout);

        synchronized (workers) {
            while (!isTerminated()) {
                long waitTime = deadline - System.currentTimeMillis();
                if (waitTime <= 0) {
                    break;
                }

                workers.wait(waitTime);
            }
        }
        return isTerminated();
    }

    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    @Override
    public boolean isTerminated() {
        if (!shutdown) {
            return false;
        }

        synchronized (workers) {
            return workers.isEmpty();
        }
    }

    @Override
    public void shutdown() {
        if (shutdown) {
            return;
        }

        shutdown = true;

        synchronized (workers) {
            for (int i = workers.size(); i > 0; i--) {
                getQueue().offer(EXIT_SIGNAL);
            }
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        shutdown();

        List<Runnable> answer = new ArrayList<Runnable>();
        Runnable task;
        while ((task = getQueue().poll()) != null) {
            if (task == EXIT_SIGNAL) {
                getQueue().offer(EXIT_SIGNAL);
                Thread.yield(); // Let others take the signal.
                continue;
            }

           
            answer.add(task);
        }

        return answer;
    }

    @Override
    public void execute(Runnable task) {
        if (shutdown) {
            rejectTask(task);
        }

      //  checkTaskType(task);

        
        getQueue().offer(task);
      
       
        addWorkerIfNecessary();

        
    }

    private void rejectTask(Runnable task) {
        getRejectedExecutionHandler().rejectedExecution(task, this);
    }

    private void checkTaskType(Runnable task) {
        if (!(task instanceof GameEvent)) {
            throw new IllegalArgumentException("task must be an IoEvent or its subclass.");
        }
    }

    @Override
    public int getActiveCount() {
        synchronized (workers) {
            return workers.size() - idleWorkers.get();
        }
    }

    @Override
    public long getCompletedTaskCount() {
        synchronized (workers) {
            long answer = completedTaskCount;
            for (Worker w : workers) {
                answer += w.completedTaskCount;
            }

            return answer;
        }
    }

    @Override
    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    @Override
    public int getPoolSize() {
        synchronized (workers) {
            return workers.size();
        }
    }

    @Override
    public long getTaskCount() {
        return getCompletedTaskCount();
    }

    @Override
    public boolean isTerminating() {
        synchronized (workers) {
            return isShutdown() && !isTerminated();
        }
    }

    @Override
    public int prestartAllCoreThreads() {
        int answer = 0;
        synchronized (workers) {
            for (int i = corePoolSize - workers.size(); i > 0; i--) {
                addWorker();
                answer++;
            }
        }
        return answer;
    }

    @Override
    public boolean prestartCoreThread() {
        synchronized (workers) {
            if (workers.size() < corePoolSize) {
                addWorker();
                return true;
            }

            return false;
        }
    }

    @Override
    public void purge() {
        // Nothing to purge in this implementation.
    }

    @Override
    public boolean remove(Runnable task) {
        boolean removed = super.remove(task);
       
        return removed;
    }

    @Override
    public int getCorePoolSize() {
        return corePoolSize;
    }

    @Override
    public void setCorePoolSize(int corePoolSize) {
        if (corePoolSize < 0) {
            throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
        }
        if (corePoolSize > maximumPoolSize) {
            throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
        }

        synchronized (workers) {
            if (this.corePoolSize > corePoolSize) {
                for (int i = this.corePoolSize - corePoolSize; i > 0; i--) {
                    removeWorker();
                }
            }
            this.corePoolSize = corePoolSize;
        }
    }

    private class Worker implements Runnable {

        private volatile long completedTaskCount;

        private Thread thread;

        public void run() {
            thread = Thread.currentThread();

            try {
                for (;;) {
                    Runnable task = fetchTask();

                    idleWorkers.decrementAndGet();

                    if (task == null) {
                        synchronized (workers) {
                        
                               if (workers.size() > corePoolSize) {
                                // Remove now to prevent duplicate exit.
                                workers.remove(this);
                                break;
                            }
                        }
                    }

                    if (task == EXIT_SIGNAL) {
                        break;
                    }

                    try {
                        if (task != null) {
                            runTask(task);
                        }
                    } finally {
                        idleWorkers.incrementAndGet();
                       
                    }
                }
            } finally {
                synchronized (workers) {
                    workers.remove(this);
                    UnorderedThreadPoolExecutor.this.completedTaskCount += completedTaskCount;
                    workers.notifyAll();
                   
                }
            }
        }

        private Runnable fetchTask() {
            Runnable task = null;
            long currentTime = System.currentTimeMillis();
            long deadline = currentTime + getKeepAliveTime(TimeUnit.MILLISECONDS);
            for (;;) {
                try {
                    long waitTime = deadline - currentTime;
                    if (waitTime <= 0) {
                        break;
                    }

                    try {
                        task = getQueue().poll(waitTime, TimeUnit.MILLISECONDS);
                       
                        break;
                    } finally {
                        if (task == null) {

                        
                            currentTime = System.currentTimeMillis();
                        }
                    }
                } catch (InterruptedException e) {
                    // Ignore.
                    continue;
                }
            }
            return task;
        }

        private void runTask(Runnable task) {
            beforeExecute(thread, task);
            boolean ran = false;
            try {
                task.run();
                ran = true;
                afterExecute(task, null);
                completedTaskCount++;
            } catch (RuntimeException e) {
                if (!ran) {
                    afterExecute(task, e);
                }
                throw e;
            }
        }
    }
}
