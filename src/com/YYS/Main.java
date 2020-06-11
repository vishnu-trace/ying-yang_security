package com.YYS;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner in  =new Scanner(System.in);
        int x = in.nextInt();
        if(x == 1) {
            CoreSplitterSystem CSS = new CoreSplitterSystem("testData.mp4");
            YYSecContainer secContainer1 = new YYSecContainer("testContainer", "testData.mp4", 2, true, 1);
            YYSecContainer secContainer2 = new YYSecContainer("testContainer", "testData.mp4", 2, false, 2);
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
            CoreMergerSystem CMS = new CoreMergerSystem("testContainer");
        }
    }
}
