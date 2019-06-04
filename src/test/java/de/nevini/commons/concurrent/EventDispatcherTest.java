package de.nevini.commons.concurrent;

import de.nevini.commons.util.Functions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventDispatcherTest {

    private EventDispatcher<Number> eventDispatcher;

    @Before
    public void createEventDispatcher() {
        // create a new event dispatcher for each test
        eventDispatcher = new EventDispatcher<>();
    }

    /**
     * Waits up to 10 seconds for the event dispatcher to finish whatever it is doing.
     *
     * @throws Exception if {@link CompletableFuture#get(long, TimeUnit)} throws an exception
     */
    private void sync() throws Exception {
        CompletableFuture<Void> completable = new CompletableFuture<>();
        eventDispatcher.execute(() -> completable.complete(null));
        completable.get(10, TimeUnit.SECONDS);
    }

    @After
    public void shutDownEventDispatcher() {
        // shut down the event dispatcher after each test
        eventDispatcher.shutdown();
    }

    @Test
    public void testSubscribe() throws Exception {
        // make sure "more-than-once" subscriptions are called multiple times
        List<Number> results = Collections.synchronizedList(new ArrayList<>());
        eventDispatcher.subscribe(Number.class, results::add);
        eventDispatcher.publish(0);
        eventDispatcher.publish(0);
        // wait for asynchronous execution to finish
        sync();
        // check the results
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(0, results.get(0));
        Assert.assertEquals(0, results.get(1));
    }

    @Test
    public void testSubscribeOnce() throws Exception {
        // make sure "only-once" subscriptions are called no more than once
        List<Number> results = Collections.synchronizedList(new ArrayList<>());
        eventDispatcher.subscribe(Number.class, null, results::add, true);
        eventDispatcher.publish(0);
        eventDispatcher.publish(0);
        // wait for asynchronous execution to finish
        sync();
        // check the results
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(0, results.get(0));
    }

    @Test
    public void testConditionMatch() throws Exception {
        // make sure the callback is called if the condition matches
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, n -> Integer.valueOf(0).equals(n), completable::complete, true);
        eventDispatcher.publish(0);
        Assert.assertEquals(0, completable.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testConditionNoMatch() throws Exception {
        // make sure the callback is not called if the condition does not match
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, n -> Integer.valueOf(0).equals(n), completable::complete, true);
        eventDispatcher.publish(1);
        completable.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testTimeoutNoEvent() throws Exception {
        // make sure the timeout callback is called if no matching event is published
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, null, Functions.ignore(), true,
                1, TimeUnit.SECONDS, () -> completable.complete(0), false);
        Assert.assertEquals(0, completable.get(2, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testTimeoutEvent() throws Exception {
        // make sure the timeout callback is not called if a matching event is published
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, null, Functions.ignore(), true,
                1, TimeUnit.SECONDS, () -> completable.complete(0), false);
        eventDispatcher.publish(0);
        completable.get(2, TimeUnit.SECONDS);
    }

    @Test
    public void testTimeoutAlwaysNoEvent() throws Exception {
        // make sure the timeout callback is called if no matching event is published
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, null, Functions.ignore(), true,
                1, TimeUnit.SECONDS, () -> completable.complete(0), true);
        Assert.assertEquals(0, completable.get(2, TimeUnit.SECONDS));
    }

    @Test
    public void testTimeoutAlwaysEvent() throws Exception {
        // make sure the timeout callback is called event after if a matching event is published
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.subscribe(Number.class, null, Functions.ignore(), true,
                1, TimeUnit.SECONDS, () -> completable.complete(0), true);
        eventDispatcher.publish(0);
        Assert.assertEquals(0, completable.get(2, TimeUnit.SECONDS));
    }

    @Test
    public void testExecute() throws Exception {
        // make sure that asynchronous tasks are executed in near-time
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.execute(() -> completable.complete(0));
        Assert.assertEquals(0, completable.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testExecuteAfterShutdown() throws Exception {
        // make sure that new asynchronous tasks are not executed after shutdown
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.shutdown();
        eventDispatcher.execute(() -> completable.complete(0));
        completable.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testSchedule() throws Exception {
        // make sure that asynchronous tasks are executed within their allotted time
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.schedule(1, TimeUnit.SECONDS, () -> completable.complete(0));
        Assert.assertEquals(0, completable.get(2, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testScheduleAfterShutdown() throws Exception {
        // make sure that new asynchronous tasks are not executed after shutdown
        CompletableFuture<Number> completable = new CompletableFuture<>();
        eventDispatcher.shutdown();
        eventDispatcher.schedule(1, TimeUnit.SECONDS, () -> completable.complete(0));
        completable.get(2, TimeUnit.SECONDS);
    }

    @Test
    public void testShutdownMultiple() {
        // no exceptions should be thrown when callling shutdown multiple times
        eventDispatcher.shutdown();
        eventDispatcher.shutdown();
        eventDispatcher.shutdown();
    }

}
