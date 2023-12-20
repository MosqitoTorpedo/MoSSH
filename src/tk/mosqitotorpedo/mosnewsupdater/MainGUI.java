package tk.mosqitotorpedo.mosnewsupdater;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


public class MainGUI {
    private JPanel contentRoot;
    private JButton connectButton;
    private JButton uploadFileButton;
    private JButton disconnectButton;
    private JButton createFileButton;
    private JButton helpButton;
    private JLabel welcomeLabel;
    private JButton uploadCreatedFileButton;
    private JButton cancelButton;
    private JTextArea fileText;
    public static final JFrame frame = new JFrame("MoSSH");


    public static String iconImagePath = "/MoSSHLogo.png";

    public static ImageIcon icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource(iconImagePath)));

    public static String filePath;
    public static String remoteFilePath;


    public MainGUI() {

        frame.setContentPane(contentRoot);
        frame.setPreferredSize(new Dimension(835, 600));
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(icon.getImage());

        cancelButton.hide();
        uploadCreatedFileButton.hide();
        fileText.hide();
        connectButton.addActionListener(e -> connectToSSH());
        disconnectButton.addActionListener(e -> disconnectFromSSH());
        uploadFileButton.addActionListener(e -> uploadFile());
        createFileButton.addActionListener(e -> createFile());
        uploadCreatedFileButton.addActionListener(e -> uploadCreatedFile());
        cancelButton.addActionListener(e -> resetMainWindow());
        helpButton.addActionListener(e -> helpScreen());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void connectToSSH() {
        new ConnectWindow();
    }

    public void disconnectFromSSH() {
        SSHUtils.session.disconnect();
        System.out.println("Disconnected from SSH");
        JOptionPane.showMessageDialog(frame,
                "Disconnected from SSH",
                "SSH",
                JOptionPane.PLAIN_MESSAGE);


    }


    public void uploadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select File To Upload");

        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String homeDir = System.getProperty("user.home");

            File file = chooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            System.out.println("This is the 'File file = chooser.getSelectedFile()': " + file + "\n This the 'String filePath = file.getAbsolutePath()': " + filePath);

            String remoteFilePath = JOptionPane.showInputDialog("Enter Remote filepath: ");
            String keyPath = homeDir + "/.mossh/key/id_rsa";

            try {
                SSHUtils.uploadFileVP(remoteFilePath, filePath, keyPath, "");
            } catch (SftpException e) {
                e.printStackTrace();
                System.out.println("Something went wrong with the upload: " + e.getMessage() + "\nHere's the cause: " + e.getCause());
                JOptionPane.showMessageDialog(frame, "Something went wrong: " + e.getMessage(), "File Upload", JOptionPane.ERROR_MESSAGE);
            } catch (JSchException e) {
                e.printStackTrace();
                System.out.println("Error adding identity: " + e.getMessage());
                JOptionPane.showMessageDialog(frame,
                        "There was an issue adding the ssh identity: " + e.getMessage(),
                        "SSH Identity",
                        JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("The upload was successful!");
            JOptionPane.showMessageDialog(frame,
                    "Upload successful!",
                    "File Upload",
                    JOptionPane.PLAIN_MESSAGE);


        }
    }

    public void createFile() {

        welcomeLabel.hide();
        uploadCreatedFileButton.show();
        cancelButton.show();
        fileText.show();

    }

    public void uploadCreatedFile() {
        String homeDir = System.getProperty("user.home");
        String fileData = fileText.getText() + "\n";
        remoteFilePath = JOptionPane.showInputDialog("Enter Remote filepath: ");
        String uploadFileName = JOptionPane.showInputDialog("Enter File Name (include desired extension): ");
        String keyPath = homeDir + "/.mossh/key/id_rsa";

        try {
            String tempFileLoc = homeDir + "/.mossh/temp/" + uploadFileName;
            FileWriter fileWriter = new FileWriter(tempFileLoc);
            fileWriter.write(fileData);
            fileWriter.close();
            filePath = tempFileLoc;

            try {
                SSHUtils.uploadFileVP(remoteFilePath, filePath, keyPath, "");
                System.out.println("The upload was successful!");
                JOptionPane.showMessageDialog(frame,
                        "Upload successful!",
                        "File Upload",
                        JOptionPane.PLAIN_MESSAGE);
                resetMainWindow();
                File deleteTempFile = new File(filePath);
                if (deleteTempFile.exists()) {
                    if (deleteTempFile.delete()) {
                        System.out.println("Deleted temp file successfully!");
                    } else {
                        System.out.println("Failed to delete the temp file");
                    }
                } else {
                    System.out.println("That file does not exist");
                }
            } catch (JSchException e) {
                e.printStackTrace();
                System.out.println("Error adding identity: " + e.getMessage());
                JOptionPane.showMessageDialog(frame,
                        "There was an issue adding the ssh identity: " + e.getMessage(),
                        "SSH Identity",
                        JOptionPane.ERROR_MESSAGE);
            }


        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with the upload: " + e.getMessage() + "\nHere's the cause: " + e.getCause());
            JOptionPane.showMessageDialog(frame, "Something went wrong: " + e.getMessage(), "File Upload", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File Failed To Save: " + e.getMessage());
            JOptionPane.showMessageDialog(frame,
                    "Something went wrong with saving the file: " + e.getMessage(),
                    "File Save",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void resetMainWindow() {
        welcomeLabel.show();
        uploadCreatedFileButton.hide();
        cancelButton.hide();
        fileText.setText("");
        fileText.hide();
    }

    public void helpScreen() {
        System.out.println("Opened help screen");
        new HelpScreen();
    }
}
