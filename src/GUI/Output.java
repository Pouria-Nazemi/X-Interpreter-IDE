package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Output extends JFrame{
    private static JFrame currentOutput;
    public Output(){
        if(currentOutput!=null){
            currentOutput.dispose();
        }
        currentOutput=this;
        setTitle("Output");
        setSize(300,300);
        JTextArea textArea=new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0, 4, 36));
        textArea.setForeground(Color.white);
        textArea.setFont(textArea.getFont().deriveFont(18f));
        JScrollPane scrollPane=new JScrollPane(textArea);
        add(scrollPane);
        PrintStream out =new PrintStream(new CustomOutputStream(textArea));
        System.setOut(out);
    }
}


class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
        // keeps the textArea up to date
        textArea.update(textArea.getGraphics());
    }
}
