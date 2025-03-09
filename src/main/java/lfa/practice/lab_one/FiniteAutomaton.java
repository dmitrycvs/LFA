package lfa.practice.lab_one;

import java.util.*;

public class FiniteAutomaton {
    List<String> Q;
    List<String> Sigma;
    HashMap<HashMap<String, String>, List<String>> delta;
    String q0;
    String F;

    public FiniteAutomaton(List<String> Q, List<String> Sigma, HashMap<HashMap<String, String>, List<String>> delta, String q0, String F) {
        this.Q = Q;
        this.Sigma = Sigma;
        this.delta = delta;
        this.q0 = q0;
        this.F = F;
    }

    public boolean stringBelongToLanguage(final String inputString) {
        Set<String> currentStates = new HashSet<>();
        currentStates.add(q0);

        for (int i = 0; i < inputString.length(); i++) {
            String symbol = String.valueOf(inputString.charAt(i));

            if (!Sigma.contains(symbol)) {
                return false;
            }

            Set<String> nextStates = new HashSet<>();
            for (String currentState : currentStates) {
                HashMap<String, String> transitionKey = new HashMap<>();
                transitionKey.put(currentState, symbol);

                List<String> nextStatesList = delta.get(transitionKey);
                if (nextStatesList != null) {
                    nextStates.addAll(nextStatesList);
                }
            }

            if (nextStates.isEmpty()) {
                return false;
            }

            currentStates = nextStates;
        }

        return currentStates.contains(F);
    }

    public Grammar toGrammar() {
        Set<String> nonTerminals = new HashSet<>();
        Set<String> terminals = new HashSet<>(Sigma);
        HashMap<String, List<String>> productionRules = new HashMap<>();
        String startSymbol = q0;

        for (String state : Q) {
            nonTerminals.add(state);
        }

        for (Map.Entry<HashMap<String, String>, List<String>> entry : delta.entrySet()) {
            HashMap<String, String> transitionKey = entry.getKey();
            List<String> destinationStates = entry.getValue();

            String sourceState = transitionKey.keySet().iterator().next();
            String inputSymbol = transitionKey.get(sourceState);

            for (String destState : destinationStates) {
                String productionRight = inputSymbol + destState;

                productionRules.computeIfAbsent(sourceState, k -> new ArrayList<>()).add(productionRight);
            }
        }

        return new Grammar(nonTerminals, terminals, productionRules, startSymbol);
    }

    public boolean isDeterministic() {
        for (List<String> nextStates : delta.values()) {
            if (nextStates.size() != 1) {
                return false;
            }
        }

        return true;
    }

    public FiniteAutomaton toDFA() {
        List<String> newQ = new ArrayList<>();
        List<String> newSigma = new ArrayList<>(this.Sigma);
        HashMap<HashMap<String, String>, List<String>> newDelta = new HashMap<>();
        String newQ0;
        Set<String> newF = new HashSet<>();

        Map<Set<String>, String> stateSetToName = new HashMap<>();
        Map<String, Set<String>> nameToStateSet = new HashMap<>();

        Set<String> initialStateSet = new HashSet<>();
        initialStateSet.add(this.q0);
        newQ0 = "S0";
        stateSetToName.put(initialStateSet, newQ0);
        nameToStateSet.put(newQ0, initialStateSet);
        newQ.add(newQ0);

        Queue<Set<String>> unprocessedStateSets = new LinkedList<>();
        unprocessedStateSets.add(initialStateSet);

        int stateCounter = 1;

        while (!unprocessedStateSets.isEmpty()) {
            Set<String> currentStateSet = unprocessedStateSets.poll();
            String currentStateName = stateSetToName.get(currentStateSet);

            if (currentStateSet.contains(this.F)) {
                newF.add(currentStateName);
            }

            for (String symbol : this.Sigma) {
                Set<String> nextStateSet = new HashSet<>();

                for (String state : currentStateSet) {
                    HashMap<String, String> transitionKey = new HashMap<>();
                    transitionKey.put(state, symbol);

                    List<String> nextStates = this.delta.get(transitionKey);
                    if (nextStates != null) {
                        nextStateSet.addAll(nextStates);
                    }
                }

                if (nextStateSet.isEmpty()) {
                    continue;
                }

                String nextStateName;
                if (!stateSetToName.containsKey(nextStateSet)) {
                    nextStateName = "S" + stateCounter++;
                    stateSetToName.put(nextStateSet, nextStateName);
                    nameToStateSet.put(nextStateName, nextStateSet);
                    newQ.add(nextStateName);
                    unprocessedStateSets.add(nextStateSet);
                } else {
                    nextStateName = stateSetToName.get(nextStateSet);
                }

                HashMap<String, String> newTransitionKey = new HashMap<>();
                newTransitionKey.put(currentStateName, symbol);
                List<String> destinationState = new ArrayList<>();
                destinationState.add(nextStateName);
                newDelta.put(newTransitionKey, destinationState);
            }
        }

        String newFinalState = newF.isEmpty() ? null : newF.iterator().next();

        return new FiniteAutomaton(newQ, newSigma, newDelta, newQ0, newFinalState);
    }

    @Override
    public String toString() {
        return "Q: " + Q.toString() + "\n" +
                "Sigma: " + Sigma.toString() + "\n" +
                "delta: " + delta.toString() + "\n" +
                "q0: " + q0 + "\n" +
                "F: " + F + "\n";
    }
}