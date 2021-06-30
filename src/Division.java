
public class Division extends Statement {

    private Number result;

    protected Division(Number a,Number b) {
        this.setResult(this.divNumbers(a, b));
    }

    public void setResult(Number result) {
        this.result = result;
    }

    public Number getResult() {
        return result;
    }

    public Number divNumbers(Number a, Number b) {
         if (a instanceof Float || b instanceof Float) {
            return a.floatValue() / b.floatValue();
        } else {
            return a.intValue() / b.intValue();
        }
    }

    @Override
    public Number run() {
        return this.getResult();//recursive amount of this statement
    }
}
