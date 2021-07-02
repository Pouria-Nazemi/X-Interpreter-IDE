package Interpreter;

import Statements.*;
import Variables.FloatVar;
import Variables.IntVar;
import Variables.Numbers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LineReader {
    
    private ArrayList<String> program = new ArrayList<>(); //Statements.For storing all lines of program to be checked
    private ArrayList<ArrayList<Statement>> commands = new ArrayList<ArrayList<Statement>>(); //Statements.For storing nested if and for commands
    private int ifForNum = -1;  //Use as the index of above Arraylist in nested if and for commands

    private ArrayList<String> getProgram() {
        return program;
    }

    private ArrayList<ArrayList<Statement>> getCommands() {
        return commands;
    }

    private int getIfForNum() {
        return ifForNum;
    }

    public LineReader(String code) {
        Variables.Numbers.getVariables().clear();
        this.variableDeclaration(code);
    }

    private void linesOfProgram(Scanner codes) {//Adding all lines to the arraylist for control and getting number of line
        int numOfFor, numOFif,numOfSepearator = 0;
        while (codes.hasNextLine()) {
            String line = codes.nextLine();
            this.getProgram().add(line);
            if(line.trim().equals("%%")){ // Statements.For checking that only one '%%' appears in code
                numOfSepearator++;
            }
        }
        if(numOfSepearator>1){
            throw new InterpretingException("More than one '%%' character");
        }
        if (numOfSepearator == 0) { // Statements.For checking the existance of '%%'
            throw new InterpretingException("Missing '%%'");
        }
        for (int i = 0; i < this.getProgram().size() && this.getProgram().get(i) != null; i++) { // Statements.For checking that all 'for' block of commands are closed
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
                    throw new InterpretingLineException("Block of for command is not closed",(i+1));
                }
            }
        }
        for (int i = 0; i < this.getProgram().size() && this.getProgram().get(i) != null; i++) {// Statements.For checking that all 'if' block of commands are closed
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
                    throw new InterpretingLineException("Block of if command is not closed",(i+1));
                }
            }
        }
    }

    private int LineFinder(String line) {
        return this.getProgram().indexOf(line) + 1;
    }

    private void variableDeclaration(String codes){
        Scanner code = new Scanner(codes);
        linesOfProgram(new Scanner(codes));
        int lineNumber = 0;
        while (code.hasNext()) {
            lineNumber++;
            String line = code.nextLine();

            if(line.trim().length()==0){ //Ignoring empty lines
                continue;
            }
            if(line.trim().startsWith("//")){ //Skipping comments in code
                continue;
            }
            if (line.trim().equals("%%")) {//end the phase of variable declaration
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
                            IntVar var = new IntVar(parts[1], value, lineNumber);
                        } catch (NumberFormatException ex) {
                            throw new InterpretingLineException("The value you entered cannot be assigned to an Integer variable" , lineNumber);
                        }

                    } else if (parts.length == 2) {//example pattern: int x
                        IntVar var = new IntVar(parts[1], lineNumber);
                    } else {
                        throw new InterpretingLineException("Wrong format of int declaration." , lineNumber);
                    }
                } else if (parts[0].equals("float")) {//Float declaration

                    if (parts.length == 4 && parts[2].equals("=")) {//example pattern: float x = 15.00
                        try {
                            float value = Float.parseFloat(parts[3]);
                            FloatVar var = new FloatVar(parts[1], value, lineNumber);
                        } catch (NumberFormatException ex) {
                            throw new InterpretingLineException("The value you entered cannot be assigned to a float variable",lineNumber);
                        }
                    } else if (parts.length == 2) {//example pattern: float x 
                        FloatVar var = new FloatVar(parts[1], lineNumber);
                    } else {
                        throw new InterpretingLineException("Wrong format of float declaration " , lineNumber);
                    }
                } else {
                    throw new InterpretingLineException("Wrong format of variable declaration " , lineNumber);
                }
            } else {
                throw new InterpretingLineException("Name of the variable is not accepted " , lineNumber);

            }
        }
    }
    private boolean varNameValidation(String name){
        if(!Variables.Numbers.getVariables().containsKey(name) && name.matches("([a-zA-Z$][\\w$]*|[_][\\w$]+)")
        && !name.equals("for") &&!name.equals("if")&&!name.equals("print")&&!name.equals("end")){
            return true;
        }
        else{
            return false;
        }
    }

    private void StatementReader(Scanner code) {
        while (code.hasNextLine()) {
            String line = code.nextLine();
            if(line.trim().length()==0){//Ignoring empty lines
                continue;
            }
            if (line.trim().startsWith("//")) { //Skipping comments in code
                line = code.nextLine();
            }
            String[] parts = line.trim().split("[ ]+");
            Statement command = null;
            StringBuilder forCommand = new StringBuilder("");//Statements.For storing each 'for' block
            StringBuilder ifCommand = new StringBuilder("");//Statements.For storing each 'if' block
            int lineNumber = this.LineFinder(line);//getting the number of line
            switch (parts[0]) {
                case "print": {
                    if (parts.length == 2) {
                        command = new Print(parts[1],lineNumber);
                    } else {
                        throw new InterpretingLineException("Wrong format of print command " , lineNumber);
                    }
                    break;
                }

                case "for": {
                    if (parts.length == 2) {
                        this.ifForNum++;
                        this.getCommands().add(this.getIfForNum(), new ArrayList<Statement>());
                        this.getCommands().get(this.getIfForNum()).add(new For(parts[1], lineNumber));
                        int i = 1; //for finding the correct end for command
                        while (code.hasNextLine()) {//Command Block Of Statements.For
                            String lines = code.nextLine();
                            lines = lines.trim();
                            if(lines.length()==0){
                                continue;
                            }
                            if (lines.startsWith("for")) {
                                i++;
                            }
                            if (lines.equals("end for")) { // to find the last 'end for'
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
                        throw new InterpretingLineException("Invalid for command " , lineNumber);
                    }
                }

                case "if": {
                    if (parts.length == 4) {
                        this.ifForNum++;
                        this.getCommands().add(this.getIfForNum(), new ArrayList<Statement>());
                        this.getCommands().get(this.getIfForNum()).add(new If(parts[1], parts[2], parts[3], lineNumber));
                        int i = 1; //for finding the correct end if command
                        while (code.hasNextLine()) {//Command Block Of Statements.If
                            String lines = code.nextLine();
                            lines = lines.trim();
                            if(lines.length()==0){
                                continue;
                            }
                            if (lines.startsWith("if")) {
                                i++;
                            }
                            if (lines.equals("end if")) {// to find the last 'end for'
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
                        throw new InterpretingLineException("Invalid if command " , lineNumber);
                    }
                }

                default: {
                    if (Variables.Numbers.getVariables().containsKey(parts[0])) {
                        /*Statements.Assignment statement*/
                        if (parts[1].equals("=")) {
                            if (parts.length == 5) {
                                command = (new Assignment(parts[0], parts[2], parts[3], parts[4], lineNumber));
                            } else if (parts.length == 3) {
                                command = (new Assignment(parts[0], parts[2], lineNumber));
                            } else {
                                throw new InterpretingLineException("Invalid assignment command " , lineNumber);
                            }
                        } else {
                            throw new InterpretingLineException("Invalid assignment operator " , lineNumber);
                        }
                    } else {
                        throw new InterpretingLineException("Invalid command " , lineNumber);
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

    private Statement addInnerCommands(Statement supCommand, ArrayList<Statement> commands) { /* adding all sub commands of
     a for or if command into the commands of a if or for instance and return that instance*/
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
            throw new RuntimeException("Wrong Super Command");
        }
    }
}
