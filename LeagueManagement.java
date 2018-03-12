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
	Arraylist<String> tableDropDown = new ArrayList<String>();
        while(in.hasNextLine())
        {   
            String data = in.nextLine();
            if(data.contains(","))
            { 
                int leagueAdminNum = Integer.parseInt(data.substring((data.lastIndexOf(",")+1)));
				if(usernameID ==  (leagueAdminNum))
				{
					String leagueInsert=(data.substring((data.indexOf(",")+1), data.lastIndexOf(","))); 
					tableDropDown.add(leagueInsert);
				}
            }
        }   
		in.close();
		editleague(tableDropDown);
		return tableDropDown;
	}
	
	//Mitch, returns an arraylist of league IDs that match the logged in admin
	public static ArrayList<Integer> getAdminLeagueIDs() throws IOException 
	{
	ArrayList<Integer> leagueIDs = new Arraylist<int>();
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
		in.close();
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
    public static void editLeague(ArrayList<String> tableDropDown) throws IOException //identify league
    {
        String input = (String) JOptionPane.showInputDialog(null, "Choose a league to manage:",
        "League Management",JOptionPane.QUESTION_MESSAGE, null, tableDropDown,tableDropDown[1]); 
        in.close();	
    }

	//Mitch,general delete league method 
    public static void deleteLeague() throws IOException //identify league
    {
        
    }
	
	//Mitch,Generation of League Table
	public static void generateLeagueTable()
    {
  	 public static ArrayList<ArrayList<String>>  teams;
 	 public static ArrayList<ArrayList<Integer>> fixtures;	
	 public static ArrayList<ArrayList<Integer>> results;
 	 public static int [][] leaderBoard;
 	 public static void main(String [] args) throws IOException
   {
	boolean readFile; 
    readFile = readFilesIntoArrayLists();
    if (!readFile)
      System.out.println("One or more files do not exist.");
    else
    {
      createEmptyLeaderBoard();
      processResults();
      orderLeaderBoard();
      displayLeaderboard();
    }
  }
  
  public static boolean readFilesIntoArrayLists() throws IOException
  {
    String file1 = "Teams.txt";
    String file2 = "Fixtures.txt";
    String file3 = "Results.txt";
    
    String filesList[];
	File inputFile1 = new File(file1);
	File inputFile2 = new File(file2);
	File inputFile3 = new File(file3);
	
	teams = new ArrayList<ArrayList<String>>();
	teams.add(new ArrayList<String>());
    teams.add(new ArrayList<String>());
  
    fixtures = new ArrayList<ArrayList<Integer>>();
	fixtures.add(new ArrayList<Integer>());
    fixtures.add(new ArrayList<Integer>());
    fixtures.add(new ArrayList<Integer>());
    
    results = new ArrayList<ArrayList<Integer>>();
	results.add(new ArrayList<Integer>());
    results.add(new ArrayList<Integer>());
    results.add(new ArrayList<Integer>());
    
	if (inputFile1.exists() && inputFile2.exists() && inputFile3.exists())
	{
	  Scanner in;
	  in = new Scanner(inputFile1);
	  while(in.hasNext())
	  {
	    filesList = (in.nextLine()).split(",");
	    teams.get(0).add(filesList[0]);  
	    teams.get(1).add(filesList[1]);  
	  } 
	  in.close();
	  in = new Scanner(inputFile2);
	  while(in.hasNext())
	  {
	    filesList = (in.nextLine()).split(",");
	    fixtures.get(0).add(Integer.parseInt(filesList[0]));  
	    fixtures.get(1).add(Integer.parseInt(filesList[1]));  
	    fixtures.get(2).add(Integer.parseInt(filesList[2]));  
	  } 
	  in.close();
	  in = new Scanner(inputFile3);
	  while(in.hasNext())
	  {
	    filesList = (in.nextLine()).split(",");
	    results.get(0).add(Integer.parseInt(filesList[0]));  
	    results.get(1).add(Integer.parseInt(filesList[1]));  
	    results.get(2).add(Integer.parseInt(filesList[2]));  
	  } 
	  in.close();
	  return true;
    }
    else
      return false;
  }
  
  public static void createEmptyLeaderBoard()
  {
	// find out the number of teams/players which will determine 
	// the number of rows  
    int rows = teams.get(0).size();
	int columns = 14;  
	leaderBoard = new int[rows][columns];
	// place team numbers in column 0 of leader board
	for (int i = 0; i < leaderBoard.length; i++)
      leaderBoard[i][0] = Integer.parseInt(teams.get(0).get(i));
  }	  
  
  public static void processResults()
  {
	int fixtureNumber, homeTeamScore, awayTeamScore, homeTeamNumber, awayTeamNumber;
	int position;
	for (int i = 0; i < results.get(0).size(); i++)  
    {
	  fixtureNumber  = results.get(0).get(i);
	  homeTeamScore  = results.get(1).get(i);
	  awayTeamScore  = results.get(2).get(i);
	  position       = fixtures.get(0).indexOf(fixtureNumber);
	  homeTeamNumber = fixtures.get(1).get(position);
	  awayTeamNumber = fixtures.get(2).get(position);
	  if (homeTeamScore == awayTeamScore)
	  {
		recordFixtureResultForHomeTeam(homeTeamNumber,0,1,0,homeTeamScore,awayTeamScore,1);
		recordFixtureResultForAwayTeam(awayTeamNumber,0,1,0,homeTeamScore,awayTeamScore,1);
	  }  
	  else if (homeTeamScore > awayTeamScore)
	  {
		recordFixtureResultForHomeTeam(homeTeamNumber,1,0,0,homeTeamScore,awayTeamScore,3);
		recordFixtureResultForAwayTeam(awayTeamNumber,0,0,1,homeTeamScore,awayTeamScore,0);  
	  }  
	  else
	  {
		recordFixtureResultForHomeTeam(homeTeamNumber,0,0,1,homeTeamScore,awayTeamScore,0);
		recordFixtureResultForAwayTeam(awayTeamNumber,1,0,0,homeTeamScore,awayTeamScore,3);  
	  }    
    }
  }	 
  
  public static void recordFixtureResultForHomeTeam(int hTN, int w, int d, int l, 
                                                       int hTS, int aTS, int p)
  {
	leaderBoard[hTN-1][1]++;        			// gamesPlayed
	leaderBoard[hTN-1][2]+= w;      			// homeWin
	leaderBoard[hTN-1][3]+= d;      			// homeDraw
	leaderBoard[hTN-1][4]+= l;      			// homeLoss
	leaderBoard[hTN-1][5]+= hTS;    			// homeTeamScore
	leaderBoard[hTN-1][6]+= aTS;    			// awayTeamScore
	leaderBoard[hTN-1][12] += (hTS - aTS);    	// goalDifference
	leaderBoard[hTN-1][13] += p;    			// points
  }
 
  public static void recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, 
                                                       int hTS, int aTS, int p)
  {
	leaderBoard[aTN-1][1]++;        			// gamesPlayed
	leaderBoard[aTN-1][7]+= w;      			// awayWin
	leaderBoard[aTN-1][8]+= d;      			// awayDraw
	leaderBoard[aTN-1][9]+= l;      			// awayLoss
	leaderBoard[aTN-1][10]+= aTS;    			// awayTeamScore
	leaderBoard[aTN-1][11]+= hTS;    			// homeTeamScore
	leaderBoard[aTN-1][12] += (aTS - hTS);    	// goalDifference
	leaderBoard[aTN-1][13] += p;    			// points  
  }	
  
  public static void orderLeaderBoard()
  {
	int [][] temp = new int[leaderBoard.length][leaderBoard[0].length];
    boolean finished = false;
    while (!finished) 
    {
      finished = true;
      for (int i = 0; i < leaderBoard.length - 1; i++) 
      {
        if (leaderBoard[i][13] < leaderBoard[i + 1][13])
        {
          for (int j = 0; j < leaderBoard[i].length; j++) 
          {
            temp[i][j]            = leaderBoard[i][j];
            leaderBoard[i][j]     = leaderBoard[i + 1][j];
            leaderBoard[i + 1][j] = temp[i][j];
          }
          finished = false;
        }
      }
    }
  }	  
	  
  public static void displayLeaderboard()
  {
	int aTeamNumber;
	String aTeamName, formatStringTeamName;
	String longestTeamName       = teams.get(1).get(0);
    int    longestTeamNameLength = longestTeamName.length();
    
    for (int i = 1; i < teams.get(1).size(); i++)
    {
	  longestTeamName = teams.get(1).get(i);  
      if (longestTeamNameLength < longestTeamName.length())
        longestTeamNameLength = longestTeamName.length();
    }
    formatStringTeamName = "%-" + (longestTeamNameLength + 2) + "s";
    System.out.printf(formatStringTeamName,"Team Name");
    System.out.println("  GP  HW  HD  HL  GF  GA  AW  AD  AL  GF  GA   GD   TP"); 
   
    for (int i = 0; i < leaderBoard.length; i++)
    {
	  aTeamNumber       = leaderBoard[i][0];
	  aTeamName         = teams.get(1).get(aTeamNumber - 1);       
      System.out.printf(formatStringTeamName, aTeamName);
      System.out.printf("%4d", leaderBoard[i][1]);
      System.out.printf("%4d", leaderBoard[i][2]);
      System.out.printf("%4d", leaderBoard[i][3]);
      System.out.printf("%4d", leaderBoard[i][4]);
      System.out.printf("%4d", leaderBoard[i][5]);
      System.out.printf("%4d", leaderBoard[i][6]);
      System.out.printf("%4d", leaderBoard[i][7]);
	  System.out.printf("%4d", leaderBoard[i][8]);
      System.out.printf("%4d", leaderBoard[i][9]);
      System.out.printf("%4d", leaderBoard[i][10]);
      System.out.printf("%4d", leaderBoard[i][11]);
      System.out.printf("%5d", leaderBoard[i][12]);
      System.out.printf("%5d", leaderBoard[i][13]);
      System.out.println();
    }
  } 
