package lfa.practice.lab_one;

import java.util.*;

public class Grammar {
    Set<String> nonTerminals;
    Set<String> terminals;
    HashMap<String, List<String>> productionRules;
    String startSymbol;

    public Grammar(Set<String> nonTerminals, Set<String> terminals, HashMap<String, List<String>> productionRules, String startSymbol) {
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

        List<String> possibleProductions = productionRules.get(symbol);
        if (possibleProductions == null || possibleProductions.isEmpty()) {
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
        HashMap<HashMap<String, String>, List<String>> delta = new HashMap<>();
        String q0 = startSymbol;
        String F = "X";
        Q.add(F);

        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            String nonTerminal = entry.getKey();
            List<String> productions = entry.getValue();

            for (String production : productions) {
                if (production.length() == 1 && terminals.contains(production)) {
                    HashMap<String, String> transitionKey = new HashMap<>();
                    transitionKey.put(nonTerminal, production);

                    List<String> destinationStates = new ArrayList<>();
                    destinationStates.add(F);
                    delta.put(transitionKey, destinationStates);
                }
                else if (production.length() >= 2) {
                    String firstChar = production.substring(0, 1);
                    String restOfProduction = production.substring(1);

                    if (terminals.contains(firstChar)) {
                        HashMap<String, String> transitionKey = new HashMap<>();
                        transitionKey.put(nonTerminal, firstChar);

                        List<String> destinationStates = new ArrayList<>();
                        destinationStates.add(restOfProduction);
                        delta.put(transitionKey, destinationStates);
                    }
                }
            }
        }

        return new FiniteAutomaton(Q, Sigma, delta, q0, F);
    }

    public String classifyByChomskyHierarchy() {
        if (isRegularGrammar()) {
            return "Type-3 Regular Grammar";
        } else if (isContextFreeGrammar()) {
            return "Type-2 Context-Free Grammar";
        } else if (isContextSensitiveGrammar()) {
            return "Type-1 Context-Sensitive Grammar";
        } else {
            return "Type-0 Unrestricted Grammar";
        }
    }

    private boolean isRegularGrammar() {
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            List<String> productions = entry.getValue();

            for (String production : productions) {
                if (production.isEmpty()) {
                    return false;
                }

                boolean isSingleTerminal = production.length() == 1 && terminals.contains(production);
                boolean isTerminalNonTerminal = false;
                if (production.length() == 2) {
                    String firstChar = production.substring(0, 1);
                    String secondChar = production.substring(1);
                    isTerminalNonTerminal = terminals.contains(firstChar) && nonTerminals.contains(secondChar);
                }

                if (!isSingleTerminal && !isTerminalNonTerminal) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isContextFreeGrammar() {
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            String leftHandSide = entry.getKey();

            if (!nonTerminals.contains(leftHandSide) || leftHandSide.length() != 1) {
                return false;
            }
        }
        return true;
    }

    private boolean isContextSensitiveGrammar() {
        for (Map.Entry<String, List<String>> entry : productionRules.entrySet()) {
            String leftHandSide = entry.getKey();
            List<String> productions = entry.getValue();

            for (String rightHandSide : productions) {
                if (rightHandSide.isEmpty()) {
                    if (!leftHandSide.equals(startSymbol)) {
                        return false;
                    }

                    for (List<String> allProductions : productionRules.values()) {
                        for (String rhs : allProductions) {
                            if (rhs.contains(startSymbol)) {
                                return false;
                            }
                        }
                    }
                }

                if (rightHandSide.length() < leftHandSide.length() && !(leftHandSide.equals(startSymbol) && rightHandSide.isEmpty())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "NonTerminals: " + nonTerminals.toString() + "\n" +
                "Terminals: " + terminals.toString() + "\n" +
                "Start Symbol: " + startSymbol + "\n" +
                "Production Rules: " + productionRules.toString() + "\n";
    }
}