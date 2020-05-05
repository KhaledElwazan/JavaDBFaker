# JavaDBFaker
## This tool is used to fill databases with dummy data for testing and educational purposes. 
### Currently it creates:
  - a table of students and their addresses (one-to-one relationship)
  
### To run this tool, write the following:
  `java -cp JavaDBFaker.jar Main --host 19.180.11.1 --port 3308 --db fakedb --user username --pass password`
  
  Or you could simply run 
  
  `java -cp JavaDBFaker.jar Main `
  
  to run the tool with the default values, `host=localhost`, `port=3308`, `db=fakedb`, `user=root`, `and pass=''`
  
  After running the previous command, you'll be directed to a local cli; write the following:
  * to create a table of students and their addresses:
  
    `createFakeStudents 100`
    
    where 100 is a number indicating the number of records in the tables.
