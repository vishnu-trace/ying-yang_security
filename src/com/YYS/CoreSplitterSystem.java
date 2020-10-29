
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
import java.io.*;
import java.util.NoSuchElementException;


public class CoreSplitterSystem {
    private static final int EOF = -1;   // end of file

    private BufferedInputStream BuffInputStream;      // the input stream
    private int buffer;                  // one character buffer
    private int numberOfBits;                       // number of bits left in buffer

    public CoreSplitterSystem(String fileName){
        try{
            //Trying to read a file given as filename String
            File source = new File(fileName);
            if(source.exists()){
                //If source file exists
                System.out.println(fileName + " :File Exists");
                FileInputStream sourceStream = new FileInputStream(source);
                //Initializing Buffer Input Stream.
                BuffInputStream = new BufferedInputStream(sourceStream);
                buffer=0;
                numberOfBits=0;
                fillBuffer();
            }
            else System.out.println(fileName + "File Not Found");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Fills the buffer one Byte at a time
    private void fillBuffer() {
        try {
            buffer = BuffInputStream.read();
            numberOfBits = 8;
        }
        catch (IOException e) {
            buffer = EOF;
            numberOfBits = -1;
        }
    }

    // Returns true if this binary input stream exists.
    public boolean BinDataExists(){
        return BuffInputStream != null;
    }

    // Returns true if the buffer empty.
    public boolean isEmpty() {
        return buffer == EOF;
    }

    // Returns one bit from the buffer
    public boolean readBit(){
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        numberOfBits--;
        boolean bit = ((buffer >> numberOfBits) & 1) == 1;
            if (numberOfBits == 0) fillBuffer();
        return bit;
    }


}
