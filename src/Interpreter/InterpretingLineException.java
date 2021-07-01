package Interpreter;

public class InterpretingLineException extends RuntimeException {

    private int lineNumber;

    public InterpretingLineException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
