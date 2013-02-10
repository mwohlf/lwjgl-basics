package net.wohlfart.basic.time;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TimerTest {

    private class TestClock implements Clock {

        private final long ticksPerSecond;
        private final long maxValidCount;
        private final List<Long> ticks;
        private int index = 0;

        TestClock(long tps, long wac, List<Long> ticks) {
            this.ticksPerSecond = tps;
            this.maxValidCount = wac;
            this.ticks = ticks;
        }

        @Override
        public long getTicks() {
            return ticks.get(index++);
        }

        @Override
        public long getTicksPerSecond() {
            return ticksPerSecond;
        }

        @Override
        public long getMaxValidCount() {
            return maxValidCount;
        }

    }

    @Test
    public void simpleWrapAround() {

        final List<Long> ticks = new ArrayList<Long>();
        final long max = 12;
        long tick = 0;
        final long tickdelta = 5;
        ticks.add(tick);
        for (int i = 0; i < 15; i++) {
            tick = (tick + tickdelta) % (max + 1);
            ticks.add(tick);
        }

        final Timer timer = new TimerImpl(new TestClock(1, max, ticks));
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
        assertEquals(5f, timer.getDelta(), 0.00001f);
    }

    @Test
    public void defaultWrapAround() {

        final List<Long> ticks = new ArrayList<Long>();
        final long max = 13;
        long tick = 0;
        final long tickdelta = 5;
        ticks.add(tick);
        for (int i = 0; i < 15; i++) {
            tick = (tick + tickdelta) % (max + 1);
            ticks.add(tick);
        }

        final Timer timer = new TimerImpl(new TestClock(2, max, ticks));
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
    }

    @Test
    public void longWrapAround() {

        final List<Long> ticks = new ArrayList<Long>();
        final long max = Long.MAX_VALUE; // = = 0x7fffffffffffffffL;
        long tick = Long.MAX_VALUE - 17;
        final long tickdelta = 5;
        ticks.add(tick);
        for (int i = 0; i < 15; i++) {
            tick = (tick + tickdelta) & Long.MAX_VALUE;
            ticks.add(tick);
        }

        final Timer timer = new TimerImpl(new TestClock(2, max, ticks));
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
        assertEquals(2.5f, timer.getDelta(), 0.00001f);
    }

}
