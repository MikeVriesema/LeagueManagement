/**
 * LeagueManagement
 */
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class LeagueManagement
{
    static StringBuilder username;
    static int usernameID;

    public static void main(String[] args) throws IOException
    {
        boolean loggedIn = false;
        File admin = new File("administrators.txt");
        File leagues = new File("leagues.txt");
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
                        usernameID = findIdentifierNumber(admin, username.toString());
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
        String passwordPattern = "[a-zA-Z0-9]{5,}";


        inputName = JOptionPane.showInputDialog(null, "Please enter admin name");
        if(inputName != null)
        {
            if(doesInputExist(adminFile, inputName, false))
            {
                String temp[];
                int attempts = 0;
                int index = findIdentifierNumber(adminFile, inputName);
                temp = stringAtLineNumber(adminFile, index).split(",");
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
    public static int findIdentifierNumber(File aFile, String adminName) throws IOException
    {
        int identifier = 0;
        Scanner in = new Scanner(aFile);
        String aLineFromFile = "";
        
        if(aFile.exists())
        {
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
        }
        else 
            JOptionPane.showMessageDialog(null, "This admin does not exist");
        
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
	public static void createAdmin() throws IOException  //creates admin username and password and stores them in the file administrator.txt already made by rian 
	{
		int lastNumber, newNumber;
		String adminName , password;
		
		FileWriter aFileWriter = new FileWriter("administrator.txt", true);
		printWriter out = new PrintWriter(aFileWriter);
		
		lastNumber = LeagueManagementRyan.checklastnumber();
		newNumber = lastNumber + 1 ;
		
		while( true )
		{
			adminName = JOptionPane.showInputDialog(null,"Please enter the username you wish to use:","CreateAdmin",1);
			adminName = adminName.trim();
			if ( adminName != null)
			{
				if( adminName.MitchPattern())
				{
					password = JOptionPane.showInputDialog(null,"Please enter the password you wish to use:","CreatePassword",1);
					password = password.trim();
						
					if( password != null)
					{
						if(password.MitchPattern())
						{
							out.println(newNumber +","+adminName+"," + password);
							break;
						}
						else
						{
							JOptionPane.showMessageDialog(null,"The password you entered is invalid!","PasswordError",1);
							 // need some sort of break here to go back to the enter password thing
						}
					}
				else
				{
					JOptionPane.showMessageDialog(null,"The user name you entered is invalid!","UserNameError",1);
					 //need some sort of break here to go back to the enter username thing
				}
				
				}
			}		
		
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
	
	//Mitch, returns an arraylist of leagues that match the logged in admin and forwards it to the dropdown option pane for management
	public static void ArrayList<String> getAdminLeagues() throws IOException 
	{
        Scanner in = new Scanner(leagues);
		int i = 0;
		Arraylist<String> tableDropDown = new ArrayList<String>();
        while(in.hasNextLine())
        {   
            String data = in.nextLine();
            if(data.contains(","))
            { 
                int leagueAdminNum=(data.substring((data.lastIndexOf(",")+1)));
				if(usernameID ==  Integer.parseInt(leagueAdminNum))
				{
					String leagueInsert=(data.substring((data.indexOf(",")+1), data.lastIndexOf(","))); 
					tableDropDown.add(leagueInsert);
					i++;
				}
            }
        }   
		in.close;
		editleague(tableDropDown);
		return tableDropDown;
	}
	
	//Mitch, returns an arraylist of league IDs that match the logged in admin
	public static ArrayList<int> getAdminLeagueIDs() throws IOException 
	{
		ArrayList<int> leagueIDs = new Arraylist<int>();
        Scanner in = new Scanner(leagues);
        while(in.hasNextLine())
        {   
            String data = in.nextLine();
            if(data.contains(","))
            { 
				if(usernameID == (leagueAdminNum))
				{
					int leagueAdminNum=Integer.parseInt(data.substring(1));
					leagueIDs.add(leagueAdminNum);
				}
            }
        }   
		in.close;
		return leagueIDs;
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
