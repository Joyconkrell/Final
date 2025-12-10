package test;

import model.Decision;
import model.DecisionManager;

import java.util.HashMap;
import java.util.Map;

public class DecisionManagerTest {
    public static void main(String[] args) {
        DecisionManager dm = new DecisionManager();
        Map<String,Integer> f1 = new HashMap<>(); f1.put("urgency",8); f1.put("importance",7); f1.put("value",6);
        Map<String,Integer> f2 = new HashMap<>(); f2.put("urgency",4); f2.put("importance",5); f2.put("value",9);

        Decision d1 = new Decision("Buy groceries", "Milk, eggs, fruit");
        Decision d2 = new Decision("Answer emails", "Inbox clean up");

        dm.addDecision(d1, f1);
        dm.addDecision(d2, f2);

        System.out.println("Before sort:\n" + dm.generateSummary());
        dm.sortDecisionsByPriorityDescending();
        System.out.println("After sort:\n" + dm.generateSummary());
    }
}
