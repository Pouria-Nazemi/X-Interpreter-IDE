public class Calculations <T> implements Runnable {
    String expression ;
//    private T firstValue ;
//    private T secondValue ;

    public void setFirstValue(T firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(T secondValue) {
        this.secondValue = secondValue;
    }

    public Calculations(String expression) {
        String[] token = expression.split("[ ]+");
        String sign = token[1];
        try {
            int firstVar = Integer.parseInt(token[0]);

        } catch (NumberFormatException e) {
            try {
                float firstVar = Float.parseFloat(token[0]);
            } catch (NumberFormatException numberFormatException) {
                if (Number.getVariables().get(token[0]) instanceof Integer) {
                    int firstVar = Number.getVariables().get(token[0]).intValue();
                } else if (Number.getVariables().get(token[0]) instanceof Double) {
                    double firstVar = Number.getVariables().get(token[0]).floatValue();
                }
            }
        }
        try {
            int secondVar = Integer.parseInt(token[2]);
        } catch (NumberFormatException e) {
            try {
                float secondVar = Float.parseFloat(token[2]);
            } catch (NumberFormatException numberFormatException) {
                if (Number.getVariables().get(token[2]) instanceof Integer) {
                    int secondVar = Number.getVariables().get(token[2]).intValue();
                } else if (Number.getVariables().get(token[2]) instanceof Double) {
                    double secondVar = Number.getVariables().get(token[2]).floatValue();
                }
            }
        }

        Calculations c;
        switch (sign) {
            case "+":
                c = new Add(firstVar, secondVar);
                c.run() ;
                break;
            case "-":
                c = new Reduce(firstVar, secondVar);
                c.run() ;
                break;
            case "*":
                c = new Multiply(firstVar, secondVar);
                c.run() ;
                break;
            case "/":
                c = new Divide(firstVar, secondVar);
                c.run() ;
                break;
        }
    }



    @Override
    public Object run() {
        return null;
    }
}
