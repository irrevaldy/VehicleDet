# VehicleDet

This repository contains java application to infer transportation mode and potential traffic congestion area. For more information about this project you can download the paper here:
https://ieeexplore.ieee.org/document/8285857

It use mySQL as the database.

To run the project you need to install mySQL JDBC connector or MySQL Connector/J you can find it here:
https://downloads.mysql.com/archives/c-j/

For mac:
1. Choose the "Platform Independent" for the operating system and then click download
2. You may find the zipped folder after you finish the download. Extract it and put it somewhere in your lib directory

To run the app via mac terminal: 
1. Compile the DeteksiKendaraan.java class with Point.java class library
   Example: javac DeteksiKendaraan.java Point.java
2. Run the java class with the MySQL connector you have downloaded before
    java -cp /Users/aido/Downloads/mysql-connector-j-8.1.0/mysql-connector-j-8.1.0.jar:.: DeteksiKendaraan





