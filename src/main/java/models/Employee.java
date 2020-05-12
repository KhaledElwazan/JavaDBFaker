package models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "employees")
public class Employee {


    @DatabaseField(generatedId = true, unique = true, canBeNull = false)
    private int ssn;

    @DatabaseField(canBeNull = false)
    private String fname;

    @DatabaseField(canBeNull = false)
    private String lname;

    @DatabaseField
    private double salary;

    @DatabaseField(foreignAutoRefresh = true, foreign = true)
    private Address address;

    @DatabaseField
    private String email;

    @DatabaseField
    private Date dateOfBirth;

    @DatabaseField
    private char sex;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Department department;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
