package tk.mosqitotorpedo.mosnewsupdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ShellUtils {
    public static void modifyScript() {
        String username = ConnectWindow.username;
        String host = ConnectWindow.host;
        String password = ConnectWindow.password;

        String scriptContent = readScriptFile();

        scriptContent = scriptContent.replace("$destination_user", username);
        scriptContent = scriptContent.replace("$destination_host", host);
        scriptContent = scriptContent.replace("$password", password);


        saveScriptFile(scriptContent);
    }

    public static String readScriptFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("bin/add-to-auth_keys.sh"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static void saveScriptFile(String scriptContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bin/add-to-auth_keys.sh"))) {
            writer.write(scriptContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
