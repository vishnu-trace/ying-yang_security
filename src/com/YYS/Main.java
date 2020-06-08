package com.YYS;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner in  =new Scanner(System.in);
        int x = in.nextInt();
        CoreSplitterSystem CSS = new CoreSplitterSystem("testData.png");
        YYSecContainer secContainer1 = new YYSecContainer("testContainer", "testData.png",2,true,1);
        YYSecContainer secContainer2 = new YYSecContainer("testContainer", "testData.png",2,false,2);
        int alternator=0;
        while(!CSS.isEmpty()){
            boolean bit = CSS.readBit();
            if(alternator == 0) {
                secContainer1.writeBit(bit);
                alternator=1;
            }
            else{
                secContainer2.writeBit(bit);
                alternator=0;
            }
        }
        secContainer1.clearBuildData();
        secContainer2.clearBuildData();
        secContainer1.saveContainer();
        secContainer2.saveContainer();
        System.out.println("Finished Creation!");
    }
}
