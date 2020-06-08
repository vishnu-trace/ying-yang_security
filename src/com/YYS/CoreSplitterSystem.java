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
                System.out.println("File Exists");
                FileInputStream sourceStream = new FileInputStream(source);
                //Initializing Buffer Input Stream.
                BuffInputStream = new BufferedInputStream(sourceStream);
                buffer=0;
                numberOfBits=0;
                fillBuffer();
            }
            else System.out.println("No File Exists");
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
