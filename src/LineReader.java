
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class LineReader {

    private int linePointer = 0;

    public LineReader(String code) {
        try {
            this.variableDeclaration(code);
        } catch (IOException ex) {
            Logger.getLogger(LineReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void variableDeclaration(String codes) throws IOException {
        //BufferedReader code = new BufferedReader(new StringReader(codes));
        Scanner code = new Scanner(codes);
        while (code.hasNext()/*code.readLine()!= null*/) {
            this.linePointer++;
            //String line = code.readLine();
            String line = code.nextLine();
            if (line != null && line.equals("%%")) {
                //TODO calling statermentReader function
                break;//End of variable declaration part
            }

            String[] parts = line.split("[ ]+");

            if (!Number.getVariables().containsKey(parts[1]) && parts[1].matches("([a-zA-Z$][\\w$]*|[_][\\w$]+)")) {/*
                    Check the name of new variable not to be reapeted and if it matches java format of variable name decleration
                 */
                if (parts[0].equals("int")) { //Integer declaration

                    if (parts.length == 4 && parts[2].equals("=")) { //example pattern: int x = 15
                        try {
                            int value = Integer.parseInt(parts[3]);
                            Int var = new Int(parts[1], value);
                        } catch (NumberFormatException ex) {
                            throw new RuntimeException("The value you entered at line: " + this.getLinePointer()
                                    + " cannot be assigned to an Integer variable");
                        }

                    } else if (parts.length == 2) {//example pattern: int x
                        Int var = new Int(parts[1]);
                    } else {
                        throw new RuntimeException("Wrong format of int declaration." + "At line: " + this.getLinePointer());
                    }
                } else if (parts[0].equals("float")) {//Float declaration

                    if (parts.length == 4 && parts[2].equals("=")) {//example pattern: float x = 15.00
                        try {
                            float value = Float.parseFloat(parts[3]);
                            FloaT var = new FloaT(parts[1], value);
                        } catch (NumberFormatException ex) {
                            throw new RuntimeException("The value you entered at line: " + this.getLinePointer()
                                    + " cannot be assigned to an float variable");
                        }

                    } else if (parts.length == 2) {//example pattern: float x 
                        FloaT var = new FloaT(parts[1]);
                    } else {
                        throw new RuntimeException("Wrong format of float declaration" + "At line: " + this.getLinePointer());
                    }
                } else {
                    throw new RuntimeException("Wrong format of variable declaration" + "At line: " + this.getLinePointer());
                }
            } else {
                throw new RuntimeException("Name of the variable is not accepted" + "At line: " + this.getLinePointer());

            }
        }
    }

    public int getLinePointer() {
        return linePointer;
    }

}
