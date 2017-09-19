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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import weka.classifiers.trees.J48;

public class ClassifyInstance{
	public static void main(String args[]) throws Exception
	{	
		Class.forName("com.mysql.jdbc.Driver");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/trajectorybdg?" + "user=root&password=");

        Statement statement = connect.createStatement();
		
		//load training dataset
		DataSource source = new DataSource("data/trainingset1.arff");
		Instances trainDataset = source.getDataSet();	
		
		//set class index to the last attribute
		trainDataset.setClassIndex(trainDataset.numAttributes()-1);
		//get number of classes
		int numClasses = trainDataset.numClasses();
		//print out class values in the training dataset
		for(int i = 0; i < numClasses; i++){
			//get class string value using the class index
			String classValue = trainDataset.classAttribute().value(i);
			System.out.println("Class Value "+i+" is " + classValue);
		}
		
		//load model
		J48 tree = (J48) weka.core.SerializationHelper.read("data/my_tree_model.model");
		
		//load new dataset
		DataSource source1 = new DataSource("data/full-unknown.arff");
		Instances testDataset = source1.getDataSet();	
		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);

		//loop through the new dataset and make predictions
		System.out.println("===================");
		System.out.println("Actual Class, Tree Predicted");
		for (int i = 0; i < testDataset.numInstances(); i++) 
		{
			//get class double value for current instance
			double actualValue = testDataset.instance(i).classValue();
			String actual = testDataset.classAttribute().value((int) actualValue);
			//get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			//call classifyInstance, which returns a double value for the class
			double predtree = tree.classifyInstance(newInst);
			String predString = testDataset.classAttribute().value((int) predtree);
			System.out.println(i + 1 + " "  + actual+", "+ predString);
			
			String sqli = "UPDATE gpsp SET transmode='" + predString + "' WHERE no =" + (i+1);
            statement.executeUpdate(sqli);
            System.out.println("Transportation mode predicted is " + predString); 
			
		}

	}
}
