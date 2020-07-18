package com.YYS;

import GUI.UIForm;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	// write your code here

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
            }
        });
    }
}
