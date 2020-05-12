package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "depart_loc")
public class DepartmentLocation {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreignAutoRefresh = true, foreign = true, uniqueCombo = true)
    private Department dNumber;

    @DatabaseField(uniqueCombo = true, canBeNull = false)
    private String dLocation;

    public Department getdNumber() {
        return dNumber;
    }

    public void setdNumber(Department dNumber) {
        this.dNumber = dNumber;
    }

    public String getdLocation() {
        return dLocation;
    }

    public void setdLocation(String dLocation) {
        this.dLocation = dLocation;
    }
}
