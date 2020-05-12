package models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "projects")
public class Project {

    @DatabaseField(generatedId = true, unique = true, canBeNull = false)
    private int pnumber;

    @DatabaseField(canBeNull = false)
    private String pname;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Department department;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
