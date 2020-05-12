package models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "works_on")
public class WorksOn {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField( foreign = true,foreignAutoRefresh = true,canBeNull = false)
    private Employee employee;


    @DatabaseField( foreign = true,foreignAutoRefresh = true,canBeNull = false)
    private Project project;

    @DatabaseField
    private double hours;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }
}
