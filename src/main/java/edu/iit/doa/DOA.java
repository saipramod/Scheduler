/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.iit.doa;

import edu.iit.model.User;
import edu.iit.model.User_Jobs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author supramo
 */
public class DOA {

    private Connection connect = null;
    private PreparedStatement preparedStatement = null;

    public void makeConnection() {
        // this will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // setup the connection with the DB.
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/itmd544?"
                            + "user=root&password=root");
        } catch (Exception e) {
            System.out.println("Could not make connection");
            System.exit(1);
        }

    }

    public void createTables() {
        try {
            preparedStatement = connect
                    .prepareStatement("CREATE TABLE IF NOT EXISTS users ("
                            + "userid varchar(30) PRIMARY KEY,"
                            + "emailid varchar(50),"
                            + "phonenumber int,"
                            + "password varchar(30)"
                            + ")");
            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("CREATE TABLE IF NOT EXISTS user_jobs ("
                            + "userid varchar(30),"
                            + "jobid varchar(255) PRIMARY KEY,"
                            + "jobstatus varchar(100),"
                            + "intputurl varchar(200),"
                            + "outputurl varchar(200),"
                            + "CONSTRAINT userid_key FOREIGN KEY (userid) REFERENCES users(userid)"
                            + ")");

            preparedStatement.executeUpdate();

            preparedStatement = connect
                    .prepareStatement("CREATE TABLE IF NOT EXISTS ec2_queue ("
                            + "ec2ip varchar(30),"
                            + "queuename varchar(255)"
                            + ")");

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connect.close();
        } catch (Exception e) {
            
            System.out.println("Table could not be created");
            System.exit(1);
        }

    }

    public void addEc2Queue(String ec2, String queue) {
        try {
            preparedStatement = connect
                    .prepareStatement("insert into ec2_queue values (?,?)");
            preparedStatement.setString(1, ec2);
            preparedStatement.setString(2, queue);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Addition failed");
        }
    }

    public void updateEc2Queue(String ec2, String queue) {
        try {
            preparedStatement = connect
                    .prepareStatement("update ec2_queue values set ec2ip = ? where queuename = ?");
            preparedStatement.setString(1, ec2);
            preparedStatement.setString(2, queue);

            preparedStatement.executeUpdate();
            connect.close();
        } catch (Exception e) {
            System.out.println("updatefailed failed");
        }
    }

    public void addUser(User user) {
        try {
            preparedStatement = connect
                    .prepareStatement("insert into user values (?,?,?,?)");
            preparedStatement.setString(1, user.getUserid());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmailid());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Could not add job to the database");
        }
    }

    public void addJob(User_Jobs userjob) {
        try {
            preparedStatement = connect
                    .prepareStatement("insert into user_jobs values (?,?,?,?,?)");
            preparedStatement.setString(1, userjob.getUserid());
            preparedStatement.setString(2, userjob.getJobid());
            preparedStatement.setString(3, userjob.getJobstatus());
            preparedStatement.setString(4, userjob.getInputurl());
            preparedStatement.setString(5, userjob.getOutputurl());
            preparedStatement.executeUpdate();
            connect.close();
        } catch (Exception e) {
            System.out.println("Could not add job to the database");
        }
    }

    public void updateJob(User_Jobs userjob) {
        try {
            preparedStatement = connect
                    .prepareStatement("update user_jobs set jobstatus=?,inputurl=?,outputurl=? where"
                            + "userid=? and jobid=?");
            preparedStatement.setString(4, userjob.getUserid());
            preparedStatement.setString(5, userjob.getJobid());
            preparedStatement.setString(1, userjob.getJobstatus());
            preparedStatement.setString(2, userjob.getInputurl());
            preparedStatement.setString(3, userjob.getOutputurl());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Could not update the job");
        }
    }
    public User getUser(String userid){
        User usr = new User();
        try {
            preparedStatement = connect
                    .prepareStatement("select * from user where"
                            + "userid=?");

            preparedStatement.setString(1, userid);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                usr.setUserid(userid);
                usr.setEmailid(rs.getString("emailid"));
                usr.setPhonenumber(rs.getString("phonenumber"));
                usr.setPassword(rs.getString("password"));
            }
            rs.close();
            preparedStatement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println("Could not update the job");
        }
        return usr;
    }
    
    public String getEc2Queue(String ec2ip){
        String queuename = "";
        try{
            preparedStatement = connect
                    .prepareStatement("select * from ec2_queue where"
                            + "ec2ip=?");
            preparedStatement.setString(1,ec2ip);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
                queuename = rs.getString("queuename");
            rs.close();
            preparedStatement.close();
            connect.close();
        }
        catch(Exception e){
            System.out.println("sai is aesome");
        }
        return queuename;
    }
}