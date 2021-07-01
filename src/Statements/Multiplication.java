package Statements;

public class Multiplication extends Statement {

    private Number result;

    protected Multiplication(Number a,Number b) {
        this.setResult(this.multNumbers(a, b));
    }

    private void setResult(Number result) {
        this.result = result;
    }

    private Number getResult() {
        return result;
    }

    private Number multNumbers(Number a, Number b) {
          if (a instanceof Float || b instanceof Float) {
            return a.floatValue() * b.floatValue();
        } else {
            return a.intValue() * b.intValue();
        }
    }

    @Override
    public Number run() {
        return this.getResult();//recursive amount of this statement
    }
}
