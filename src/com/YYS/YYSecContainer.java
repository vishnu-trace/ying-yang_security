package com.YYS;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.zip.*;

public class YYSecContainer implements Serializable {


    private String originalFileName;            //Stores the original File Name
    private String fileName;                    //Stores the current filename
    private int numberOfFiles;                  //tells how many files split is performed in
    private int seqNumber;                     //Sequence number of current file
    private boolean major;                     //Indicates whether a container is major or not
    private transient File fileObject;         //Contains partial BIN data
    private String key;                          //for major file
    private String lock;                   //for non-major file
    private int trailingBuffer;                //Indicates how many final bits are just buffer bits RANGE(0-7)
    /*
    * Following data is used at container build time only
    */
    private transient int buffer;                  // one character buffer
    private transient int numberOfBits;            // number of bits to be filled in buffer
    private transient BufferedOutputStream out;    // the output stream

    public YYSecContainer(String fileName,String orgFl, int nof, boolean wMajor, int seq_number, String key){
        this.key = key;
        this.fileName = fileName;
        this.originalFileName = orgFl;
        numberOfFiles = nof;
        seqNumber = seq_number;
        major = wMajor;
        numberOfBits=0;
        buffer=0;
        /* Adding file extension to create the new file along with filename
        * Format: path/filename_seqNumber.yysec
        */
        String fileExtension ="_" + String.valueOf(seq_number) + ".yysec";
        fileName += fileExtension;
        fileObject = new File(fileName);
        try {
            boolean cont = fileObject.createNewFile();
            if (!cont)
                throw new FileAlreadyExistsException(fileName + "already exists.");
            FileOutputStream fileOut = new FileOutputStream(fileObject);
            out  = new BufferedOutputStream(fileOut);
        }catch (FileAlreadyExistsException f){
            f.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if(!major)  setLock();
        else    setKey();
    }


    public YYSecContainer(String fileName){
        numberOfBits=0;
        buffer=0;
        fileObject = new File(fileName);
        try {
            boolean cont = fileObject.createNewFile();
            if (!cont)
                throw new FileAlreadyExistsException(fileName + " already exists.");
            FileOutputStream fileOut = new FileOutputStream(fileObject);
            out  = new BufferedOutputStream(fileOut);
        }catch (FileAlreadyExistsException f){
            f.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void fileFlush() throws IOException {
        clearBuffer();
        out.flush();
    }

    private void setLock(){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte data[] = md.digest(key.getBytes("UTF-8"));
            lock = new String(data, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setKey(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        key= originalFileName+ ".."+dtf.format(now);
    }



    public  void  saveContainer(){
        //To save current object to file
        String objectName = fileName+"_obj"+"_"+String.valueOf(seqNumber)+".dat";
        try{
                FileOutputStream fs = new FileOutputStream(objectName);
                ObjectOutputStream objectStream = new ObjectOutputStream(fs);
                //Write current object to file
                objectStream.writeObject(this);
                objectStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Creating a zip package to keep container and its respected object file together
        try{
            File containerObjectFile = new File(objectName);
            String zipFileName = fileName.concat("_" + String.valueOf(seqNumber)+".zip");
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry(containerObjectFile.getName()));
            byte[] bytes = Files.readAllBytes(Paths.get(containerObjectFile.getName()));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.putNextEntry(new ZipEntry(fileObject.getName()));
            byte[] bytes2 = Files.readAllBytes(Paths.get(fileObject.getName()));
            zos.write(bytes2, 0, bytes2.length);
            zos.closeEntry();
            zos.close();
            containerObjectFile.delete();
            out.close();
            fileObject.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //pushes the buffer to the outputStream if not full 8 bits appends 0's at end till 8 bit long and then pushes
    private void clearBuffer() {
        if (numberOfBits == 0) return;
        if (numberOfBits > 0) buffer <<= (8 - numberOfBits);
        try {
            out.write(buffer);
        }
        catch (IOException e) { e.printStackTrace(); }
        numberOfBits = 0;
        buffer = 0;
    }
    public void writeBit(boolean bit) {
        // add bit to buffer
        buffer <<= 1;
        if (bit) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        numberOfBits++;
        if (numberOfBits == 8) clearBuffer();
    }

    //clears build time data after container is built
    public void clearBuildData(){
        if(numberOfBits !=8 || numberOfBits!=0)
            trailingBuffer = 8 - numberOfBits;
        clearBuffer();
        buffer = 0;
        numberOfBits = 0;
    }


    public boolean isMajor(){
        return major;
    }

    public int getNumberOfFiles(){
        return numberOfFiles;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    //Hashes the passed key and verifies with its own lock signature
    public int verifyLock(String mKey){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte data[] = md.digest(mKey.getBytes("UTF-8"));
            String lck = new String(data, StandardCharsets.UTF_8);
            if(lck.compareTo(lock) == 0) return 1;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // returns the key of the major file
    public String getKey() {
        return key;
    }

    public File getFileObject(){
        return fileObject;
    }
}
