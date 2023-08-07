package EmployeeData;

import java.util.*;
import java.io.*;

class FileHandling
{	
	public Scanner sobj;
	public int id;
	public File fo;
	
	// create scanner and file objects.
	public FileHandling()
	{
		sobj = new Scanner(System.in);
		id = 1;
		fo = new File("employeeData.txt");
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// check to file is open or not.
	public boolean CheckOpenOrNot() throws Exception
	{
		if(fo.exists())
		{
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// To display all data from file.
	public void ReadAllData() throws Exception
	{
		RandomAccessFile randomAccessFile = new RandomAccessFile("employeeData.txt", "rw");
		// Read data from the file
		randomAccessFile.seek(0);
        StringBuilder fileContent = new StringBuilder();
        int data;
        while ((data = randomAccessFile.read()) != -1) {
            fileContent.append((char) data);
        }
        randomAccessFile.close();
        
        // Check if the file content is empty
        if (fileContent.length() == 0) {
            System.out.println("File is empty.");
        } else {
            System.out.println("File content: \n" + fileContent.toString());
        }
	}
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// to extract file from line
	public int extractIdFromLine(String line) {
	    int id = 0;
	    String[] parts = line.split(":");
	    for (String part : parts) {
	        if (part.contains("Id")) {
	            String idStr = part.split("=")[1].trim();
	            id = Integer.parseInt(idStr);
	            break;
	        }
	    }
	    return id;
	}
	
	
	public int getLastIdFromFile() {
	    int highestId = 0;

	    try (BufferedReader reader = new BufferedReader(new FileReader(fo))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            int id = extractIdFromLine(line);
	            highestId = Math.max(highestId, id);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return highestId;
	}

	public void InsertData() throws Exception
	{
		try (PrintWriter writer = new PrintWriter(new FileWriter(fo, true))) {
	        System.out.println("Enter the all details of new employee : \n");
	        System.out.println("Enter name : ");
	        String name = this.sobj.next();
	        System.out.println("Enter Salary : ");
	        int salary = sobj.nextInt();
	        System.out.println("Enter city : ");
	        String city = this.sobj.next();
	        System.out.println("Enter Age : ");
	        int age = this.sobj.nextInt();
	        int highestId = getLastIdFromFile();
	        writer.println("Id = " + (highestId+1) + " : Name = " + name + " : Salary = " + salary + " : City = " + city + " : Age = " + age + ".");
	        id++;
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private String getEmployeeRecordById(int targetId) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(fo))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            int id = extractIdFromLine(line);
	            if (id == targetId) {
	                return line;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null; // ID not found
	}

	public void displaySelectedEmployeeData() throws Exception {
	    System.out.println("Enter the employee ID to display:");
	    int targetId = sobj.nextInt();

	    String employeeRecord = getEmployeeRecordById(targetId);
	    if (employeeRecord != null) {
	        System.out.println("Employee record: " + employeeRecord);
	    } else {
	        System.out.println("Employee with ID " + targetId + " not found.");
	    }
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// Update data
	public void updateEmployeeAttributeById(int targetId) throws Exception {
        List<String> updatedRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = extractIdFromLine(line);
                if (id == targetId) {
                    System.out.println("Enter updated name: ");
                    String updatedName = sobj.next();
                    System.out.println("Enter updated salary: ");
                    int updatedSalary = sobj.nextInt();
                    System.out.println("Enter updated city: ");
                    String updatedCity = sobj.next();
                    System.out.println("Enter updated age: ");
                    int updatedAge = sobj.nextInt();

                    line = updateAttribute(line, "Name", updatedName);
                    line = updateAttribute(line, "Salary", String.valueOf(updatedSalary));
                    line = updateAttribute(line, "City", updatedCity);
                    line = updateAttribute(line, "Age", String.valueOf(updatedAge));
                }
                updatedRecords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fo))) {
            for (String record : updatedRecords) {
                writer.println(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String updateAttribute(String line, String attributeName, String updatedValue) {
        return line.replaceFirst(attributeName + " = .*? :", attributeName + " = " + updatedValue + " :");
    }
	    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	    
	
    // delete data
    public void deleteEmployeeById(int targetId) throws Exception 
    {
        List<String> remainingRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = extractIdFromLine(line);
                if (id != targetId) {
                    remainingRecords.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fo))) {
            for (String record : remainingRecords) {
                writer.println(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Emloyee 
{
	public static void main(String args[]) throws Exception
	{
		System.out.println("***Welcome to Employee Management System Application***");
		
		FileHandling fobj = new FileHandling();
		
		boolean check = fobj.CheckOpenOrNot();
		if(check == true)
		{
			System.out.println("File is opened Succesfully...");
		}
		else
		{
			System.out.println("Error to open file...");
			return;
		}
		Scanner sobj = new Scanner(System.in);
		
		while(true)
		{
			System.out.println("\nYou can Perform following operation on the employee data : \n");
			
			System.out.println("Press 1 to display all data of employees.");
			System.out.println("Press 2 to display selected employee data.");
			System.out.println("Press 3 to insert new employee data.");
			System.out.println("Press 4 to ubdate employee data.");
			System.out.println("Press 5 to delete employee data.");
			System.out.println("Press 6 to exit from application.");
			
			System.out.println("\nEnter your choice number : ");
			
			int Choice = sobj.nextInt();
			
			if(Choice == 6)
			{
				System.out.println("THANKS FOR USING APPLICATION");
				break;
			}
			
			switch(Choice)
			{
				case 1:
					fobj.ReadAllData();
					break;
					
				case 2:
					fobj.displaySelectedEmployeeData();
					break;
					
				case 3:
					fobj.InsertData();
					break;
					
				case 4:
					
					
					System.out.println("Enter the id of employee id to update data : ");
					int id = sobj.nextInt();
					
//					System.out.println("What do you want to change : ");
//					System.out.println("Enter 1 for name");
//					System.out.println("Enter 2 for Salary");
//					System.out.println("Enter 3 for city");
//					System.out.println("Enter 4 for Age");
//					
//					System.out.println("Enter your choice : ");
//					int c = sobj.nextInt();
//					String Data = new String();
//					if(c == 1)
//					{
//						System.out.println("Enter the name : ");
//						Data = sobj.next();
//						
//					}
//					else if(c == 2)
//					{
//						System.out.println("Enter the salary : ");
//						Data = sobj.next();
//					}
//					else if(c == 3)
//					{
//						System.out.println("Enter the city : ");
//						Data = sobj.next();
//					}
//					else if(c == 4)
//					{
//						System.out.println("Enter the age : ");
//						Data = sobj.next();
//					}
//					
					fobj.updateEmployeeAttributeById(id);
					break;
					
				case 5:
					System.out.println("Enter the id number whoes data you :");
					int num = sobj.nextInt();
					fobj.deleteEmployeeById(num);
					break;

					
				default:
		
					break;
			}
		}
		sobj.close();
	}
}
