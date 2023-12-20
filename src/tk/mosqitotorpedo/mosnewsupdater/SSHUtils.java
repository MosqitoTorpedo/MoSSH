package tk.mosqitotorpedo.mosnewsupdater;

import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class SSHUtils {

    public static Session session;
    public static JSch ssh = new JSch();
    public static void connectToSSH(String host, String username, int port, String password) throws JSchException {

            session = ssh.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();
            System.out.println("Connection to " + username + '@' + host + " on port " + port + " successful!");

    }

    public static void runCommand(String command) {
        runCommand(command, null);
    }
    public static void runCommand(String command, String sudoPassword) {
        try {
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();
            InputStream response = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write((sudoPassword + "\n").getBytes());
            outputStream.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            channel.disconnect();
        } catch(JSchException e) {
            e.printStackTrace();
            System.out.println("Failed to execute: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong: " + e.getMessage());
        }


    }

    public static void uploadFile(String filePath, String remoteFilePath) {
        try {
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.put(filePath, remoteFilePath);
            channel.disconnect();


        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("Failed to open SFTP channel: " + e.getMessage());
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println("Failed to upload file: " + e.getMessage());
        }
    }

    public static void uploadFileVP(String remoteFile,
                                    String localFile,
                                    String pathToKey,
                                    String keyPassphrase) throws SftpException, JSchException {



            ssh.addIdentity(pathToKey, keyPassphrase);
            System.out.println("Added identity successfully!");
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            channel.put(localFile, remoteFile);
            channel.disconnect();

    }


}
