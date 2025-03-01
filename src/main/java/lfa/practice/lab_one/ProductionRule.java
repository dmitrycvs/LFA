package lfa.practice.lab_one;

public class ProductionRule {
    private final String leftHandSymbol;
    private final String rightHandSymbol;

    public ProductionRule(String leftHandSymbol, String rightHandSymbol) {
        this.leftHandSymbol = leftHandSymbol;
        this.rightHandSymbol = rightHandSymbol;
    }

    public String getLeftHandSymbol() {
        return leftHandSymbol;
    }

    public String getRightHandSymbol() {
        return rightHandSymbol;
    }
}