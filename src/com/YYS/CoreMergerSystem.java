
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

/*
* The Merger System class is responsible for decryption of the yysec files. It requires an absolute path to the first(or major) file in sequence.
* The decryption process takes each zip file and unzips it to a new directory called YYSout in the zip file's parent directory.
* Each zip file contains its own object file also the .yysec file to be decrypted.
* The System takes the object file to verify that each of the parts do indeed belong to each other to make sure no mismatch has occurred.
* Thus lock on each non-major file is tested against the key of the major file for ensuring the authenticity and avoid a mismatch as mentioned above.
*
* The merger system uses the YYSec container system to build the original file by taking bit from each part sequentially in a circular fashion.
*
* */

import java.io.*;
import java.rmi.AccessException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CoreMergerSystem {
    YYSecContainer[] containers;                   //For storing container objects
    int noOfContainers;                            //Stores no of containers
    String workingDir;                             // stores path to working directory
    String fileName;                               // stores zip file name

    public CoreMergerSystem( String fileName){
        this.fileName = fileName.replaceAll("_1.zip", "");
        this.fileName.trim();
        fileName = this.fileName;
        int seqNumber = 1;
        String destDir = null;
        //Unpacking the zip files for further processing
        while(true) {
            System.out.println("Searching " + this.fileName);
            String fileNameWExt = fileName + "_" + String.valueOf(seqNumber)+".zip";
            File fileObject1 = new File(fileNameWExt);
            if(!fileObject1.exists()) break;
            destDir = workingDir = fileObject1.getParent();
            destDir += "\\YYSout";
            this.fileName = destDir + "\\" + this.fileName.substring(this.fileName.lastIndexOf("\\")+1);
            File dest = new File(destDir);
            if (!dest.exists()) dest.mkdir();
            FileInputStream fis;
            byte[] buffer = new byte[1024];
            try {
                fis = new FileInputStream(fileObject1);
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze = zis.getNextEntry();
                while (ze != null) {
                    String fileNameOBJ = ze.getName();
                    File newFile = new File(destDir + File.separator + fileNameOBJ);
                    System.out.println("Unzipping to " + newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
                //close last ZipEntry
                zis.closeEntry();
                zis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            seqNumber++;
        }
        checkObjects();
        try {
            verifyAuthenticity();
        } catch (AccessException e) {
            e.printStackTrace();
            System.out.println(e);
            System.exit(22);
        }
        merge();
        assert destDir != null;
        File dest = new File(destDir);
        for (File subFile : dest.listFiles())
                subFile.delete();
        dest.delete();
    }

    //Merging code to create the final file
    private void merge(){
        CoreSplitterSystem[] CSS = new CoreSplitterSystem[noOfContainers];
        for(int seqNumber =1; seqNumber<=noOfContainers; seqNumber++){
            String fileNameWExtN = this.fileName + "_" + String.valueOf(seqNumber)+".yysec";
            CSS[seqNumber-1] = new CoreSplitterSystem(fileNameWExtN);
        }
        int alternator = 0,flag =0;
        Boolean bit = null;
        YYSecContainer finalFile = new YYSecContainer(containers[0].getOriginalFileName());
        while(flag != noOfContainers){
            if(CSS[alternator].isEmpty()) flag++;
            else bit = CSS[alternator].readBit();
            finalFile.writeBit(bit);
            alternator = (alternator+1)%noOfContainers;
        }
        try { finalFile.fileFlush(); }
        catch (IOException e) { e.printStackTrace(); }
    }


    //Verifies the authenticity of the container to make sure all are for the same end file
    private void verifyAuthenticity() throws AccessException {
        String key = containers[0].getKey();
        for(int i=1;i<noOfContainers;i++)
            if(containers[i].verifyLock(key) != 1)
                throw new AccessException("Lock and Key Mismatch.");
    }

    //Verifying presence of all objects
    private void checkObjects(){
        //files placed in same directory with sub-directory name nullDocument
        int seqNumber =1;
        String fileNameWExt = this.fileName + "_obj_" + String.valueOf(seqNumber)+".dat";
        //loading first container from .dat object file
        try {
            FileInputStream fis = new FileInputStream(fileNameWExt);
            ObjectInputStream obj = new ObjectInputStream(fis);
            YYSecContainer tempContainer  = (YYSecContainer) obj.readObject();
            obj.close();
            noOfContainers = tempContainer.getNumberOfFiles();
        } catch (FileNotFoundException e) {
            System.out.println("Object File Not Found.");
            e.printStackTrace();
            System.exit(21);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(21);
        } catch (ClassNotFoundException e) {
            System.out.println("Error Reading Object File.");
            e.printStackTrace();
            System.exit(21);
        }
        //loading all containers from .dat object file
        containers =new YYSecContainer[noOfContainers];
        for(seqNumber =1; seqNumber <= noOfContainers; seqNumber++){
            String fileNameWExtN = this.fileName + "_obj_" + String.valueOf(seqNumber)+".dat";
            try{
                FileInputStream fis = new FileInputStream(fileNameWExtN);
                ObjectInputStream obj = new ObjectInputStream(fis);
                containers[seqNumber-1] = (YYSecContainer) obj.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("All Object Files Not Found.");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error Reading Object File.");
                e.printStackTrace();
            }
        }
    }
}
