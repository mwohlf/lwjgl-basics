package net.wohlfart;

import net.wohlfart.tools.SimpleMathBenchmarks;

public class BenchmarkRunner {

    public static void main(String[] args) {
        runBenchmark();
    }

    private static void runBenchmark() {
        com.google.caliper.Runner.main(new String[] { "-Dsize=100", SimpleMathBenchmarks.Benchmark01.class.getName() });
    }
}
