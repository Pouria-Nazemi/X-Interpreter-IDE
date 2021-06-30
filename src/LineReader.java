
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class LineReader {
    
    private ArrayList<String> program = new ArrayList<>();
    private ArrayList<ArrayList<Statement>> commands = new ArrayList<ArrayList<Statement>>();
    private int ifForNum = -1; 

    

    public ArrayList<String> getProgram() {
        return program;
    }

    public ArrayList<ArrayList<Statement>> getCommands() {
        return commands;
    }
    
    public int getIfForNum() {
        return ifForNum;
    }

    
    
    

    public LineReader(String code) {
        try {
            this.variableDeclaration(code);
        } catch (IOException ex) {
            Logger.getLogger(LineReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void linesOfProgram(Scanner codes) {
        int numOfFor, numOFif,numOfSepearator = 0;
        while (codes.hasNextLine()) {
            String line = codes.nextLine();
            this.getProgram().add(line);
            if(line.trim() != null && line.trim().equals("%%")){
                numOfSepearator++;
            }
        }
        if(numOfSepearator>1){
            throw new RuntimeException("More than one '%%' character");
        }
        if (!this.getProgram().contains("%%")) {
            throw new RuntimeException("Missing '%%'");
        }
        for (int i = 0; i < this.getProgram().size() && this.getProgram().get(i) != null; i++) {
            numOfFor = 0;
            if (this.getProgram().get(i).trim().startsWith("for")) {
                numOfFor++;
                for (int j = i + 1; j < this.getProgram().size() && this.getProgram().get(j) != null; j++) {
                    if (this.getProgram().get(j).trim().startsWith("for")) {
                        numOfFor++;
                    }
                    if (this.getProgram().get(j).trim().startsWith("end for")) {
                        numOfFor--;
                    }
                    if (numOfFor == 0) {
                        break;
                    }

                }
                if (numOfFor != 0) {
                    throw new RuntimeException("Block of for command At line: " + (i + 1) + " is not closed");
                }
            }
        }
        for (int i = 0; i < this.getProgram().size() && this.getProgram().get(i) != null; i++) {
            numOFif = 0;
            if (this.getProgram().get(i).trim().startsWith("if")) {
                numOFif++;
                for (int j = i+1; j < this.getProgram().size() && this.getProgram().get(j) != null; j++) {
                    if (this.getProgram().get(j).trim().startsWith("if")) {
                        numOFif++;
                    }
                    if (this.getProgram().get(j).trim().startsWith("end if")) {
                        numOFif--;
                    }
                    if (numOFif == 0) {
                        break;
                    }

                }
                if (numOFif != 0) {
                    throw new RuntimeException("Block of if command At line: " + (i + 1) + " is not closed");
                }
            }
        }
    }

    public int LineFinder(String line) {
        return this.getProgram().indexOf(line) + 1;
    }

    public void variableDeclaration(String codes) throws IOException {
        Scanner code = new Scanner(codes);
        linesOfProgram(new Scanner(codes));
        int lineNumber = 0;
        while (code.hasNext()) {
            lineNumber++;
            String line = code.nextLine();

            if(line.trim().length()==0){
                continue;
            }
            if(line.trim().startsWith("//")){
                continue;
            }
            if (line.trim().equals("%%")) {
                StatementReader(code);
                break;//End of variable declaration part
            }


            String[] parts = line.trim().split("[ ]+");

            if (varNameValidation(parts[1])) {/*
                    Check the name of new variable not to be repeated and if it matches java format of variable name declaration
                 */
                if (parts[0].equals("int")) { //Integer declaration

                    if (parts.length == 4 && parts[2].equals("=")) { //example pattern: int x = 15
                        try {
                            int value = Integer.parseInt(parts[3]);

                            Int var = new Int(parts[1], value, lineNumber);
                        } catch (NumberFormatException ex) {
                            throw new RuntimeException("The value you entered At line: " + lineNumber
                                    + " cannot be assigned to an Integer variable");
                        }

                    } else if (parts.length == 2) {//example pattern: int x
                        Int var = new Int(parts[1], lineNumber);
                    } else {
                        throw new RuntimeException("Wrong format of int declaration." + "At line: " + lineNumber);
                    }
                } else if (parts[0].equals("float")) {//Float declaration

                    if (parts.length == 4 && parts[2].equals("=")) {//example pattern: float x = 15.00
                        try {
                            float value = Float.parseFloat(parts[3]);
                            FloaT var = new FloaT(parts[1], value, lineNumber);
                        } catch (NumberFormatException ex) {
                            throw new RuntimeException("The value you entered At line: " + lineNumber
                                    + " cannot be assigned to a float variable");
                        }

                    } else if (parts.length == 2) {//example pattern: float x 
                        FloaT var = new FloaT(parts[1], lineNumber);
                    } else {
                        throw new RuntimeException("Wrong format of float declaration " + "At line: " + lineNumber);
                    }

                } else {
                    throw new RuntimeException("Wrong format of variable declaration " + "At line: " + lineNumber);
                }
            } else {
                throw new RuntimeException("Name of the variable is not accepted " + "At line: " + lineNumber);

            }
        }
    }
    public boolean varNameValidation(String name){
        if(!Numbers.getVariables().containsKey(name) && name.matches("([a-zA-Z$][\\w$]*|[_][\\w$]+)")
        && !name.equals("for") &&!name.equals("if")&&!name.equals("print")&&!name.equals("end")){
            return true;
        }
        else{
            return false;
        }
    }

    public void StatementReader(Scanner code) {

        while (code.hasNextLine()) {
            String line = code.nextLine();
            if(line.trim().length()==0){
                continue;
            }
            if (line.trim().startsWith("//")) { //comment
                line = code.nextLine();
            }
            String[] parts = line.trim().split("[ ]+");
            Statement command = null;
            StringBuilder forCommand = new StringBuilder("");
            StringBuilder ifCommand = new StringBuilder("");
            int lineNumber = this.LineFinder(line);
            switch (parts[0]) {
                case "print": {
                    if (parts.length == 2) {
                        command = new Print(parts[1],lineNumber);
                    } else {
                        throw new RuntimeException("Wrong format of print command " + lineNumber);
                    }
                    break;
                }

                case "for": {
                    if (parts.length == 2) {
                        this.ifForNum++;
                        this.getCommands().add(this.getIfForNum(), new ArrayList<Statement>());
                        this.getCommands().get(this.getIfForNum()).add(new For(parts[1], lineNumber));
                        int i = 1; //for finding the correct end for command

                        while (code.hasNextLine()) {//Command Block Of For
                            String lines = code.nextLine();
                            lines = lines.trim();
                            if(lines.length()==0){
                                continue;
                            }
                            if (lines.startsWith("for")) {
                                i++;
                            }
                            if (lines.equals("end for")) {
                                if (i == 1) {
                                    break;
                                }
                                i--;
                            }
                            forCommand.append(lines + "\n");
                        }
                        Scanner forCommands = new Scanner(forCommand.toString());
                        this.StatementReader(forCommands);

                        Statement supCommand = this.addInnerCommands(this.getCommands().get(this.getIfForNum()).get(0), this.getCommands().get(this.getIfForNum()));
                        this.getCommands().get(this.getIfForNum()).clear();
                        this.ifForNum--;
                        if (this.getIfForNum() >= 0) {
                            this.getCommands().get(this.getIfForNum()).add(supCommand);
                        } else if (this.getIfForNum() == -1) {
                            this.getCommands().get(this.getIfForNum() + 1).add(supCommand);
                        }

                        break;
                    } else {
                        throw new RuntimeException("Invalid for command " + "At line: " + lineNumber);
                    }
                }

                case "if": {
                    if (parts.length == 4) {
                        this.ifForNum++;
                        this.getCommands().add(this.getIfForNum(), new ArrayList<Statement>());
                        this.getCommands().get(this.getIfForNum()).add(new If(parts[1], parts[2], parts[3], lineNumber));
                        int i = 1; //for finding the correct end if command

                        while (code.hasNextLine()) {//Command Block Of If
                            String lines = code.nextLine();
                            lines = lines.trim();
                            if(lines.length()==0){
                                continue;
                            }
                            if (lines.startsWith("if")) {
                                i++;
                            }
                            if (lines.equals("end if")) {
                                if (i == 1) {
                                    break;
                                }
                                i--;
                            }
                            ifCommand.append(lines + "\n");
                        }
                        Scanner ifCommands = new Scanner(ifCommand.toString());
                        this.StatementReader(ifCommands);
                        Statement supCommand = this.addInnerCommands(this.getCommands().get(this.getIfForNum()).get(0), this.getCommands().get(this.getIfForNum()));
                        this.getCommands().get(this.getIfForNum()).clear();
                        this.ifForNum--;
                        if (this.getIfForNum() >= 0) {
                            this.getCommands().get(this.getIfForNum()).add(supCommand);
                        } else if (this.getIfForNum() == -1) {
                            this.getCommands().get(this.getIfForNum() + 1).add(supCommand);
                        }
                        break;
                    } else {
                        throw new RuntimeException("Wrong format of if command " + lineNumber);
                    }
                }

                default: {
                    if (Numbers.getVariables().containsKey(parts[0])) {
                        if (parts[1].equals("=")) {
                            if (parts.length == 5) {
                                command = (new Assignment(parts[0], parts[2], parts[3], parts[4], lineNumber));
                            } else if (parts.length == 3) {
                                command = (new Assignment(parts[0], parts[2], lineNumber));
                            } else {
                                throw new RuntimeException("Invalid assignment command " + "At line: " + lineNumber);
                            }
                        } else {
                            throw new RuntimeException("Invalid assignment operator " + "At line: " + lineNumber);
                        }
                    } else {
                        throw new RuntimeException("Invalid command " + "At line: " + lineNumber);
                    }
                }
            }
            if (this.getIfForNum() < 0) {
                if (command != null) {
                    command.run();
                } else {
                    this.getCommands().get(0).get(0).run();
                    this.getCommands().clear();
                }
            } else {
                if (command != null) {
                    this.getCommands().get(this.getIfForNum()).add(command);
                }
            }
        }
    }

    public Statement addInnerCommands(Statement supCommand, ArrayList<Statement> commands) {
        if (supCommand instanceof For) {
            For command = (For) supCommand;
            for (int i = 1; i < commands.size(); i++) {
                command.getInForCommands().add(commands.get(i));
            }
            return command;
        } else if (supCommand instanceof If) {
            If command = (If) supCommand;
            for (int i = 1; i < commands.size(); i++) {
                command.getInIfCommands().add(commands.get(i));
            }
            return command;
        } else {
            throw new RuntimeException("Wrong Super Comamnd");
        }
    }
}
