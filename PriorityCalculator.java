package model;

import java.util.Map;

public class PriorityCalculator {
    public static double scoreFor(Map<String, Integer> factors) {
        int urgency = factors.getOrDefault("urgency", 0);
        int importance = factors.getOrDefault("importance", 0);
        int value = factors.getOrDefault("value", 0);
        return urgency * 0.5 + importance * 0.35 + value * 0.15;
    }
}
