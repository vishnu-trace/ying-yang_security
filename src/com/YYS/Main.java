
/*MIT License
*Copyright (c) 2020 Vishnu Udaikumar
*Permission is hereby granted, free of charge, to any person obtaining a copy
*of this software and associated documentation files (the "Software"), to deal
*in the Software without restriction, including without limitation the rights
*to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*copies of the Software, and to permit persons to whom the Software is
*furnished to do so, subject to the following conditions:
*
*The above copyright notice and this permission notice shall be included in all
*copies or substantial portions of the Software.
*
*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*SOFTWARE.*/
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
