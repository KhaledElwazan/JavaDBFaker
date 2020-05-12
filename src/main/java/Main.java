import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import me.tongfei.progressbar.ProgressBar;
import models.*;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Main {
    private static String URL;

    public static void main(String[] args) throws SQLException, ParseException, InvocationTargetException, IllegalAccessException {


        Options options = new Options();

        options.addOption(Option.builder().longOpt("host").argName("host").hasArg().build());
        options.addOption(Option.builder().longOpt("port").argName("port").hasArg().build());
        options.addOption(Option.builder().longOpt("db").argName("db").hasArg().build());
        options.addOption(Option.builder().longOpt("user").argName("user").hasArg().build());
        options.addOption(Option.builder().longOpt("pass").argName("pass").optionalArg(true).hasArg().build());

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);


        String host = "localhost";
        String db = "fakedb";
        String port = "3306";
        String user = "root";
        String pass = "";

        if (cmd.hasOption("host"))
            host = cmd.getOptionValue("host");

        if (cmd.hasOption("db"))
            db = cmd.getOptionValue("db");

        if (cmd.hasOption("port"))
            port = cmd.getOptionValue("port");

        if (cmd.hasOption("user"))
            user = cmd.getOptionValue("user");

        if (cmd.hasOption("pass"))
            pass = cmd.getOptionValue("pass");


        URL = "jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass;


        //commence local CLI
        cli();
    }

    public static void createFakeStudents(int size) throws SQLException {

        Fairy fairy = Fairy.create();

        ConnectionSource connection = new JdbcConnectionSource(URL);

        Dao<Address, ?> addressDao = DaoManager.createDao(connection, Address.class);
        TableUtils.dropTable(addressDao, true);
        TableUtils.createTableIfNotExists(connection, Address.class);


        Dao<Student, ?> studentDao = DaoManager.createDao(connection, Student.class);
        TableUtils.dropTable(studentDao, true);
        TableUtils.createTableIfNotExists(connection, Student.class);


        //create fake students
        ProgressBar progressBar = new ProgressBar("Creating fake students", size);


        for (int i = 0; i < size; i++) {
            Person person = fairy.person();
            Student student = new Student();
            student.setFirstName(person.firstName());
            student.setLastName(person.lastName());
            student.setEmail(person.email());
            student.setDateOfBirth(person.dateOfBirth().toDateTime().toDate());
            student.setSex(person.sex().name().charAt(0));
            student.setPhoneNumber(person.telephoneNumber());
            student.setPassportNumber(person.passportNumber());
            student.setMiddleName(person.middleName());
            Address address = new Address();
            address.setApartmentNumber(person.getAddress().apartmentNumber());
            address.setApartmentNumber(person.getAddress().apartmentNumber());
            address.setCity(person.getAddress().getCity());
            address.setPostalCode(person.getAddress().getPostalCode());
            address.setStreet(person.getAddress().street());
            address.setStreetNumber(person.getAddress().streetNumber());
            address = addressDao.createIfNotExists(address);
            student.setAddress(address);
            student = studentDao.createIfNotExists(student);
            progressBar.step();
        }
        progressBar.close();
        connection.closeQuietly();

    }

    public static void createFakeCompany() throws SQLException {

        Fairy fairy = Fairy.create();
        JdbcConnectionSource connectionSource = new JdbcConnectionSource(URL);

        Dao<Address, ?> addressDao = DaoManager.createDao(connectionSource, Address.class);
        TableUtils.dropTable(addressDao, true);
        TableUtils.createTableIfNotExists(connectionSource, Address.class);

        Dao<Employee, ?> employeeDao = DaoManager.createDao(connectionSource, Employee.class);


        TableUtils.dropTable(employeeDao, true);
        TableUtils.createTableIfNotExists(connectionSource, Employee.class);



        List<Employee> employeeList = new ArrayList<>();


        //create fake employees
        int fakeEmp = 500;
        ProgressBar progressBar1 = new ProgressBar("Creating fake employees", fakeEmp);
        for (int i = 0; i < fakeEmp; i++) {
            Person person = fairy.person();
            Employee employee = new Employee();
            employee.setFname(person.firstName());
            employee.setLname(person.lastName());
            employee.setDateOfBirth(person.dateOfBirth().toDate());

            Address address = new Address();
            address.setApartmentNumber(person.getAddress().apartmentNumber());
            address.setApartmentNumber(person.getAddress().apartmentNumber());
            address.setCity(person.getAddress().getCity());
            address.setPostalCode(person.getAddress().getPostalCode());
            address.setStreet(person.getAddress().street());
            address.setStreetNumber(person.getAddress().streetNumber());
            address = addressDao.createIfNotExists(address);

            employee.setAddress(address);
            employee.setEmail(person.email());
            employee.setSex(person.sex().name().charAt(0));
            employee.setSalary(generateRandom(1000, 10000));

            employee = employeeDao.createIfNotExists(employee);
            employeeList.add(employee);
            progressBar1.step();
        }
        progressBar1.close();

        Dao<Department, ?> departmentDao = DaoManager.createDao(connectionSource, Department.class);
        TableUtils.dropTable(departmentDao, true);
        TableUtils.createTableIfNotExists(connectionSource, Department.class);

        List<Department> departmentList = new ArrayList<>();

        //create fake departments
        int fakeDept = 10;
        ProgressBar progressBar2 = new ProgressBar("Creating fake departments", fakeDept);
        for (int i = 0; i < fakeDept; i++) {

            Department department = new Department();
            department.setDname(fairy.textProducer().word(1));
            int randEmp = generateRandom(0, employeeList.size() - 1);
            department.setMgrssn(employeeList.get(randEmp));
            department.setMgrstDate(fairy.dateProducer().randomDateBetweenYearAndNow(1950).toDate());
            department = departmentDao.createIfNotExists(department);
            departmentList.add(department);
            progressBar2.step();
        }
        progressBar2.close();

        //assign each employee to a department
        ProgressBar progressBar3 = new ProgressBar("Assigning each employee to a department", employeeList.size());
        for (int i = 0; i < employeeList.size(); i++) {

            int randDept = generateRandom(0, departmentList.size() - 1);
            employeeList.get(i).setDepartment(departmentList.get(randDept));
            employeeDao.update(employeeList.get(i));
            employeeDao.refresh(employeeList.get(i));
            progressBar3.step();
        }
        progressBar3.close();
        Dao<DepartmentLocation, ?> departmentLocationDao = DaoManager.createDao(connectionSource, DepartmentLocation.class);
        TableUtils.dropTable(departmentLocationDao, true);
        TableUtils.createTableIfNotExists(connectionSource, DepartmentLocation.class);

        List<DepartmentLocation> departmentLocationList = new ArrayList<>();

        //create fake department locations
        ProgressBar progressBar4 = new ProgressBar("Creating fake departments' locations", departmentList.size());
        for (int i = 0; i < departmentList.size(); i++) {

            DepartmentLocation departmentLocation = new DepartmentLocation();
            departmentLocation.setdLocation(fairy.person().getAddress().getCity());
            int randDept = generateRandom(0, departmentList.size() - 1);
            departmentLocation.setdNumber(departmentList.get(randDept));
            departmentLocation = departmentLocationDao.createIfNotExists(departmentLocation);
            departmentLocationList.add(departmentLocation);
            progressBar4.step();
        }
        progressBar4.close();


        //create fake projects
        Dao<Project, ?> projectDao = DaoManager.createDao(connectionSource, Project.class);
        TableUtils.dropTable(projectDao, true);
        TableUtils.createTableIfNotExists(connectionSource, Project.class);

        List<Project> projectList = new ArrayList<>();

        int fakeProj = 200;
        ProgressBar progressBar5 = new ProgressBar("Creating fake projects", fakeProj);
        for (int i = 0; i < fakeProj; i++) {

            Project project = new Project();
            int randDept = generateRandom(0, departmentList.size() - 1);
            project.setDepartment(departmentList.get(randDept));
            project.setPname(fairy.textProducer().word(1));

            project = projectDao.createIfNotExists(project);
            projectList.add(project);
            progressBar5.step();
        }
        progressBar5.close();

        Dao<WorksOn, ?> worksOns = DaoManager.createDao(connectionSource, WorksOn.class);
        TableUtils.dropTable(worksOns, true);
        TableUtils.createTableIfNotExists(connectionSource, WorksOn.class);

        //assign each employee to work for a certain project
        employeeList = employeeDao.queryForAll();
        ProgressBar progressBar6 = new ProgressBar("Assigning each employee to work for a certain project", employeeList.size());
        for (int i = 0; i < employeeList.size(); i++) {
            WorksOn worksOn = new WorksOn();
            worksOn.setEmployee(employeeList.get(i));
            int randProj = generateRandom(0, projectList.size() - 1);
            worksOn.setProject(projectList.get(randProj));
            worksOn.setHours(generateRandom(1, 1000));
            worksOns.createIfNotExists(worksOn);
            progressBar6.step();
        }
        progressBar6.close();

        connectionSource.closeQuietly();

    }

    private static void cli() throws InvocationTargetException, IllegalAccessException {
        Class<Main> mainClass = Main.class;

        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.next();
            for (Method method : mainClass.getMethods()) {

                if (line.equals(method.getName())) {

                    Integer[] params = new Integer[method.getParameterCount()];
                    for (int i = 0; i < method.getParameterCount(); i++) {

                        params[i] = scan.nextInt();
                    }

                    method.invoke(new Object(), params);

                    break;
                }

            }

        }
    }

    public static void help() throws InvocationTargetException, IllegalAccessException {


        Class<Main> mainClass = Main.class;

        for (Method method : mainClass.getMethods()) {

            if (Modifier.isStatic(method.getModifiers()))
                System.out.println(method.getName());

        }


    }

    public static void exit() {
        System.exit(0);
    }

    private static int generateRandom(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
