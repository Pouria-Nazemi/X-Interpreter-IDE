
public class Addition extends Statement {

    private Number result;

    protected Addition(Number a, Number b) {
        this.setResult(this.addNumbers(a, b));
    }

    public void setResult(Number result) {
        this.result = result;
    }

    public Number getResult() {
        return result;
    }

    public Number addNumbers(Number a, Number b) {
        if (a instanceof Float || b instanceof Float) {
            return a.floatValue() + b.floatValue();
        } else {
            return a.intValue() + b.intValue();
        }
    }

    @Override
    public Number run() {
        return this.getResult();//recursive amount of this statement
    }

}
