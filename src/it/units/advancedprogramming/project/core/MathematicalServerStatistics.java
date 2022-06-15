package it.units.advancedprogramming.project.core;

public class MathematicalServerStatistics {
    private int okResponseCount;
    private double avgResponseTime;
    private double maxResponseTime;

    public MathematicalServerStatistics() {
        okResponseCount = 0;
        avgResponseTime = 0;
        maxResponseTime = 0;
    }

    public int getOkResponseCount() {
        return okResponseCount;
    }

    public void incrementOkResponseCount() {
        okResponseCount = okResponseCount + 1;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void updateAvgResponseTime(double time) {
        try {
            avgResponseTime = (avgResponseTime + time) / okResponseCount;
        } catch (ArithmeticException e) {
            avgResponseTime = 0;
        }
    }

    public double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void updateMaxResponseTimeIfGreater(double time) {
        if (time > maxResponseTime) {
            maxResponseTime = time;
        }
    }

}
