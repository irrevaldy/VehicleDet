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

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;

public class Classification{
	public static void main(String args[]) throws Exception{
		//load dataset
		DataSource source = new DataSource("../../data/trainingset1.arff");
		Instances dataset = source.getDataSet();	
		//set class index to the last attribute
		dataset.setClassIndex(dataset.numAttributes()-1);
		//create and build the classifier!
		
		String[] options = new String[4];
		options[0] = "-C"; options[1] = "0.11";
		options[2] = "-M"; options[3] = "3";
		J48 tree = new J48();
		tree.setOptions(options);
		tree.buildClassifier(dataset);
		//save model
		weka.core.SerializationHelper.write("data/my_tree_model.model", tree);
		System.out.println(tree.getCapabilities().toString());
		System.out.println(tree.graph());
		
		Evaluation eval = new Evaluation(dataset);
		Random rand = new Random(1);
		int folds = 10;
		
		//Notice we build the classifier with the training dataset
        //we initialize evaluation with the training dataset and then
        //evaluate using the test dataset

		//test dataset for evaluation
		DataSource source1 = new DataSource("data/testset1.arff");
		Instances testDataset = source1.getDataSet();
		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);
		//now evaluate model
		//eval.evaluateModel(tree, testDataset);
		eval.crossValidateModel(tree, testDataset, folds, rand);
		System.out.println(eval.toSummaryString("Evaluation results:\n", false));
		
		System.out.println("Correct % = "+eval.pctCorrect());
		System.out.println("Incorrect % = "+eval.pctIncorrect());
		System.out.println("AUC = "+eval.areaUnderROC(1));
		System.out.println("kappa = "+eval.kappa());
		System.out.println("MAE = "+eval.meanAbsoluteError());
		System.out.println("RMSE = "+eval.rootMeanSquaredError());
		System.out.println("RAE = "+eval.relativeAbsoluteError());
		System.out.println("RRSE = "+eval.rootRelativeSquaredError());
		System.out.println("Precision = "+eval.precision(1));
		System.out.println("Recall = "+eval.recall(1));
		System.out.println("fMeasure = "+eval.fMeasure(1));
		System.out.println("Error Rate = "+eval.errorRate());
	    //the confusion matrix
		System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));
		
		
	}

}
