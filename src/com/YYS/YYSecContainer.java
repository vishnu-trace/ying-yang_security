package com.YYS;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemAlreadyExistsException;
import java.util.NoSuchElementException;
public class YYSecContainer {
    int numberOfFiles;                  //tells how many files split is performed in
    short seqNumber;                    //Sequence number of current file
    private boolean major;              //Indicates whether a container is major or not
    private File fileObject;            //Contains partial BIN data
    private int trailingBuffer;         //Indicates how many final bits are just buffer bits RANGE(0 -7)
    /*
    * Following data is used at container build time only
    */
    private int buffer;                  // one character buffer
    private int numberOfBits;            // number of bits left to be filled in buffer
    private BufferedOutputStream out;    // the output stream

    public YYSecContainer(String fileName, int nof, boolean wMajor, short seq_number){

        numberOfFiles = nof;
        seqNumber = seq_number;
        major = wMajor;
        /* Adding file extension to create the new file along with filename
        * Format: path/filename_seqNumber.yysec
        */
        String fileExtension ="_" + String.valueOf(seq_number) + ".yysec";
        fileName.concat(fileExtension);
        fileObject = new File(fileName);
        try {
            if (!fileObject.createNewFile())
                    throw new FileAlreadyExistsException(fileName + "already exists.");
            FileOutputStream fileOut = new FileOutputStream(fileObject);
            out  = new BufferedOutputStream(fileOut);
        }catch (FileAlreadyExistsException f){
            f.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearBuffer() {
        if (numberOfBits == 0) return;
        if (numberOfBits > 0) buffer <<= (8 - numberOfBits);
        try { out.write(buffer); }
        catch (IOException e) { e.printStackTrace(); }
        numberOfBits = 0;
        buffer = 0;
    }
    private void writeBit(boolean bit) {
        // add bit to buffer
        buffer <<= 1;
        if (bit) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        numberOfBits++;
        if (numberOfBits == 8) clearBuffer();
    }

}
