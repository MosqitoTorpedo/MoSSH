package tk.mosqitotorpedo.mosnewsupdater;

import com.jcraft.jsch.JSchException;

import javax.swing.*;

public class ConnectWindow {
    private JPanel contentConnect;
    private JTextField textField1;
    private JTextField textField2;
    private JButton submitButton;
    private JButton cancelButton;
    private JPasswordField passwordField1;
    private JTextField textField3;

    public static String username;
    public static String host;
    public static String password;
    public static int port;
    private final JFrame frame = new JFrame("Connect");


    public ConnectWindow() {

        frame.setContentPane(contentConnect);
        frame.setResizable(false);
        frame.setIconImage(MainGUI.icon.getImage());

        submitButton.addActionListener(e -> submitInformation());
        cancelButton.addActionListener(e -> frame.dispose());

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void submitInformation() {

        username = textField1.getText();
        host = textField2.getText();
        char[] passwordChars = passwordField1.getPassword();
        password = new String(passwordChars);
        port = Integer.parseInt(textField3.getText());
        System.out.println("disconnecting");
        try {
            SSHUtils.connectToSSH(host, username, port, password);
            ShellUtils.modifyScript();
            String scriptContent = ShellUtils.readScriptFile();
            System.out.println("Script: " + scriptContent);
            JOptionPane.showMessageDialog(frame,
                    "Connection Successful!",
                    "SSH Connection",
                    JOptionPane.PLAIN_MESSAGE);
            frame.dispose();
        } catch(JSchException e) {
            e.printStackTrace();
            System.out.println("Connection failed: " + e.getMessage());
            JOptionPane.showMessageDialog(frame,
                    "Connection Failed: " + e.getMessage(),
                    "SSH Connection",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
