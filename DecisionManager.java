package model;

import java.util.*;

public class DecisionManager {
    private final ArrayList<Decision> decisions = new ArrayList<>();
    private final HashMap<Decision, Map<String, Integer>> decisionFactors = new HashMap<>();

    public void addDecision(Decision d, Map<String, Integer> factors) {
        decisions.add(d);
        decisionFactors.put(d, new HashMap<>(factors));
    }

    public void removeDecision(Decision d) {
        decisions.remove(d);
        decisionFactors.remove(d);
    }

    public List<Decision> getDecisions() {
        return Collections.unmodifiableList(decisions);
    }

    public Map<String, Integer> getFactors(Decision d) {
        return decisionFactors.getOrDefault(d, Collections.emptyMap());
    }

    public void updateFactors(Decision d, Map<String, Integer> factors) {
        if (decisionFactors.containsKey(d)) {
            decisionFactors.put(d, new HashMap<>(factors));
        }
    }

    public void editDecision(Decision oldDecision, Decision newDecision) {
        int idx = decisions.indexOf(oldDecision);
        if (idx >= 0) {
            Map<String, Integer> factors = decisionFactors.remove(oldDecision);
            decisions.set(idx, newDecision);
            if (factors != null) decisionFactors.put(newDecision, factors);
        }
    }

    public void sortDecisionsByPriorityDescending() {
        ArrayList<Double> scores = new ArrayList<>();
        for (Decision d : decisions) {
            Map<String, Integer> f = decisionFactors.getOrDefault(d, Collections.emptyMap());
            scores.add(PriorityCalculator.scoreFor(f));
        }

        for (int i = 1; i < decisions.size(); i++) {
            Decision keyDecision = decisions.get(i);
            double keyScore = scores.get(i);
            int j = i - 1;
            while (j >= 0 && scores.get(j) < keyScore) {
                decisions.set(j + 1, decisions.get(j));
                scores.set(j + 1, scores.get(j));
                j--;
            }
            decisions.set(j + 1, keyDecision);
            scores.set(j + 1, keyScore);
        }
    }

    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Decisions (count: ").append(decisions.size()).append(")\n");
        for (Decision d : decisions) {
            sb.append("- ").append(d.toString());
            Map<String, Integer> f = decisionFactors.getOrDefault(d, Collections.emptyMap());
            double s = PriorityCalculator.scoreFor(f);
            sb.append(" [score: ").append(String.format("%.2f", s)).append("]\n");
        }
        return sb.toString();
    }
}
