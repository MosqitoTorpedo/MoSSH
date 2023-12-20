package tk.mosqitotorpedo.mosnewsupdater;

import javax.swing.*;
import java.awt.*;

public class HelpScreen {
    private JPanel ContentHelp;
    private JButton closeHelpScreenButton;

    private final JFrame frame = new JFrame("MoSSH Help Screen");


    public HelpScreen() {

        frame.setPreferredSize(new Dimension(600, 250));
        frame.setIconImage(MainGUI.icon.getImage());
        frame.setContentPane(ContentHelp);
        frame.setResizable(false);

        closeHelpScreenButton.addActionListener(e -> frame.dispose());
        closeHelpScreenButton.addActionListener(e -> System.out.println("Closed help screen"));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }

}
