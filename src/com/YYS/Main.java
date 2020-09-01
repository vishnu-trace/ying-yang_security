package com.YYS;

import GUI.UIForm;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String appDetails = "This toolkit is designed to encrypt your data into multiple packages, which independently cannot be decrypted.\n" +
                "Any number of parts can be made as the user may choose. \n"+
                "Make sure to have a complete deletion of the file/Directory you wish to encrypt after encryption to ensure a third party doesn't get access to it.\n"+
                "(Note: In case you want to encrypt a directory you would have to first make a RAR/ZIP archive and then after Encrypt it.)\n\n" +
                "This toolkit is a BETA version and still under testing. In case of errors contact the author.\n\n"  ;
        UIManager.setInstalledLookAndFeels(UIManager.getInstalledLookAndFeels());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIForm form = null;
                try {
                    form = new UIForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                form.setVisible(true);
                form.Console.append(appDetails);
            }
        });
    }
}
