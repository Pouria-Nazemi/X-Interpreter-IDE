
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

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
                + "for 2\n"
                + "for 3\n"
                + "for 2\n"
                + "for 4\n"
                + "sum = sum + 1\n"
                + "print sum\n"
                + "end for\n"
               /* + "print 10\n"*/
                + "end for\n"
                + "end for\n"
                + "end for\n"
                + "for 5\n"
                + "print w\n"
                + "end for\n"
                + "print 15\n"
        );
    }

}
