package tk.mosqitotorpedo.mosnewsupdater;
import com.formdev.flatlaf.FlatDarkLaf;
import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws UnsupportedLookAndFeelException {



        UIManager.setLookAndFeel(new FlatDarkLaf());
        MainGUI gui = new MainGUI();

    }

}
