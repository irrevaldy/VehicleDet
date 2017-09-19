/*
 *  How to use WEKA API in Java 
 *  Copyright (C) 2014 
 *  @author Dr Noureddin M. Sadawi (noureddin.sadawi@gmail.com)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it as you wish ... 
 *  I ask you only, as a professional courtesy, to cite my name, web page 
 *  and my YouTube Channel!
 *  
 */
package deteksikendaraan.inference;
//import required classes
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.io.File;



public class CSV2Arff 
{  
  public static void main(String[] args) throws Exception 
  {
	 
    // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("D:/Kuliah S2/Semester 4/Tesis Extend/Implementasi Tesis/DeteksiKendaraan/data/masterfrommysql.csv"));
    Instances data = loader.getDataSet();//get instances object

    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);//set the dataset we want to convert
    //and save as ARFF
    saver.setFile(new File("D:/Kuliah S2/Semester 4/Tesis Extend/Implementasi Tesis/DeteksiKendaraan/data/full.arff"));
    saver.writeBatch();
    
    System.out.println("Data has been converted to arff file");
  }
} 