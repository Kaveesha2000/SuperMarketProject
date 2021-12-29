CREATE DATABASE SipsewanaInstitute;
SHOW DATABASES;
USE SipsewanaInstitute;

/*Student Table*/
DROP TABLE IF EXISTS Student;
CREATE TABLE IF NOT EXISTS Student(
    SId VARCHAR (6) NOT NULL PRIMARY KEY ,
    SName VARCHAR (25) ,
    Address VARCHAR (40) ,
    DOB VARCHAR (15) ,
    NIC VARCHAR (20) ,
    TNo VARCHAR (20) ,
    Course VARCHAR (25)
    );

/*Course Table*/
DROP TABLE IF EXISTS Course;
CREATE TABLE IF NOT EXISTS Course(
    CId VARCHAR (6) NOT NULL PRIMARY KEY ,
    CName VARCHAR (25) ,
    Duration VARCHAR (20) ,
    Fee DOUBLE
    );

/*Associate Table-RegisterDetails*/
DROP TABLE IF EXISTS RegisterDetails;
CREATE TABLE IF NOT EXISTS RegisterDetails(
    SId VARCHAR (6) ,
    CId VARCHAR (6) ,
    RegDate VARCHAR (20) ,
    FOREIGN KEY (SId) REFERENCES Student(SId) ,
    FOREIGN KEY (CId) REFERENCES Course(CId)
    );


SHOW TABLES;
DESC Student;
DESC Course;
DESC RegisterDetails;

/*Queries*/