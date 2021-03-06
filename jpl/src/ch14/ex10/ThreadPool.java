/*
 * Copyright (C) 2012, 2013 RICOH Co., Ltd. All rights reserved.
 * Copyright (C) 2019 Yoshiki Shibata. All rights reserved.
 */
package ch14.ex10;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Simple Thread Pool class.
 *
 * This class can be used to dispatch an Runnable object to be exectued by a
 * thread.<br>
 * <br>
 *
 * [Instruction]
 * <ul>
 * <li>Implement one constructor and three methods.</li>
 * <li>Don't forget to write a Test program to test this class.</li>
 * <li>Pay attention to @throws tags in the javadoc.</li>
 * <li>If needed, you can put "synchronized" keyword to methods.</li>
 * <li>All classes for implementation must be private inside this class.</li>
 * <li>Don't use java.util.concurrent package.</li>
 * <li>Don't use {@link java.lang.Thread#interrupt} method to stop a thread</li>
 * </ul>
 *
 * @author Yoshiki Shibata
 */
public class ThreadPool {
	private static Queue<Runnable> runnableQueue;
	final int numberOfThreads;
	private int queueSize;
//	public static final Object runLock = new Object();
//	public static final Object addLock = new Object();
	ThreadFactory[] threadFacts = null;

	/**
	 * Constructs ThreadPool.
	 *
	 * @param queueSize       the max size of queue
	 * @param numberOfThreads the number of threads in this pool.
	 *
	 * @throws IllegalArgumentException if either queueSize or numberOfThreads is
	 *                                  less than 1
	 */
	public ThreadPool(int queueSize, int numberOfThreads) {
		if (queueSize < 1 || numberOfThreads < 1) {
			throw new IllegalArgumentException();
		}
		runnableQueue = new ArrayDeque<>(queueSize);
		this.numberOfThreads = numberOfThreads;
		this.queueSize = queueSize;
		this.threadFacts = new ThreadFactory[numberOfThreads];
	}

	/**
	 * Starts threads.
	 *
	 * @throws IllegalStateException if threads has been already started.
	 */
	public synchronized void start() {
		// ThreadFactoryの要素はstart()をして代入される。
		if (Objects.nonNull(threadFacts[0])) {
			throw new IllegalStateException();
		}

		for (int i = 0; i < numberOfThreads; i++) {
			threadFacts[i] = new ThreadFactory();
			threadFacts[i].getThread().start();
		}
	}

	/**
	 * Stop all threads gracefully and wait for their terminations. All requests
	 * dispatched before this method is invoked must complete and this method also
	 * will wait for their completion.
	 * 
	 * @throws InterruptedException
	 *
	 * @throws IllegalStateException if threads has not been started.
	 */
	public synchronized void stop() {
		// threadFactoryが要素になかったらstart()をしていない。
		if (Objects.isNull(threadFacts[0])) {
			throw new IllegalStateException();
		}

		// dispatchされたタスクの実行終了まで待つ
		while (!runnableQueue.isEmpty()) {
			for (ThreadFactory thf : threadFacts) {
				synchronized (thf.runLock) {
					thf.runLock.notifyAll();
				}
			}
		}

		for (ThreadFactory thf : threadFacts) {
			thf.stopFlg = true;
		}

		// 全てのthreadの終了を待つ
		for (ThreadFactory thf : threadFacts) {
			try {
				while (!(thf.getThread().getState() == Thread.State.WAITING
						|| thf.getThread().getState() == Thread.State.TERMINATED)) {
				}
				synchronized (thf.runLock) {
					thf.runLock.notifyAll();
				}
				thf.getThread().join();
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		this.threadFacts = new ThreadFactory[numberOfThreads];
	}

	/**
	 * Executes the specified Runnable object, using a thread in the pool. run()
	 * method will be invoked in the thread. If the queue is full, then this method
	 * invocation will be blocked until the queue is not full.
	 *
	 * @param runnable Runnable object whose run() method will be invoked.
	 *
	 * @throws NullPointerException  if runnable is null.
	 * @throws IllegalStateException if this pool has not been started yet.
	 */
	public void dispatch(Runnable runnable) {
		if (Objects.isNull(runnable)) {
			throw new NullPointerException();
		} else if (Objects.isNull(threadFacts[0])) { // threadFactoryが要素になかったらstart()をしていない。
			throw new IllegalStateException();
		}

		while (true) {
			for (ThreadFactory thf : threadFacts) {
				synchronized (this) {
					if (runnable != null && runnableQueue.size() < queueSize) {
						runnableQueue.add(runnable);
						runnable = null;
					}
					if (runnable == null && runnableQueue.size() < 1) {
						return;
					}
				}
				if (thf.getThread().getState() == Thread.State.WAITING) {
					synchronized (thf.runLock) {
						thf.runLock.notifyAll();
					}
				}
			}
		}
	}

	/**
	 * タスクの先頭を取得および削除します。タスクが空の場合はnullを返します。
	 * 
	 * @return タスクの先頭
	 */
	public static Runnable getTask() {
		return runnableQueue.poll();
	}
}
