
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) {

        new LineReader(
                /*1*/    "int x = 10\n"
                /*2*/  + "int y = 30\n"
                /*3*/  + "int z = 30\n"
                /*4*/  + "int w = 50\n"
                /*5*/  + "int sum\n"
                /*6*/  + "%%\n"
                /*7*/  + "for x\n"
                /*8*/  + "for 2\n"
                /*9*/  + "sum = sum + 2\n"
                /*10*/ + "end for\n"
                /*11*/ + "print sum\n"
                /*12*/ + "end for\n"
                /*13*/ + "print sum\n"
                /*14*/ + "sum = 120\n"
                /*15*/ + "if sum == 120\n"
                /*16*/ + "for 10\n"
                /*17*/ + "print 10\n"
                /*18*/ + "if sum <= 120\n"
                /*19*/ + "for 2\n"
                /*20*/ + "print sum\n"
                /*21*/ + "end for\n"
                /*22*/ + "sum = sum + 1\n"
                /*23*/ + "end if\n"
                /*24*/ + "end for\n"
                /*25*/ + "end if\n"
                /*26*/ + "if w != 50\n"
                /*27*/ + "print w\n"
                /*28*/ + "end if\n"
        );
    }
}
