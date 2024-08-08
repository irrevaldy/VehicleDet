# VehicleDet

This repository contains java application to infer transportation mode and potential traffic congestion area. For more information about this project you can download the paper here:
https://ieeexplore.ieee.org/document/8285857

It uses mySQL as the database. Install mySQL first and after that you need to set up database connection.
Create database called "trajectorybdg" and inside of it create table called "gpsp".

To run the project you need to install mySQL JDBC connector or MySQL Connector/J you can find it here:
https://downloads.mysql.com/archives/c-j/

For mac:
1. Choose the "Platform Independent" for the operating system and then click download
2. You may find the zipped folder after you finish the download. Extract it and put it somewhere in your lib directory

To run the app via mac terminal: 
1. Compile the DeteksiKendaraan.java class with Point.java class library
   **javac DeteksiKendaraan.java Point.java**
2. Run the java class with the MySQL connector you have downloaded before
   **java -cp /Users/Downloads/mysql-connector-j-8.1.0/mysql-connector-j-8.1.0.jar:.: DeteksiKendaraan**

How to create the JAR file:
1. Make sure you have the mysqlconnector driver like shown in /lib folder.
2. Create /out folder in VehicleDet for output class file
3. Compile the DeteksiKendaraan.java class with Point.java class library, put it in out folder 
   **javac -d out src/Point.java src/DeteksiKendaraan.java**
4. Run this on terminal:
  ``` mkdir app-jar
   mkdir jdbc-jar
   cd app-jar
   jar xf ../DeteksiKendaraan.jar
   cd ../jdbc-j
   jar xf ../mysql-connector-java-8.0.xx.jar
  ```
5. Copy the contents of the JDBC JAR into the directory containing your application JAR: **cp -r jdbc-jar/\* app-jar/**
6. Create a new jar file from combined content
   **cd app-jar**
   **jar cf ../DeteksiKendaraan-fat.jar**
7. Add manifest information
   **echo "Main-Class: DeteksiKendaraan" > MANIFEST.MF**
   **jar ufm DeteksiKendaraan-fat.jar MANIFEST.MF**
8. Verify the JAR File (Make sure Jar file contains all your programs and needed drivers)
   **jar tf DeteksiKendaraan-fat.jar**
9. Run the fat JAR file
   **java -jar DeteksiKendaraan-fat.jar**

To run directly using JAR file, use the DeteksiKendaraan-fat.jar file.



