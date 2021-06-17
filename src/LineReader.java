
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class LineReader {

    private static int linePointer = 0;

    private boolean forAlert = false;
    private ArrayList<Statement> commandsOfFor = new ArrayList<>();
    private int forCount = 0;

    public LineReader(String code) {
        try {
            this.variableDeclaration(code);
        } catch (IOException ex) {
            Logger.getLogger(LineReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void variableDeclaration(String codes) throws IOException {
        Scanner code = new Scanner(codes);
        while (code.hasNext()) {
            this.linePointer++;
            String line = code.nextLine();
            if (line != null && line.equals("%%")) {
                statementReader(code);
                break;//End of variable declaration part
            }

            String[] parts = line.trim().split("[ ]+");

            if (!Numbers.getVariables().containsKey(parts[1]) && parts[1].matches("([a-zA-Z$][\\w$]*|[_][\\w$]+)")) {/*
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
                                    + " cannot be assigned to a float variable");
                        }

                    } else if (parts.length == 2) {//example pattern: float x 
                        FloaT var = new FloaT(parts[1]);
                    } else {
                        throw new RuntimeException("Wrong format of float declaration" + "At line: " + this.getLinePointer());
                    }
                } else if (parts.length == 5) {
                    if (parts[1].equals("=") && parts[3].equals("+")) {

                    }
                } else {
                    throw new RuntimeException("Wrong format of variable declaration" + "At line: " + this.getLinePointer());
                }
            } else {
                throw new RuntimeException("Name of the variable is not accepted" + "At line: " + this.getLinePointer());

            }
        }
    }

    public void statementReader(Scanner code) {
        while (code.hasNextLine()) {
            this.linePointer++;
            String line = code.nextLine();
            String[] parts = line.trim().split("[ ]+");
            Statement command = null;
            String forCommand = "";
            switch (parts[0]) {
                case "print":
                    if (parts.length == 2) {
                        command = new Print(parts[1]);
                    } else {
                        throw new RuntimeException("Wrong format of print command" + this.getLinePointer());
                    }
                    break;
                case "for": {
                    int i = 1; //for finding the correct end for command
                    if (parts.length == 2) {
                        if (this.forAlert == false) {
                            this.forCount = 1;
                        }
                        while (code.hasNextLine()) {
                            String lines = code.nextLine();
                            if (lines.startsWith("for")) {
                                i++;
                                if (this.forAlert == false) {
                                    this.forCount++;
                                }
                            }
                            if (lines.equals("end for")) {
                                if (i == 1) {
                                    break;
                                }
                                i--;
                            }
                            forCommand += lines + "\n";
                        }
                        forAlert = true;
                        Scanner forCommands = new Scanner(forCommand);
                        this.statementReader(forCommands);
                        if (this.forCount == 1) {
                            command = new For(parts[1], this.commandsOfFor);
                            this.forCount = 0;
                            this.commandsOfFor.clear();
                        } else {
                            command = new For(parts[1], this.commandsOfFor);
                            this.commandsOfFor.clear();
                            this.commandsOfFor.add(command);
                            this.forCount--;
                            command = null;
                        }
                        if (this.forCount == 0) {
                            forAlert = false;
                        }
                        break;
                    } else {
                        throw new RuntimeException("Invalid for command " + "At line: " + this.getLinePointer());
                    }
                }
                default: {
                    if (Numbers.getVariables().containsKey(parts[0])) {
                        if (parts[1].equals("=")) {
                            if (parts.length == 5) {
                                command = (new Assignment(parts[0], parts[2], parts[3], parts[4]));
                            } else if (parts.length == 3) {
                                command = (new Assignment(parts[0], parts[2]));
                            } else {
                                throw new RuntimeException("Invalid assignment command " + "At line: " + this.getLinePointer());
                            }
                        }
                    } else {
                        throw new RuntimeException("Invalid command " + "At line: " + this.getLinePointer());
                    }
                }
            }
            if (!this.forAlert) {
                command.run();
            } else {
                if (command != null) {
                    this.commandsOfFor.add(command);
                }
            }
            //System.out.println(this.getLinePointer());
        }
    }

    public static int getLinePointer() {
        return linePointer;
    }

}
