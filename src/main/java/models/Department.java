package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

@DatabaseTable(tableName = "departments")
public class Department {

    @DatabaseField(generatedId = true, unique = true, canBeNull = false)
    private int dnum;

    @DatabaseField(canBeNull = false)
    private String dname;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = true)
    private Employee Mgrssn;

    @DatabaseField
    private Date MgrstDate;


    @ForeignCollectionField
    private Collection<Project> projects;


    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Employee getMgrssn() {
        return Mgrssn;
    }

    public void setMgrssn(Employee mgrssn) {
        Mgrssn = mgrssn;
    }

    public Date getMgrstDate() {
        return MgrstDate;
    }

    public void setMgrstDate(Date mgrstDate) {
        MgrstDate = mgrstDate;
    }
}
