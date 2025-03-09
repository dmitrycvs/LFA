package lfa.practice;

import lfa.practice.lab_one.FiniteAutomaton;
import lfa.practice.lab_one.Grammar;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "B", "D"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));

        HashMap<String, List<String>> productionRules = new HashMap<>();
        productionRules.put("S", Arrays.asList("aB"));
        productionRules.put("B", Arrays.asList("aD", "bB", "cS"));
        productionRules.put("D", Arrays.asList("aD", "bS", "c"));

        String startSymbol = "S";

        Grammar grammar = new Grammar(nonTerminals, terminals, productionRules, startSymbol);
        System.out.println("Type of grammar: " + grammar.classifyByChomskyHierarchy());

        System.out.println("Generated Strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton automaton = grammar.toFiniteAutomaton();
        System.out.println("\nFinite automaton: " + automaton.toString());

        System.out.println(automaton.stringBelongToLanguage("acb"));
        System.out.println(automaton.stringBelongToLanguage("acaac"));

        List<String> Q = Arrays.asList("q0", "q1", "q2", "q3");
        List<String> Sigma = Arrays.asList("a", "b");
        String q0 = Q.getFirst();
        String F = "q3";

        HashMap<HashMap<String, String>, List<String>> delta = new HashMap<>();
        delta.put(new HashMap<>(Map.of("q0", "a")), Arrays.asList("q0"));
        delta.put(new HashMap<>(Map.of("q0", "b")), Arrays.asList("q1"));
        delta.put(new HashMap<>(Map.of("q1", "a")), Arrays.asList("q1", "q2"));
        delta.put(new HashMap<>(Map.of("q1", "b")), Arrays.asList("q3"));
        delta.put(new HashMap<>(Map.of("q2", "a")), Arrays.asList("q2"));
        delta.put(new HashMap<>(Map.of("q2", "b")), Arrays.asList("q3"));

        automaton = new FiniteAutomaton(Q, Sigma, delta, q0, F);
        grammar = automaton.toGrammar();
        System.out.println("Type of grammar: " + grammar.classifyByChomskyHierarchy());
        System.out.println("Grammar: " + grammar.toString());
        System.out.println("Is DFA: " + automaton.isDeterministic());

        FiniteAutomaton DFA = automaton.toDFA();
        System.out.println("Is DFA: " + DFA.isDeterministic());
    }
}