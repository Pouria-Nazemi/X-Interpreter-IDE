
public class Subtraction extends Statement {

    private Number result;

    public Subtraction(Number a,Number b) {
        this.setResult(this.subNumbers(a, b));
    }

    public void setResult(Number result) {
        this.result = result;
    }

    public Number getResult() {
        return result;
    }

    public Number subNumbers(Number a, Number b) {
         if (a instanceof Float || b instanceof Float) {
            return a.floatValue() - b.floatValue();
        } else {
            return a.intValue() - b.intValue();
        }
    }

    @Override
    public Number run() {
        return this.getResult();
    }
}
