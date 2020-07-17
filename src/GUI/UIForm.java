package GUI;

import com.YYS.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class UIForm extends  JFrame{
    private JPanel basePanel;
    private JTextField srcField;
    private JButton browseButtonSrc;
    private JLabel srcLabel;
    private JButton generateButton;
    private JTextArea Console;
    private JComboBox splitSelection;
    private JLabel splitLabel;
    private JRadioButton Encrypt;
    private JRadioButton Decrypt;
    private JPanel activityPanel;
    private JLabel destinationFileLabel;
    private JTextField destField;
    private JButton browseButtonDest;
    private String srcFile;
    private String destDirectory;

    public UIForm(){
        add(basePanel);
        setSize(800,500);
        setTitle("Ying-Yang Security");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Component parent = this;

        browseButtonSrc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(parent);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // user selects a file
                    File selectedFile = fileChooser.getSelectedFile();
                    srcFile = selectedFile.getAbsolutePath();
                    srcField.setText(srcFile);
                }
            }
        });
        Decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Decrypt.isSelected() == true){
                    splitLabel.setVisible(false);
                    splitSelection.setVisible(false);
                    if(Encrypt.isSelected())
                        Encrypt.doClick();
                    Decrypt.setSelected(true);
                }
            }
        });
        Encrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Encrypt.isSelected() == true){
                    splitLabel.setVisible(true);
                    splitSelection.setVisible(true);
                    if(Decrypt.isSelected())
                        Decrypt.doClick();
                    Encrypt.setSelected(true);
                }
            }
        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Encrypt.isSelected()) {
                    Console.append("Encryption Process Started...\n");
                    ConsoleOutputCapturer cos = new ConsoleOutputCapturer();
                    cos.start();
                    String nofs = splitSelection.getSelectedItem().toString();
                    int nof = Integer.parseInt(nofs);
                    int i = 2;
                    CoreSplitterSystem CSS = new CoreSplitterSystem(srcFile);
                    YYSecContainer secContainer1 = new YYSecContainer("testContainer", srcFile, nof, true, 1, null);
                    Console.append("Key: " +  secContainer1.getKey().toString() + "\n");
                    YYSecContainer secContainer[] = new YYSecContainer[nof - 1];
                    while (i <= nof) {
                        secContainer[i - 2] = new YYSecContainer("testContainer", srcFile, nof, false, i, secContainer1.getKey());
                        ++i;
                    }
                    int alternator = 0;
                    while (!CSS.isEmpty()) {
                        boolean bit = CSS.readBit();
                        if (alternator == 0) {
                            secContainer1.writeBit(bit);
                            alternator = (alternator + 1) % nof;
                        } else {
                            secContainer[alternator - 1].writeBit(bit);
                            alternator = (alternator + 1) % nof;
                        }
                    }
                    try {
                        secContainer1.fileFlush();
                        i = 2;
                        while (i <= nof) {
                            secContainer[i - 2].fileFlush();
                            i++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    secContainer1.clearBuildData();
                    secContainer1.saveContainer();
                    i = 2;
                    while (i <= nof) {
                        secContainer[i - 2].clearBuildData();
                        secContainer[i - 2].saveContainer();
                        ++i;
                    }
                    String output = cos.stop();
                    Console.append(output + "\n");
                    Console.append("Finished Encryption!\n");
                }
                else{
                    Console.append("Decryption Process Started...\n");
                    ConsoleOutputCapturer cos = new ConsoleOutputCapturer();
                    cos.start();
                    CoreMergerSystem CMS = new CoreMergerSystem(srcFile);
                    String output = cos.stop();
                    Console.append(output + "\n");
                    Console.append("Finished Encryption!\n\n");
                }
            }
        });
    }
}
