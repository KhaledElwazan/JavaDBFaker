import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;
import me.tongfei.progressbar.ProgressBar;
import models.Address;
import models.Student;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
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
}
