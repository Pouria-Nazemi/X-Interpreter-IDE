
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        new LineReader("int x = 10\n"
                + "int y = 15\n"
                + "int m = 250\n"
                + "int p\n"
                + "int o\n"
                + "float er\n"
                + "float jh = 100.05\n"
                + "float kj = 55\n"
                + "float lk  = 0.255\n"
                + "%%");

        System.out.println(Number.getVariables());
        
        /*test code*/
        if (Number.getVariables().get("x") instanceof Integer) {
            System.out.println("Integer");
            Number.setVariablesElement("y", Number.getVariables().get("x").intValue() + 5);
            System.out.println(Number.getVariables().get("y"));
        }
        else if (Number.getVariables().get("x") instanceof Float) {
            System.out.println("Float");
            Number.setVariablesElement("y", Number.getVariables().get("x").floatValue() + 5);
            System.out.println(Number.getVariables().get("y"));
        }

    }

}
