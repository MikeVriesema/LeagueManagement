/**
 * LeagueManagement
 */
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class LeagueManagement
{
    static StringBuilder username;
    static int usernameID;
    static File admin;
    static File leagues;

    public static void main(String[] args) throws IOException
    {
        boolean loggedIn = false;
        admin = new File("administrators.txt");
        leagues = new File("leagues.txt");
        String options[] = {"Log in", "Create New Admin", "Quit"};
	    String tableOptions[] = {"Create League", "Manage Existing League", "Log out", "Ouit"};
        int choice = 0;
	    int choice2 = 0;
        String loggedInUser = "You are currently not logged in:\n\n";
        username = new StringBuilder("");

        checkSetup(admin, leagues);
      
		do
        {
            if(!loggedIn)
            {
                choice = JOptionPane.showOptionDialog(null, loggedInUser + "What would you like to do?", "League Manager",JOptionPane.YES_NO_OPTION, 
                                                    1, null, options, options[0]);

                if(choice == 0)
                {
                    loggedIn = logInSequence(admin, username);
                    if(loggedIn)
                        loggedInUser = "Logged in: " + username + "\n\n";
                        usernameID = findAdminIdentifierNumber(username.toString()); //sets global varibale to the logged in users ID
                }
                else if(choice == 1)
                {

                }
            }
			//losg
            else
            {
                choice2 = JOptionPane.showOptionDialog(null, loggedInUser + "Select a Menu:", "League Manager",JOptionPane.YES_NO_OPTION, 
                                                    1, null, tableOptions, options[0]);
													
				if (choice2 == 2){
					
					username = new StringBuilder("");
					loggedIn = false;
					loggedInUser = "Not logged in:\n\n";
					choice2 = 0;
				}
            }	
        }while(choice != 2 && choice2 !=3);
    }


    public static boolean logInSequence(File admin, StringBuilder user) throws IOException
    {
        boolean loggedIn = false;
        String inputName = "";
        String inputPassword = "";
        String passwordPattern = "[a-zA-Z0-9]{5,}";


        inputName = JOptionPane.showInputDialog(null, "Please enter admin name");
        if(inputName != null)
        {
            if(doesInputExist(admin, inputName, false))
            {
                String temp[];
                int attempts = 0;
                int index = findAdminIdentifierNumber(inputName);
                temp = stringAtLineNumber(admin, index).split(",");
                while(attempts < 3)
                {
                    inputPassword = JOptionPane.showInputDialog(null, "Please enter password for " + temp[1]);
                    if(inputPassword != null)
                    {
                        if(inputPassword.matches(passwordPattern) && inputPassword.equals(temp[2]))
                        {
                            JOptionPane.showMessageDialog(null, "Succesfully logged in!");
                            user.append(temp[1]);
                            loggedIn = true;
                            break;
                        }
                        else if(attempts < 3)
                        {
                            if(attempts == 2)
                            {
                                attempts++;
                                JOptionPane.showMessageDialog(null, "No attempts left!");
                            }
                            else
                            {    
                                attempts++;
                                JOptionPane.showMessageDialog(null, "Incorrect password\n" + (3 - attempts) + " attempts left");
                            }
                        }
                    }
                    else
                        break;
                }
            }
            else
                JOptionPane.showMessageDialog(null, inputName + " is not an existing admin");
        }

        return loggedIn;
    }
    //Rian
    public static void checkSetup(File input1, File input2) throws IOException
    {
        if(!input1.exists() && !input2.exists())
        {
            FileWriter file1 = new FileWriter(input1);
            FileWriter file2 = new FileWriter(input2);
            file1.close();
            file2.close();
        }
        else if(!input1.exists())
        {
            FileWriter file1 = new FileWriter(input1);
            file1.close();
        }
        else if(!input2.exists())
        {
            FileWriter file2 = new FileWriter(input2);
            file2.close();
        }
    }
    //Rian
    public static boolean doesInputExist(File filename, String input, boolean caseSensitive) throws IOException
    {
        if(filename.exists())
        {
            Scanner in = new Scanner(filename);
            while(in.hasNext())
            {
                if(caseSensitive)
                {
                    String aLineFromFile = in.nextLine();
                    if(aLineFromFile.contains(input))
                    {
                      in.close();
                     return true;
                    }
                }
                else
                {
                    String aLineFromFile = in.nextLine().toLowerCase();
                    if(aLineFromFile.contains(input.toLowerCase()))
                    {
                        in.close();
                        return true;
                    }
                }
            }
            in.close();
        }
        return false;
    }

    //Rian
    public static String stringAtLineNumber(File file, int lineNumber) throws IOException
    {
        String result = "";
        if(file.exists())
        {
            Scanner in = new Scanner(file);

            for(int i = 0; i < lineNumber && in.hasNext(); i++)
                result = in.nextLine();
                
            in.close();
        }
        return result;
    }

    //Modified by Rian
    public static int findAdminIdentifierNumber(String adminName) throws IOException
    {
        int identifier = 0;
        Scanner in = new Scanner(admin);
        String aLineFromFile = "";
        
        
        
        while(in.hasNext())
        {
            aLineFromFile = in.nextLine();
            String temp[] = aLineFromFile.split(",");

            if(temp[1].toLowerCase().equals(adminName.toLowerCase()))
            {
                identifier = Integer.parseInt(temp[0]);
                break;
            }
        }
        in.close();

        return identifier;
    }
	
	
	//love from RyAn 
	public static String displayTeams() throws IOException //this method needs some work to make it more general
	{
		String teams="";
		FileReader aFileReader = new FileReader("teams.txt");
		Scanner in = new Scanner(aFileReader);
		String aLineFromFile;
		while(in.hasNext())
		{
			
			aLineFromFile = in.nextLine();
		    String single[] = aLineFromFile.split(",");
			teams   += "\n" + single[1];
			
			
		}
		in.close();
		aFileReader.close();
		return teams;
	}
	
	
	
	//Love From rYaN
	/*public static void createAdmin() throws IOException  //creates admin username and password and stores them in the file administrator.txt already made by rian 
	{
		int lastNumber, newNumber;
        String adminName , password;
        boolean userCreated = false;
		
		FileWriter aFileWriter = new FileWriter(admin, true); 
		PrintWriter out = new PrintWriter(aFileWriter);
		
		lastNumber = LeagueManagementRyan.checklastnumber();
		newNumber = lastNumber + 1 ;
		
		while(userCreated == false)
		{
			adminName = JOptionPane.showInputDialog(null,"Please enter the username you wish to use:","CreateAdmin",1);
			adminName = adminName.trim();
			if ( adminName != null)
			{
				if(stringCheck(adminName))
				{
                    while(true)
                    {
                        password = JOptionPane.showInputDialog(null,"Please enter the password you wish to use:","CreatePassword",1);
                        password = password.trim();
                        
                        if(password != null)
                        {
                            if(passwordCheck(password))
                            {
                                out.println(newNumber +","+adminName+"," + password);
                                userCreated = true;
                                break;
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null,"The password you entered is invalid!","PasswordError",1);
                            }
                        }
                    }
                }
				else
				{
					JOptionPane.showMessageDialog(null,"The user name you entered is invalid!","UserNameError",1);
				}
            }		
        }   
		out.close();
		aFileWriter.close();
	}*/
    
    public static boolean stringCheck(String input) 
	{
		String pattern = "[a-z A-Z]{1,20}"; 
		return(input.matches(pattern));
	}
	
	//Mitch,password against pattern
	public static boolean integerCheck(int input) 
	{
		String resultInt = ""+input;
		String pattern = "[0-9]{1,500}"; 
		return(resultInt.matches(pattern));
	}
	
	//Mitch,general number check against pattern 
	public static boolean passwordCheck(String input) 
	{
		String resultPassword = ""+input;
		String pattern = "[A-Za-Z0-9]{5,30}"; 
		return(resultPassword.matches(pattern));
	}
}
