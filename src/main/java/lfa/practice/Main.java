package lfa.practice;

import lfa.practice.lab_one.FiniteAutomaton;
import lfa.practice.lab_one.Grammar;
import lfa.practice.lab_one.ProductionRule;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "B", "D"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));

        List<ProductionRule> productionRules = new ArrayList<>();
        productionRules.add(new ProductionRule("S", "aB"));
        productionRules.add(new ProductionRule("B", "aD"));
        productionRules.add(new ProductionRule("B", "bB"));
        productionRules.add(new ProductionRule("D", "aD"));
        productionRules.add(new ProductionRule("D", "bS"));
        productionRules.add(new ProductionRule("B", "cS"));
        productionRules.add(new ProductionRule("D", "c"));

        String startSymbol = "S";

        Grammar grammar = new Grammar(nonTerminals, terminals, productionRules, startSymbol);

        System.out.println("Generated Strings:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton automaton = grammar.toFiniteAutomaton();
        System.out.println("\nFinite automaton: " + automaton.toString());


        System.out.println(automaton.stringBelongToLanguage("acb"));
        System.out.println(automaton.stringBelongToLanguage("acaac"));
    }
}