
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class LineReader {

    private static int linePointer = 0;
    private ArrayList<Statement> commands = new ArrayList<>();
    private boolean forAlert = false;
    private int forInFor = 0;
    private ArrayList<Statement> commandsOfFor = new ArrayList<>();
    private int tedadFor = 0;
    //private int i = 0;

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
                //TODO calling statementReader function
                statementReader(code);
                runCommands();
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
        //System.out.println(Numbers.getVariables());
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
                    int i = 1;
                    if (parts.length == 2) {
                        if (this.forInFor == 0) {
                            //i = 1;
                            this.tedadFor = 1;
                        }
                        //commands.add(new For(parts[1]));
                        //forAlert = true;
                        while (code.hasNextLine()) {
                            String lines = code.nextLine();
                            if (lines.startsWith("for")) {
                                i++;
                                if (this.forAlert==false ) {
                                    this.tedadFor++;
                                }
                                
                                this.forInFor += 1;
                            }
                            if (lines.equals("end for")) {
                                if (i == 1) {
                                    break;
                                }
                                i--;
                                //commands.add(new For(parts[1]/*, this.commandsOfFor*/));

                                //forAlert = false;
                            }
                            forCommand += lines + "\n";
                        }
                        forAlert = true;
                        Scanner forCommands = new Scanner(forCommand);
                        this.statementReader(forCommands);
                        if (this.tedadFor == 1) {
                            commands.add(new For(parts[1], this.commandsOfFor));
                            this.tedadFor--;
                            this.forInFor = 0;
                            this.commandsOfFor.clear();
                        } else {
                            command = new For(parts[1], this.commandsOfFor);
                            this.commandsOfFor.clear();
                            this.commandsOfFor.add(command);
                            if (this.tedadFor == 1) {
                                this.forInFor = 0;
                            }
                            this.tedadFor--;
                            command = null;

                        }
                        if (this.tedadFor == 0) {
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
                    }
                    /*else {
                        throw new RuntimeException("Invalid command " + "At line: " + this.getLinePointer());
                    }*/
                }
            }
            if (!this.forAlert) {
                if (command != null) {
                    this.commands.add(command);
                }
                //this.commands.add(command);
                if (this.commands.size() != 0) {
                    for (int j = 0; j < this.commands.size(); j++) {
                        this.commands.get(j).run();
                        this.commands.remove(j);
                    }
                }
                //Number a = command.run();
            } else {
                if (command != null) {
                    this.commandsOfFor.add(command);
                }
            }
            /*System.out.println(a);
            System.out.println(Numbers.getVariables());
            System.out.println("line: " + this.getLinePointer() );*/
        }
    }

    public void runCommands() {
        /*for (int i = 0; i < this.commands.size() && this.commands.size() != 0; i++) {
            this.commands.get(i).run();
        }*/
    }

    public static int getLinePointer() {
        return linePointer;
    }

}
