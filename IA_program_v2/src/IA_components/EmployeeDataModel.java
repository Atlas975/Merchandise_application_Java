package IA_components;
import java.sql.Date;
public class EmployeeDataModel {
    int empID;
    String firstName;
    String lastName;
    Date joinDate;
    String gender;
    String jobTitle;
    int salary;
    int superID;
    int branchID;

    public EmployeeDataModel(int empID, String firstName, String lastName, Date joinDate, String gender, String jobTitle, int salary, int superID, int branchID) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.gender = gender;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.superID = superID;
        this.branchID = branchID;
    }

    public int getEmpID() {
        return empID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getGender() {
        return gender;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getSalary() {
        return salary;
    }

    public int getSuperID() {
        return superID;
    }

    public int getBranchID() {
        return branchID;
    }


}
