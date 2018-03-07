/**
 * LeagueManagement
 */
import javax.swing.*;
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
        
        checkSetup();
      
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
                        usernameID = findAdminIdentifierNumber(username.toString());
                }
                else if(choice == 1)
                {
                    createAdmin();
                }
            }
			//losg
            else
                choice2 = JOptionPane.showOptionDialog(null, loggedInUser + "Select a Menu:", "League Manager",JOptionPane.YES_NO_OPTION, 
                                                    1, null, tableOptions, options[0]);
													
				if (choice2 == 2){
					
					username = new StringBuilder("");
					loggedIn = false;
					loggedInUser = "Not logged in:\n\n";
					choice2 = 0;
					
					
				}
				
        }while(choice != 2 && choice2 !=3);
    }


    public static boolean logInSequence(File adminFile, StringBuilder user) throws IOException
    {
         boolean loggedIn = false;
        String inputName = "";
        String inputPassword = "";

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
                        if(passwordCheck(inputPassword) && inputPassword.equals(temp[2]))
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
    public static void checkSetup() throws IOException
    {
         if(!admin.exists() && !leagues.exists())
        {
            FileWriter file1 = new FileWriter(admin);
            FileWriter file2 = new FileWriter(leagues);
            file1.close();
            file2.close();
        }
        else if(!admin.exists())
        {
            FileWriter file1 = new FileWriter(admin);
            file1.close();
        }
        else if(!leagues.exists())
        {
            FileWriter file2 = new FileWriter(leagues);
            file2.close();
        }
    }
    //Rian
     /*
    DoesInputExist - returns a boolean saying whether a provided string exists in a file
    Inputs - filename: file to search, input: string to search for, caseSensitive - use for case sensitivity
    */
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
    /*
    stringAtLineNumber - returns the string at the line of a provided file
    Inputs - file: file to be searched, lineNumber - the linenumber you want the string of
    Searches the file using a loop with index of lneNumber to get the string and return it
    */ 
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
    /*
    findAdminIdentifierNumber - returns the identifier number of a provided admin
    Inputs - adminName: name of admin whose ID you want to get
    Searches for admin name in the file, splits on comma and returns the int ID
    */
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
	
	
	public static void createAdmin() throws IOException  //creates admin username and password and stores them in the file administrator.txt already made by rian 
	{
		int lastNumber, newNumber;
        String adminName , password;
        boolean userCreated = false;
		
		FileWriter aFileWriter = new FileWriter(admin, true); 
		PrintWriter out = new PrintWriter(aFileWriter);
		
		lastNumber = findLastAdminNumber();
		newNumber = lastNumber + 1 ;
		
		while(userCreated == false)
		{
			adminName = JOptionPane.showInputDialog(null,"Please enter the username you wish to use:","CreateAdmin",1);
			if (adminName != null)
			{
                adminName = adminName.trim();
				if(stringCheck(adminName))
				{
                    if(!doesInputExist(admin, adminName, false))
                    {
                        while(true)
                        {
                            password = JOptionPane.showInputDialog(null,"Please enter the password you wish to use:","CreatePassword",1);
                            if(password != null)
                            {
                                password = password.trim();
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
                            else
                                break;
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "This username is not available");
                }
				else
				{
					JOptionPane.showMessageDialog(null,"The user name you entered is invalid!","UserNameError",1);
				}
            }
            else
                break;		
        }   
		out.close();
		aFileWriter.close();
	}
	
	//Mitch,string check against pattern
    public static boolean stringCheck(String input) 
    {
        String pattern = "[a-z A-Z]{1,20}"; 
        return(input.matches(pattern));
    }

    //Mitch,number against pattern
    public static boolean integerCheck(int input) 
    {
        String resultInt = ""+input;
        String pattern = "[0-9]{1,500}"; 
        return(resultInt.matches(pattern));
    }

    //Mitch,general password check against pattern 
    public static boolean passwordCheck(String input) 
    {
        String resultPassword = ""+input;
        String pattern = "[A-Za-Z0-9]{5,30}"; 
        return(resultPassword.matches(pattern));
    }
	
    /*
    Returns the number of the last admin in the administrators file
    Inputs - None
    Loops through every line until the end is reached, splits the last line on a comma
    returns the ID number
    */
    public static int findLastAdminNumber() throws IOException
    {
        int idNumber = 0;
        Scanner in = new Scanner(admin);
        String lastLine = "";

        if(admin.length() == 0)
        {    
            in.close();
            return 0;
        }
        else
        {
            while(in.hasNext())
                lastLine = in.nextLine();
        
            idNumber = Integer.parseInt(lastLine.substring(0, lastLine.indexOf(",")));
            in.close();
            return idNumber;
        }
    }
    
    //Rian
    /*
    inputLeagueParticipants - Input participants until cancelled is pressed
    Each input of the JOPtionpane is placed into an ArrayList. The array list is returned
    */
    public static ArrayList<String> inputLeagueParticipants()
    {
        ArrayList<String> participants = new ArrayList<String>();
        String input = "";

        while(true)
        {
            input = JOptionPane.showInputDialog(null, "Input participant");
            if(input != null)
                if(stringCheck(input))
                    participants.add(input);
                else
                    JOptionPane.showMessageDialog(null, "Incorrect format of participant");
            else
                break;
        }

        return participants;
    }

	//Mitch,//identify league admin
	public static void getAdminLeagues() throws IOException 
        FileReader aFileReader = new FileReader("leagues.txt");
        Scanner in = new Scanner(aFileReader);
        //int idNum = findIdentifierNumber(admin,username.toString());
		int i = 0;
        while(in.hasNextLine())
        {   
            String data = in.nextLine();
            if(data.contains(","))
            { 
                leagueAdminNum=(data.substring((data.lastIndexOf(",")+1)));
				if(usernameID ==  Integer.parseInt(leagueAdminNum))
				{
					leagueInsert=(data.substring((data.indexOf(",")+1), data.lastIndexOf(","))); 
					tableChoices[i]=leagueInsert;
					i++;
				}
            }
        }   
		in.close();
        aFileReader.close();
        out.close();
        aFileWriter.close();
	}
	/**
	\\MITCH// FOR LATER
	public static void getAdminLeagues() throws IOException
    {
        ArrayList<ArrayList<String>> leagues = new ArrayList<ArrayList<String>>();
        String filename = "leagues.txt";
        String fileError = filename + " not found";
        String leagueError = "No leagues created";
        File leaguesFile = new File(filename);
        String leagueElements[];
        int    leagueAdminID, adminID;
        boolean found = false;
        if (!(leaguesFile.exists()))
        {
            System.out.println(fileError);
        }
        else if (leaguesFile.length() == 0)
        {
            System.out.println(leagueError);
        }
        else
        {
            Scanner in = new Scanner(leaguesFile);
            leagues.add(new ArrayList<String>());
            leagues.add(new ArrayList<String>());
            leagues.add(new ArrayList<String>());
            while (in.hasNext())
            {
                leagueElements = (in.nextLine()).split(",");
                for (int i = 0; i < leagueElements.length; i++)
                {
                    (leagues.get(i)).add(leagueElements[i]);
                }
            }
            in.close();
            for (int list = 0; list < leagues.size(); list++)
            {
                for (int item = 0; item < leagues.get(list).size(); item++)
                {
                    System.out.println(leagues.get(list).get(item));
                }
            }
        }
    }
	**/

    //Mitch,sort leagues for respective admin
    public static void editLeague() throws IOException //identify league
    {
        String input = (String) JOptionPane.showInputDialog(null, "Choose a league to manage:",
        "League Management",JOptionPane.QUESTION_MESSAGE, null, tableChoices,tableChoices[1]); 
        in.close();	
    }

	//Mitch,general delete league method 
    public static void deleteLeague() throws IOException //identify league
    {
        
    }
	
	//Mitch,Generation of League Table
	public static void generateLeagueTable()
    {
        String[][] leagueTable = new String[][]{
                {"1","leagues.get(i)","x","x","x","x","x"},
                {"2","leagues.get(i)","x","x","x","x","x"},
                {"3","leagues.get(i)","x","x","x","x","x"},
                {"4","leagues.get(i)","x","x","x","x","x"},
                {"5","leagues.get(i)","x","x","x","x","x"},
                {"6","leagues.get(i)","x","x","x","x","x"},
                {"7","leagues.get(i)","x","x","x","x","x"},
                {"8","leagues.get(i)","x","x","x","x","x"}};
        String[] columnNames = {"Position","Team","Games","Wins","Draws","Losses","Score"};
        String[][] data = new String[leagueTable.length][leagueTable[0].length];
        for (int i = 0; i < data.length; i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                data[i][j] = leagueTable[i][j];
            }
        }
		//--Important table stuff
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
		//--Important table stuff
        JOptionPane.showMessageDialog(null, scrollPane, "Output",JOptionPane.INFORMATION_MESSAGE);
    }
}
