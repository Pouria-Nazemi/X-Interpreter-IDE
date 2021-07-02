package Network;

import GUI.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private final String DELIMITER="~~end~~";
    private Socket socket;
    private PrintWriter output;
    private final Scanner input;
    private Document textBox;
    private String roomKey;
    private String id;
    public Client(Document document,String roomKey) throws IOException {
            textBox=document;
            socket = new Socket("127.0.0.1", 14004);
            input = new Scanner(socket.getInputStream());
            id= input.nextLine();
            input.useDelimiter(DELIMITER);
            System.out.println("id= " + id);
            output = new PrintWriter(
                       new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
            this.roomKey =roomKey;
            output.println(roomKey);
            Thread clientThread = new ClientThread(this);
            clientThread.start();
    }

    public void recognize(String data){
        Matcher matcher= Pattern.compile("(request|accept|insert|delete|getText|setText|message)[|](\\d+)[|] (.+)",Pattern.DOTALL).matcher(data);
        if(matcher.find()){
            switch (matcher.group(1)){
                case "request":
                    output.format("%s |accept|%s| .%s",
                            roomKey,
                            matcher.group(2),
                            DELIMITER);
                    ProgramGUI.changeTeamRole();
                    break;
                case "accept":
                    ProgramGUI.changeTeamRole();
                    break;
                case "getText":
                    try {
                        output.format("%s |sendText|%s| %s%s",
                                roomKey,
                                matcher.group(2),
                                textBox.getText(0, textBox.getLength()),
                                DELIMITER);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "setText":
                    try {
                        textBox.remove(0,textBox.getLength());
                        textBox.insertString(0,matcher.group(3),StyleContext.getDefaultStyleContext().getEmptySet());
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "insert":
                    try {
                        textBox.insertString(Integer.parseInt(matcher.group(2)),matcher.group(3), StyleContext.getDefaultStyleContext().getEmptySet());
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        textBox.remove(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    break;
                case "message":
                    ProgramGUI.addMessage(matcher.group(2),matcher.group(3));
                    break;
            }
        }
    }

    public void insert(DocumentEvent e) throws BadLocationException {
        output.format("%s |insert|%d| %s%s",
                roomKey,
                e.getOffset(),
                textBox.getText(e.getOffset(), e.getLength()),
                DELIMITER);
    }

    public void delete(DocumentEvent e) throws BadLocationException {
        output.format("%s |delete|%d| %d%s",
                roomKey,
                e.getOffset(),
                e.getLength(),
                DELIMITER);
    }

    public void request(){
        output.format("%s |request|%s| .%s",
                roomKey,
                id,
                DELIMITER);
    }

    public void message(String msg){
        output.format("%s |message|%s| %s%s",
                roomKey,
                id,
                msg,
                DELIMITER);
    }

    public String getRoomKey(){
        return roomKey;
    }

    public String getId() {
        return id;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public Scanner getInput() {
        return input;
    }

    public boolean isAlive(){
        return !socket.isClosed();
    }

    public void close(){
        output.format("%s |left|%s| .%s",
                roomKey,
                id,
                DELIMITER);
        output.close();
    }
}

class ClientThread extends Thread{
    private Client client;

    public ClientThread(Client client){
        this.client=client;
    }

    @Override
    public void run() {
        while(client.isAlive()){
            if(client.getInput().hasNext()) {
                String text = client.getInput().next();
                System.out.println(text);
                client.recognize(text);
            }
        }
    }

}

