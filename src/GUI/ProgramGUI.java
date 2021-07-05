package GUI;

import Interpreter.*;
import Network.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ProgramGUI {
    private static JTextPane lineNumbersPane;
    private static JTextPane textBox;
    private static int errorLine;
    private static String errorMessage="";
    private static String currentPath;
    private static JPanel jPanel;
    private static GitManager git= new GitManager();
    private static Client client;
    private static DocumentListener clientListener;
    private static JTextArea chatBox;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFrame frame=new JFrame("X Interpreter");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon("images/icon.png").getImage());
        createTextBox(frame);
        createButtons(frame);
        createChatBox(frame);
        createStyles();
        frame.setSize(700,500);
        frame.setLocation(300,150);
        //frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static void createButtons(JFrame frame) {
        jPanel=new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".x files","x"));
        Border buttonBorder=BorderFactory.createEmptyBorder(4,6,4,6);
        Image openImage=new ImageIcon("images/open.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton openButton=new JButton(new ImageIcon(openImage));
        openButton.setBorder(buttonBorder);
        openButton.setToolTipText("Open");
        openButton.setFocusPainted(false);

        JPanel openPanel=new JPanel();
        openPanel.setLayout(new BoxLayout(openPanel,BoxLayout.Y_AXIS));
        openPanel.setVisible(false);

        Image newFileImage=new ImageIcon("images/d-newFile.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton newFileButton=new SubButton(newFileImage,"Create new file");
        openPanel.add(newFileButton);

        Image browseImage=new ImageIcon("images/d-computer.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton browseButton=new SubButton(browseImage,"select from computer");
        browseButton.addActionListener(e -> {
            Open(textBox);
            openPanel.setVisible(false);
        });
        openPanel.add(browseButton);

        Image cloudImage=new ImageIcon("images/d-cloud.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton cloudButton=new SubButton(cloudImage,"Clone from github");
        cloudButton.addActionListener(e -> gitClone());
        openPanel.add(cloudButton);

        openButton.addActionListener(e -> {
            openPanel.setVisible(!openPanel.isVisible());
        });
        jPanel.add(openButton);
        jPanel.add(openPanel);
        Image saveImage=new ImageIcon("images/save.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton saveButton=new JButton(new ImageIcon(saveImage));
        saveButton.setToolTipText("Save");
        saveButton.setBorder(buttonBorder);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> {
            save(textBox);

        });
        jPanel.add(saveButton);
        Image runImage=new ImageIcon("images/run.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton runButton=new JButton(new ImageIcon(runImage));
        runButton.setToolTipText("Run");
        runButton.setBorder(buttonBorder);
        runButton.setFocusPainted(false);

        runButton.addActionListener(event -> {
            try {
                 JFrame output=new Output();
                 new LineReader(textBox.getText());
                 output.setVisible(true);
            }catch (InterpretingLineException e){
                showError(e.getLineNumber(),e.getMessage());
            }catch (InterpretingException e) {
                JOptionPane.showMessageDialog(frame,e.getMessage() , "build failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        jPanel.add(runButton);

        Image gitImage=new ImageIcon("images/git-disable.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton gitButton=new JButton(new ImageIcon(gitImage));
        gitButton.setToolTipText("Git");
        gitButton.setBorder(buttonBorder);
        gitButton.setFocusPainted(false);
        gitButton.setEnabled(false);
        jPanel.add(gitButton);

        Image gitActiveImage=new ImageIcon("images/git.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton gitActiveButton=new JButton(new ImageIcon(gitActiveImage));
        gitActiveButton.setBorder(buttonBorder);
        gitActiveButton.setVisible(false);
        gitActiveButton.setFocusPainted(false);
        jPanel.add(gitActiveButton);
        gitButton.addActionListener(actionEvent -> {
            showGitSettings();
        });

        JPanel gitPanel=new JPanel();
        gitPanel.setLayout(new BoxLayout(gitPanel,BoxLayout.Y_AXIS));
        gitPanel.setVisible(false);

        Image pushImage=new ImageIcon("images/git-push.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton pushButton=new SubButton(pushImage,"Push");

        Image commitImage=new ImageIcon("images/git-commit.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton commitButton=new SubButton(commitImage,"Commit");
        commitButton.addActionListener(e -> {
            String msg= JOptionPane.showInputDialog("enter commit message:");
            if (msg!=null){
                git.commit(msg);
                JOptionPane.showMessageDialog(null,"Committed");
            }
        });

        Image pullImage=new ImageIcon("images/git-pull.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton pullButton=new SubButton(pullImage,"Pull");
        pullButton.addActionListener(e ->{
            boolean successful= git.pull();
            if (successful){
                JOptionPane.showMessageDialog(null,"Successful :)");
            }else
                JOptionPane.showMessageDialog(null,"Failed","Error!",JOptionPane.ERROR_MESSAGE);

        });

        Image gSettingImage=new ImageIcon("images/git-setting.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton gSettingButton=new SubButton(gSettingImage,"Git Settings");

        gitPanel.add(commitButton);
        gitPanel.add(pushButton);
        gitPanel.add(pullButton);
        gitPanel.add(gSettingButton);
        gSettingButton.addActionListener(e -> showGitSettings());
        newFileButton.addActionListener(e -> {
            boolean confirm=true;
            if(!textBox.getText().equals("")) {
                int result=JOptionPane.showConfirmDialog(null,"Unsaved changes will be lost. Are you sure?","",JOptionPane.YES_NO_OPTION);
                if (result!= JOptionPane.YES_OPTION)
                    confirm=false;
            }
            if (confirm) {
                textBox.setText("");
                currentPath = "";
                jPanel.getComponent(4).setEnabled(false);
                git=new GitManager();
                checkGit();
                openPanel.setVisible(false);
            }
        });
        jPanel.add(gitPanel);

        pushButton.addActionListener(actionEvent -> {
            if (!git.isRemote())
                gSettingButton.doClick();
            else {
                try {
                    git.push();
                    JOptionPane.showMessageDialog(null,"Successful!");
                } catch (GitAPIException | IOException e) {
                    JOptionPane.showMessageDialog(null,e.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gitActiveButton.addActionListener(actionEvent -> {
            gitPanel.setVisible(!gitPanel.isVisible());
        });

        Image connectImage=new ImageIcon("images/team-disable.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton connectButton=new JButton(new ImageIcon(connectImage));
        connectButton.setToolTipText("Code with friends!");
        connectButton.setBorder(buttonBorder);
        connectButton.setFocusPainted(false);
        Image connectedImage=new ImageIcon("images/team-enable.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        JButton connectedButton=new JButton(new ImageIcon(connectedImage));
        connectedButton.setToolTipText("Connected");
        connectedButton.setBorder(buttonBorder);
        connectedButton.setFocusPainted(false);
        connectedButton.setVisible(false);
        connectedButton.addActionListener(e -> {
            showClientMenu(frame);
        });
        connectButton.addActionListener(e -> {
            String roomName= JOptionPane.showInputDialog("Enter room name:");
            if (roomName!=null && !roomName.equals("")) {
                try {
                    client = new Client(textBox.getDocument(), roomName);
                    changeTeamRole();
                    connectedButton.setVisible(true);
                    connectButton.setVisible(false);
                    frame.getLayeredPane().getComponentsInLayer(0)[0].setVisible(true);//messages button
                    JOptionPane.showMessageDialog(textBox.getParent(),"Successfully Joined to room");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(jPanel.getParent(),ioException.getMessage(),"Failed",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        jPanel.add(connectButton);
        jPanel.add(connectedButton);
        frame.getContentPane().add(jPanel, BorderLayout.EAST);
    }

    private static void createTextBox(JFrame frame) {
        textBox=new JTextPane(){
            @Override
            public String getToolTipText(MouseEvent event) {
                if(!errorMessage.equals("")) {
                    try {
                        int lineNumber = getLineNumber(viewToModel(event.getPoint()));
                        if (lineNumber == errorLine)
                            return errorMessage;
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }

                return super.getToolTipText(event);
            }
        };
        textBox.setToolTipText("");
        textBox.setFont(new Font(Font.DIALOG,Font.PLAIN,15));
        textBox.setBackground(Color.white);
        textBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
                Runnable doHighlight = new java.lang.Runnable() {
                    @Override
                    public void run() {

                        try {
                            if(textBox.isEditable())
                                showPopup();
                            updateTextStyles();
                            if(getLineNumber(e.getOffset())==errorLine)
                                clearError();

                        } catch (BadLocationException badLocationException) {
                            badLocationException.printStackTrace();
                        }
                    }
                };
                SwingUtilities.invokeLater(doHighlight);

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
                Runnable doHighlight = new java.lang.Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(textBox.isEditable())
                                showPopup();
                            updateTextStyles();
                        } catch (BadLocationException badLocationException) {
                            badLocationException.printStackTrace();
                        }
                    }
                };
                SwingUtilities.invokeLater(doHighlight);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineNumbers();
            }
        });
        JScrollPane scrollPane=new JScrollPane(textBox);
        lineNumbersPane =new JTextPane();
        lineNumbersPane.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
        lineNumbersPane.setEditable(false);
        lineNumbersPane.setBackground(Color.lightGray);
        scrollPane.setRowHeaderView(lineNumbersPane);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private static void createChatBox(JFrame frame){
        JPanel chatPanel=new JPanel(new BorderLayout());
        chatPanel.setVisible(false);
        chatBox= new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBackground(new Color(231, 231, 231));
        chatBox.setLineWrap(true);
        JScrollPane scrollPane=new JScrollPane(chatBox);
        scrollPane.setPreferredSize(new Dimension(150,400));
        chatPanel.add(scrollPane,BorderLayout.CENTER);
        JTextField inputField=new JTextField(10);
        JPanel inputPanel=new JPanel(new BorderLayout());
        inputPanel.add(inputField,BorderLayout.NORTH);
        JButton button=new JButton("Send");
        button.addActionListener(e -> {
            if(!inputField.getText().equals("")) {
                client.message(inputField.getText());
                inputField.setText("");
            }
        });
        button.setBorder(new EmptyBorder(10,50,10,50));
        inputPanel.add(button,BorderLayout.CENTER);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!inputField.getText().equals("") && e.getKeyCode()==KeyEvent.VK_ENTER)
                    button.doClick();
            }
        });
        Image hideImg=new ImageIcon("images/messages-hide.png").getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        JButton hideButton=new JButton(new ImageIcon(hideImg));
        hideButton.setBorder(new EmptyBorder(0,5,0,5));
        inputPanel.add(hideButton,BorderLayout.WEST);
        chatPanel.add(inputPanel,BorderLayout.SOUTH);
        Image showImg=new ImageIcon("images/messages-show.png").getImage().getScaledInstance(20,22,Image.SCALE_SMOOTH);
        JButton showButton=new JButton(new ImageIcon(showImg));
        showButton.setBounds(0,420,30,30);
        showButton.setVisible(false);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                showButton.setLocation(0,frame.getHeight()-70);
            }
        });
        hideButton.addActionListener(e -> {
            chatPanel.setVisible(false);
            showButton.setVisible(true);
        });
        showButton.addActionListener(e -> {
            showButton.setVisible(false);
            chatPanel.setVisible(true);
        });
        frame.getContentPane().add(chatPanel,BorderLayout.WEST);
        frame.getLayeredPane().add(showButton,new Integer(0));
    }

    public static void addMessage(String name,String message){
        String text="";
        if (name.equals("Server")){
           text= String.format("💬 %s ❗\n\n",message);
        }else {
            if (name.equals(client.getId()))
                name = "You";
            else
                name = "client " + name;
            text = String.format("%s: %s\n\n", name, message);
        }
        chatBox.append(text);
    }

    private static void showClientMenu(JFrame frame) {
        JDialog clientMenu=new JDialog();
        clientMenu.setTitle("Menu - client "+client.getId());
        clientMenu.setLayout(null);
        JLabel roomLabel=new JLabel("<html>  Room name:<br><h2>"+ client.getRoomKey()+"</h2></html>");
        roomLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        roomLabel.setBounds(80,20, 200,50);
        JButton requestButton=new JButton("Request Control");
        requestButton.setBounds(20,90, 200,50);
        requestButton.addActionListener(click->{
            client.request();
            clientMenu.dispose();
        });
        if(textBox.isEditable()){
            requestButton.setText("you are controller");
            requestButton.setEnabled(false);
        }
        JButton leftButton=new JButton("Disconnect from server");
        leftButton.setBounds(20,160, 200,50);
        leftButton.addActionListener(click ->{
            client.close();
            client=null;
            clientListener=null;
            textBox.setEditable(true);
            jPanel.getComponent(7).setVisible(true);
            jPanel.getComponent(8).setVisible(false);
            chatBox.setText("");
            chatBox.getParent().getParent().getParent().setVisible(false); //hiding chat box
            frame.getLayeredPane().getComponentsInLayer(0)[0].setVisible(false);//hiding messages button
            clientMenu.dispose();
        });
        clientMenu.add(roomLabel);
        clientMenu.add(requestButton);
        clientMenu.add(leftButton);
        clientMenu.setSize(250,350);
        clientMenu.setLocationRelativeTo(textBox.getParent());
        clientMenu.setVisible(true);
    }

    private static void showGitSettings() {
        JDialog settings=new JDialog();
        settings.setTitle("Git Settings");
        JLabel l1=new JLabel("Username:");
        l1.setBounds(20,20, 80,30);
        JTextField usernameField = new JTextField();
        usernameField.setText(git.getUsername());
        usernameField.setBounds(100,20, 100,30);
        JLabel l2=new JLabel("Password:");
        l2.setBounds(20,75, 80,30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setText(git.getPassword());
        passwordField.setBounds(100,75,100,30);
        JLabel l3=new JLabel("URL:");
        l3.setBounds(20,125, 80,30);
        JTextField urlField = new JTextField();
        if (git.find(currentPath))
            urlField.setText(git.getRemoteUrl());
        urlField.setBounds(100,125, 120,30);
        JButton submit = new JButton("Submit");
        submit.setBounds(100,200, 80,30);
        settings.add(l1); settings.add(usernameField);
        settings.add(l2); settings.add(passwordField);
        settings.add(l3); settings.add(urlField);
        settings.add(submit);
        submit.addActionListener(e -> {
            String userName = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String url = urlField.getText();
            if(!userName.equals("") && !password.equals("") && !url.equals("")) {
                git.setUser(userName, password);
                git.connect(url, currentPath);
                settings.dispose();
                checkGit();
            }else
                JOptionPane.showMessageDialog(settings,"please complete all required fields","Error!",JOptionPane.WARNING_MESSAGE);
        });
        settings.setSize(300,300);
        settings.setLocationRelativeTo(jPanel.getParent());
        settings.setLayout(null);
        settings.setVisible(true);
    }

    private static void checkGit(){
        boolean foundGit= git.find(currentPath);
        jPanel.getComponent(4).setVisible(!foundGit);
        jPanel.getComponent(5).setVisible(foundGit);
    }

    private static void gitClone() {
        JTextField repositoryUrl = new JTextField(10);
        JPanel inputDialog = new JPanel();
        inputDialog.setLayout(new BoxLayout(inputDialog,BoxLayout.Y_AXIS));
        inputDialog.add(repositoryUrl);
        JCheckBox checkBox=new JCheckBox("private");
        checkBox.setBorder(null);
        inputDialog.add(checkBox);
        JPanel loginInfo=new JPanel();
        loginInfo.add(new JLabel("Username:"));
        JTextField userName = new JTextField(5);
        userName.setEnabled(false);
        loginInfo.add(userName);
        loginInfo.add(new JLabel("password:"));
        JPasswordField password=new JPasswordField(5);
        password.setEnabled(false);
        loginInfo.add(password);
        inputDialog.add(loginInfo,BorderLayout.SOUTH);
        checkBox.addActionListener(e -> {
            userName.setEnabled(checkBox.isSelected());
            password.setEnabled(checkBox.isSelected());
        });

        int status= JOptionPane.showConfirmDialog(jPanel.getParent(),inputDialog,"Enter repository Url:",JOptionPane.OK_CANCEL_OPTION);
        String url=repositoryUrl.getText();
        if (status==JOptionPane.YES_OPTION && !url.equals("")) {
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setDialogTitle("select an empty directory");
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            status = folderChooser.showDialog(null, "Select");
            File selectedFolder = folderChooser.getSelectedFile();
            while (status == JFileChooser.APPROVE_OPTION  && selectedFolder.listFiles()!=null && selectedFolder.listFiles().length!=0) {
                status = folderChooser.showDialog(null, "Select");
            }
            if(status==JFileChooser.APPROVE_OPTION){
                try {
                    if (checkBox.isSelected())
                        git.setUser(userName.getText(), String.valueOf(password.getPassword()));
                    git.clone(url,selectedFolder.getAbsolutePath());
                    currentPath=selectedFolder.getAbsolutePath();
                    JOptionPane.showMessageDialog(null,"Successful!");
                } catch (GitAPIException e) {
                    JOptionPane.showMessageDialog(null,e.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static void save(JTextPane textBox) {
        JFileChooser fileChooser=new JFileChooser(currentPath);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".x file","x"));

        if (fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
            String fileName=fileChooser.getSelectedFile().getAbsolutePath();
            if (!fileName.endsWith(".x"))
                fileName=fileChooser.getSelectedFile() + ".x";
            try (FileWriter output = new FileWriter(fileName)) {
                output.write(textBox.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            currentPath=fileChooser.getSelectedFile().getParent();
            jPanel.getComponent(4).setEnabled(true);
        }
    }

    private static void Open(JTextPane textBox) {
        JFileChooser fileChooser=new JFileChooser(currentPath);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".x files","x"));

        if (fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
            File selectedFile=fileChooser.getSelectedFile();
            StringWriter stringWriter = new StringWriter();
            try (
                    BufferedReader input = new BufferedReader(new FileReader(selectedFile));
                    PrintWriter writer= new PrintWriter(new BufferedWriter(stringWriter));
            ) {
                while (true){
                    String line= input.readLine();
                    if (line==null)
                        break;
                    else{
                        writer.println(line);
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            textBox.setText(stringWriter.toString());
            currentPath=selectedFile.getParent();
            jPanel.getComponent(4).setEnabled(true);
            checkGit();
        }
    }
    private static void updateLineNumbers(){
        int linesCount=lineNumbersPane.getText().length() -lineNumbersPane.getText().replaceAll("\n", "").length();
        int textBoxLines=textBox.getText().length() -textBox.getText().replaceAll("\n", "").length()+1;
        StringBuilder rows=new StringBuilder(lineNumbersPane.getText());
        if (textBoxLines > linesCount) {
            for (int i = linesCount + 1; i <= textBoxLines; i++) {
                rows.append(i).append("\n");
            }
        }else if(textBoxLines < linesCount)
            rows.replace(lineNumbersPane.getText().indexOf(String.valueOf(textBoxLines+1)),lineNumbersPane.getText().length(),"");
        lineNumbersPane.setText(rows.toString());
    }

    private static void createStyles(){
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Style blackStyle = styleContext.addStyle("default", null);
        blackStyle.addAttribute(StyleConstants.Foreground, Color.BLACK);
        blackStyle.addAttribute(StyleConstants.FontSize,15);
        blackStyle.addAttribute(StyleConstants.FontFamily,"Dialog");
        blackStyle.addAttribute(StyleConstants.Bold, new Boolean(false));


        Style boldStyle = styleContext.addStyle("bold", null);
        boldStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style errorStyle = styleContext.addStyle("error", null);
        errorStyle.addAttribute(StyleConstants.Background, Color.red);
        styleContext.addStyle("noError", null).addAttribute(StyleConstants.Background, Color.white);;

        Style forstyle = styleContext.addStyle("for", null);
        forstyle.addAttribute(StyleConstants.Italic, new Boolean(true));
        forstyle.addAttribute(StyleConstants.FirstLineIndent,new Float(7));

        Style commentStyle = styleContext.addStyle("//", null);
        commentStyle.addAttribute(StyleConstants.Foreground, Color.GRAY);
        commentStyle.addAttribute(StyleConstants.Bold, new Boolean(false));

        Style intStyle = styleContext.addStyle("int", null);
        intStyle.addAttribute(StyleConstants.Foreground, Color.blue);
        intStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style floatStyle = styleContext.addStyle("float", null);
        floatStyle.addAttribute(StyleConstants.Foreground, new Color(1, 7, 164));
        floatStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style forStyle = styleContext.addStyle("for", null);
        forStyle.addAttribute(StyleConstants.Foreground, new Color(6, 91, 1));
        forStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style ifStyle = styleContext.addStyle("if", null);
        ifStyle.addAttribute(StyleConstants.Foreground, new Color(9, 131, 99));
        ifStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style operatorStyle = styleContext.addStyle("operator", null);
        operatorStyle.addAttribute(StyleConstants.Foreground, new Color(119, 0, 0));
        operatorStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style printStyle = styleContext.addStyle("print", null);
        printStyle.addAttribute(StyleConstants.Foreground, new Color(177, 133, 2));
        printStyle.addAttribute(StyleConstants.Bold, new Boolean(true));

        Style separatorStyle = styleContext.addStyle("%%", null);
        separatorStyle.addAttribute(StyleConstants.Foreground, new Color(0, 176, 255));
        separatorStyle.addAttribute(StyleConstants.Bold, new Boolean(true));
    }


    private static void updateTextStyles() throws BadLocationException {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        StyledDocument styledDocument = textBox.getStyledDocument();

        // Clear existing styles
        styledDocument.setCharacterAttributes(0, textBox.getText().length(), styleContext.getStyle("default"), false);
        styledDocument.setParagraphAttributes(0, textBox.getText().length(), styleContext.getStyle("default"), true);

        Pattern pattern=Pattern.compile("\\bint\\b|\\bfloat\\b|\\bfor\\b|end for|\\bif\\b|end if|\\bprint\\b|[/][/]|[=+-/]|[*]|%%");


        // Look for tokens and highlight them
        Matcher matcher = pattern.matcher(textBox.getText().replaceAll("\n",""));
        while (matcher.find()) {
            String style;
            if(matcher.group().matches("[*=+-/]"))
                style= "operator";
            else if(matcher.group().matches("end for"))
                style= "for";
            else if(matcher.group().matches("end if"))
                style= "if";
            else
                style=matcher.group();
            // Change the color of recognized tokens
            styledDocument.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), styleContext.getStyle(style), false);
        }

        indent();
        styleComments();
    }


    private static void indent(){
        int offset=textBox.getText().indexOf("%%");
        if (offset!=-1)
            offset+=2;
        else
            offset=0;
        String text=textBox.getText().substring(offset);
        Scanner scn=new Scanner(text);
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Style style = styleContext.addStyle("indent",null);
        int space=0;
        int lineNumber=0;
        while (scn.hasNext()){
            String line= scn.nextLine().trim();
            if(line.matches("end[ ]+for|end[ ]+if"))
                space-=12;
            style.addAttribute(StyleConstants.FirstLineIndent, new Float(space));
            textBox.getStyledDocument().setParagraphAttributes(offset+lineNumber,line.length(),style, true);
            if (line.matches("for .+|if .+"))
                space+=12;
            lineNumber++;
            offset+=line.length();
        }
    }
    private static void styleComments() throws BadLocationException {
        int start=textBox.getText().indexOf("//");
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        while (start!=-1) {
            int end = textBox.getText().indexOf("\n", start);
            if (end==-1) end=textBox.getText().length();
            int lineNumber= getLineNumber(start);
            String line =textBox.getText().substring(start,end);
            textBox.getStyledDocument().setCharacterAttributes(start-lineNumber ,line.length(), styleContext.getStyle("//"), false);
            start=textBox.getText().indexOf("//",end);
        }
    }

    private static int getLineNumber(int offset) throws BadLocationException {
        int n=0;
        int index=0;
        while (index!=-1){
            index=textBox.getText().substring(0,offset).indexOf("\n",index+1);
            n++;
        }
        return n;
    }

    private static int getOffsetByLine(int lineNumber) {
        int index=0;
        for (int i = 1; i <lineNumber ; i++) {
            index= textBox.getText().indexOf("\n",index+1);
        }
        return index;
    }

    private static void showError(int lineNumber,String error){
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        StyledDocument styledDocument = textBox.getStyledDocument();
        int start= getOffsetByLine(lineNumber)+1;
        int end=textBox.getText().indexOf("\n",start);
        String message=String.format("an error at line %d:\n%s",lineNumber,error);
        JOptionPane.showMessageDialog(null,message,"build failed",JOptionPane.ERROR_MESSAGE);
        styledDocument.setCharacterAttributes(start-lineNumber,end-start,styleContext.getStyle("error"),false);
        errorLine=lineNumber;
        errorMessage=error;
    }

    private static void clearError(){
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        StyledDocument styledDocument = textBox.getStyledDocument();
        int start= getOffsetByLine(errorLine)+1;
        int end=textBox.getText().indexOf("\n",start);
        styledDocument.setCharacterAttributes(start-errorLine,end-start,styleContext.getStyle("noError"),false);
        errorLine=-1;
        errorMessage="";
    }

    private static String[] getSuggestWords(int offset) throws BadLocationException {
        int splitter= textBox.getText().indexOf("%%");
        ArrayList<String> words=new ArrayList<>();
        String text=textBox.getText(offset,textBox.getCaretPosition()-offset);
        if(!text.contains(" ")) {
            if (offset < splitter || splitter == -1)
                Collections.addAll(words, "int", "float", "%%");
            else
                Collections.addAll(words, "print", "for", "end for","if","end if");
        }else
            text=text.substring(text.lastIndexOf(" ")+1);
        words.addAll(getVariableNames());
        ArrayList<String> list = new ArrayList<>();
        for (String word : words) {
            if (word.startsWith(text))
                list.add(word);
        }
        return list.toArray(new String[0]);
    }

    private static ArrayList<String> getVariableNames(){
        int index=textBox.getText().indexOf("%%");
        ArrayList<String> list=new ArrayList<>();
        if(index>0){
            Pattern pattern= Pattern.compile("(int|float) (\\w+)");
            Matcher matcher= pattern.matcher(textBox.getText().substring(0,index));
            while (matcher.find())
                list.add(matcher.group(2));
        }
        return list;
    }

    private static void showPopup() throws BadLocationException {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        StyledDocument styledDocument = textBox.getStyledDocument();

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(null);
        int caretPosition=textBox.getCaretPosition();
        String text=textBox.getText(0,caretPosition);
        int offset=text.lastIndexOf("\n")+1;
        String[] words=getSuggestWords(offset);

        for (String word: words) {
            JMenuItem menuItem=new JMenuItem(word);
            if(word.matches("print|for|end for|int|float|%%|if|end if"))
                menuItem.setIcon(UIManager.getIcon("Menu.arrowIcon"));
            else
                menuItem.setIcon(UIManager.getIcon("Tree.leafIcon"));
            popupMenu.add(menuItem);
            menuItem.addMenuKeyListener(new MenuKeyListener() {
                @Override
                public void menuKeyTyped(MenuKeyEvent e) {

                }

                @Override
                public void menuKeyPressed(MenuKeyEvent e) {

                }

                @Override
                public void menuKeyReleased(MenuKeyEvent e) {
                    if(e.getKeyChar()==KeyEvent.VK_ENTER){
                        menuItem.requestFocus();
                        menuItem.doClick();
                    }

                }
            });
            menuItem.addActionListener(e -> {
                try {
                    int index=Math.max(text.lastIndexOf("\n"),text.lastIndexOf(" "))+1;
                    styledDocument.remove(index,caretPosition-index);
                    styledDocument.insertString(index,word+ " ", styleContext.getEmptySet());
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
                popupMenu.setVisible(false);
            });
        }


        textBox.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN ) {
                    popupMenu.requestFocus();
                }
            }
        });

        Rectangle position=textBox.modelToView(textBox.getCaretPosition());

        popupMenu.show(textBox,position.x,position.y+12);

        SwingUtilities.invokeLater(new java.lang.Runnable() {
            @Override
            public void run() {
                textBox.requestFocusInWindow();
            }
        });

    }

    public static boolean requestControl(String id){
        int confirm= JOptionPane.showConfirmDialog(textBox,"Client "+ id+" request control. do you accept it?","Request Control",JOptionPane.YES_NO_OPTION);
        return confirm== JOptionPane.YES_OPTION;
    }

    public static void changeTeamRole(){
        Image controller=new ImageIcon("images/team-controller.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);
        Image member=new ImageIcon("images/team-enable.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH);

        if(textBox.isEditable()) {
            textBox.setEditable(false);
            ((JButton)jPanel.getComponent(8)).setIcon(new ImageIcon(member));
            if (clientListener!=null)
                textBox.getDocument().removeDocumentListener(clientListener);
        }else{
            textBox.setEditable(true);
            ((JButton)jPanel.getComponent(8)).setIcon(new ImageIcon(controller));
            if (clientListener!=null)
                JOptionPane.showMessageDialog(textBox,"You are now the controller");
            clientListener=new ClientListener(client);
            textBox.getDocument().addDocumentListener(clientListener);
        }
    }
}

class SubButton extends JButton{
    public SubButton(Image image,String toolTipText){
        ImageIcon icon=new ImageIcon(image);
        Border buttonBorder=BorderFactory.createEmptyBorder(4,6,4,6);
        setIcon(icon);
        setBorder(buttonBorder);
        setToolTipText(toolTipText);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }
}

class ClientListener implements DocumentListener{
    private Client client;

    public ClientListener(Client client){
        this.client=client;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            client.insert(e);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            client.delete(e);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}


