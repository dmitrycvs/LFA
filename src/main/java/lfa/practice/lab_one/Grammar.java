package lfa.practice.lab_one;

import java.util.*;

public class Grammar {
    Set<String> nonTerminals;
    Set<String> terminals;
    List<ProductionRule> productionRules;
    String startSymbol;

    public Grammar(Set<String> nonTerminals, Set<String> terminals, List<ProductionRule> productionRules, String startSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productionRules = productionRules;
        this.startSymbol = startSymbol;
    }

    public String generateString() {
        return generateStringFromSymbol(startSymbol);
    }

    private String generateStringFromSymbol(String symbol) {
        if (!nonTerminals.contains(symbol)) {
            return symbol;
        }

        List<String> possibleProductions = new ArrayList<>();
        for (ProductionRule productionRule : productionRules) {
            if (productionRule.getLeftHandSymbol().equals(symbol)) {
                possibleProductions.add(productionRule.getRightHandSymbol());
            }
        }

        if (possibleProductions.isEmpty()) {
            throw new IllegalArgumentException("No production rules found for symbol: " + symbol);
        }

        Random random = new Random();
        String selectedProduction = possibleProductions.get(random.nextInt(possibleProductions.size()));

        StringBuilder result = new StringBuilder();
        for (char ch : selectedProduction.toCharArray()) {
            result.append(generateStringFromSymbol(String.valueOf(ch)));
        }

        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        List<String> Q = new ArrayList<>(nonTerminals);
        List<String> Sigma = new ArrayList<>(terminals);
        HashMap<HashMap<String, String>, String> delta = new HashMap<>();
        String q0 = startSymbol;
        String F = "X";
        Q.add(F);

        for (ProductionRule productionRule : productionRules) {
            String rightHandSymbol = productionRule.getRightHandSymbol();
            String leftHandSymbol = productionRule.getLeftHandSymbol();
            HashMap<String, String> transitionKey = new HashMap<>();
            if (rightHandSymbol.length() == 2) {
                transitionKey.put(leftHandSymbol, String.valueOf(rightHandSymbol.charAt(0)));
                delta.put(transitionKey, String.valueOf(rightHandSymbol.charAt(1)));
            }
            else {
                transitionKey.put(leftHandSymbol, rightHandSymbol);
                delta.put(transitionKey, F);
            }
        }

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }
}