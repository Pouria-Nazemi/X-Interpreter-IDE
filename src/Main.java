
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        new LineReader(
                "int x = 10\n"
                + "int y = 30\n"
                + "int z\n"
                + "int w = 50\n"
                + "int sum\n"
                + "%%\n"
                + "sum = y + x \n"
                + "sum = sum + z\n"
                + "sum = sum + w\n"
                + "sum = w\n"
                + "print sum\n");

        //System.out.println(Numbers.getVariables());


    }

}
