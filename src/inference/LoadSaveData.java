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

import weka.core.Instances;
import weka.core.converters.ArffSaver;

//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileReader;

import weka.core.converters.ConverterUtils.DataSource;

public class LoadSaveData{
	public static void main(String args[]) throws Exception{
		DataSource source = new DataSource("c://Users/Irrevaldy/Desktop/training.arff");
		Instances dataset = source.getDataSet();
		//Instances dataset = new Instances(new BufferedReader(new FileReader("c://Users/Irrevaldy/Desktop/training.arff")));		
		
		System.out.println(dataset.toSummaryString());
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataset);
		saver.setFile(new File("data/training.arff"));
		saver.writeBatch();
	}
}
