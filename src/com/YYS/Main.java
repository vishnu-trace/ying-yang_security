package com.YYS;

import GUI.UIForm;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
	// write your code here

        UIManager.setInstalledLookAndFeels(UIManager.getInstalledLookAndFeels());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIForm form = new UIForm();
                form.setVisible(true);
            }
        });
       /* Scanner in  =new Scanner(System.in);
        int x = in.nextInt();
        if(x == 1) {
            CoreSplitterSystem CSS = new CoreSplitterSystem("testData_1.txt");
            YYSecContainer secContainer1 = new YYSecContainer("testContainer", "testData.txt", 2, true, 1, null);
            System.out.println(secContainer1.getKey());
            YYSecContainer secContainer2 = new YYSecContainer("testContainer", "testData.txt", 2, false, 2,secContainer1.getKey());
            int alternator = 0;
            while (!CSS.isEmpty()) {
                boolean bit = CSS.readBit();
                if (alternator == 0) {
                    secContainer1.writeBit(bit);
                    alternator = 1;
                } else {
                    secContainer2.writeBit(bit);
                    alternator = 0;
                }
            }
            try {
                secContainer1.fileFlush();
                secContainer2.fileFlush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            secContainer1.clearBuildData();
            secContainer2.clearBuildData();
            secContainer1.saveContainer();
            secContainer2.saveContainer();
            System.out.println("Finished Creation!");
        }else{
            long time = System.nanoTime();
            CoreMergerSystem CMS = new CoreMergerSystem("testContainer");
            System.out.println("Elapsed Time: "+ (System.nanoTime()-time));
        }*/
    }
}
