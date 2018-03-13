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
    public static ArrayList<ArrayList<String>>  teams;
    public static ArrayList<ArrayList<Integer>> fixtures;   
    public static ArrayList<ArrayList<Integer>> results;
    public static int [][] leaderBoard;

    public static void main(String[] args) throws IOException
    {
        boolean loggedIn = false;
        admin = new File("administrators.txt");
        leagues = new File("leagues.txt");
        String options[] = {"Log in", "Create New Admin", "Quit"};
        String tableOptions[] = {"Create League", "Manage Existing League", "Delete Account", "Log Out"};
        int choice = -1;
        int choice2 = -1;
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
            {
                choice2 = JOptionPane.showOptionDialog(null, loggedInUser + "Select a Menu:", "League Manager",JOptionPane.YES_NO_OPTION, 
                    1, null, tableOptions, options[0]);

                if(choice2 == 0) //Create new league
                {
                    createLeague();
                }
                else if(choice2 == 1) //Manage existing league
                {
                    editLeague();
                }
                else if(choice2 == 2) //Delete Admin
                {
                    if(JOptionPane.showConfirmDialog(null, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        deleteAdmin(username.toString());
                        username = new StringBuilder("");
                        loggedIn = false;
                        loggedInUser = "Not logged in:\n\n";
                        choice2 = 0;
                    }
                }
                else if (choice2 == 3) //Log out
                {
                    username = new StringBuilder("");
                    loggedIn = false;
                    loggedInUser = "Not logged in:\n\n";
                    choice2 = 0;    
                }
            }

        }while(choice != 2 && choice2 !=4);
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
        String pattern = "[0-9]{1,3}"; 
        return(resultInt.matches(pattern));
    }

    //Mitch,general password check against pattern 
    public static boolean passwordCheck(String input) 
    {
        String resultPassword = ""+input;
        String pattern = "[A-Za-z0-9]{5,30}"; 
        return(resultPassword.matches(pattern));
    }

    //Mitch, returns an arraylist of leagues that match the logged in admin and forwards it to the dropdown option pane for management
    public static ArrayList<String> getAdminLeagues() throws IOException 
    {
        Scanner in = new Scanner(leagues);
        ArrayList<String> tableDropDown = new ArrayList<String>();
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
        return tableDropDown;
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

    //Rian
    /*
    deleteAdmin - Deletes the admin which equals the provided admin.
    Firstly deletes participant and results files from the league which the admin controls
    secondly deletes the leagues which the admin controls
    lastly deletes name from admin file
     */
    public static void deleteAdmin(String adminToDelete) throws IOException
    {
        if(doesInputExist(admin, adminToDelete, false)) //Check if the admin exists in the admin file
        {
            deleteLeagueTeamsAndResults(adminToDelete); //First delete teams and results
            deleteLeague(adminToDelete); //Second delete the league

            ArrayList<String> adminArray = new ArrayList<String>();
            Scanner in = new Scanner(admin); //Delete admin

            while(in.hasNext())
            {
                String data = in.nextLine();
                if(!data.toLowerCase().contains(adminToDelete))
                    adminArray.add(data);
            }
            in.close();
            PrintWriter out = new PrintWriter(admin);

            for(int i = 0; i < adminArray.size(); i++)
                out.println(adminArray.get(i));
            out.close();
        }
        else
            JOptionPane.showMessageDialog(null, "Cannot delete this admin as it doesn't exist");
    }

    /*
    getAdminLeagueIDs - returns an Integer ArrayList containing the leagueIDs which are managed by the 
    inputted admin. This is done by comparing adminID with the owner ID of the league.
     */
    public static ArrayList<Integer> getAdminLeagueIDs(String adminName) throws IOException 
    {
        ArrayList<Integer> leagueIDs = new ArrayList<Integer>();
        Scanner in = new Scanner(leagues);
        while(in.hasNextLine())
        {   
            String data = in.nextLine();
            if(data.contains(","))
            { 
                int leagueOwnerID = Integer.parseInt(data.substring(data.lastIndexOf(',') + 1));
                if(findAdminIdentifierNumber(adminName) == leagueOwnerID)
                {
                    int leagueAdminNum=Integer.parseInt(data.substring(0, 1));
                    leagueIDs.add(leagueAdminNum);
                }
            }
        }   
        in.close();
        return leagueIDs;
    }

    /*
    deleteLeagueTeamsandResults - deltes the teams and results associated with the leagues that the provided
    admin name owns
     */
    public static void deleteLeagueTeamsAndResults(String adminName) throws IOException
    {
        String participant = "_participants.txt";
        String results = "_results.txt";

        ArrayList<Integer> leagueIDNumbers = getAdminLeagueIDs(adminName);
        for(int i = 0; i < leagueIDNumbers.size(); i++)
        {
            String tempParticipant = leagueIDNumbers.get(i) + participant;
            File participantFile = new File(tempParticipant);
            if(participantFile.exists())
            {
                String tempResults = leagueIDNumbers.get(i) + results;
                File resultsFile = new File(tempResults);
                if(resultsFile.exists())
                {
                    participantFile.delete();
                    resultsFile.delete();
                }
            }
        }
    }

    /*
    Removes the leagues that the provided admin controls from the leagues.txt file
     */
    public static void deleteLeague(String adminName) throws IOException
    {
        ArrayList<String> leaguesToKeep = new ArrayList<String>();
        ArrayList<Integer> deleteLeagueNumbers = getAdminLeagueIDs(adminName);
        Scanner in = new Scanner(leagues);

        while(in.hasNext()) //Fill String array
        {
            String data = in.nextLine();
            leaguesToKeep.add(data);
        }
        in.close();

        for(int i = 0; i < deleteLeagueNumbers.size(); i++) //Remove lines using ArrayList of lines to remove
        {
            for(int j = 0; j < leaguesToKeep.size(); j++)
            {
                if(leaguesToKeep.get(j).startsWith(deleteLeagueNumbers.get(i).toString()))
                    leaguesToKeep.remove(j);
            }
        }
        PrintWriter out = new PrintWriter(leagues);
        for(int i = 0; i < leaguesToKeep.size(); i++)
            out.println(leaguesToKeep.get(i));
        out.close();
    }

    //Mitch,sort leagues for respective admin
    public static void editLeague() throws IOException //identify league
    {   
        ArrayList<String> tableDropDown = getAdminLeagues();
        if(tableDropDown.size()==0)
        {
            JOptionPane.showMessageDialog(null,"No leagues associated with this account!\nPlease create a league!"
            ,"Manage Leagues",1);
        }
        else
        {
            String[] choices = new String[tableDropDown.size()];
            choices = tableDropDown.toArray(choices);
            String input =(String)(JOptionPane.showInputDialog(null, "Choose a league:",
                        "League Management",JOptionPane.QUESTION_MESSAGE, null, choices,choices[0])); 
            //delete league option and edit league option
        }
    }

    //losg
    public static void generateFixtures() throws IOException
    {

        int numOfTeams, totalRounds, numOfMatchesPerRound;
        int roundNum, matchNumber, homeTeamNum, awayTeamNum, even, odd;
        boolean oddnumOfTeams = false;
        PrintWriter out = new PrintWriter("fixtures.txt");
        createFixtureFile();
        String selection;
        String [][] fixtures;
        String [][] revisedFixtures;
        String []   elements;
        String fixtureAsText;
        selection = getnumOfTeams("Team Number Entry", 
            "Please enter a number in the range 2 to 99");

        //***FILE STRUCTURE: FixtureNumber,HomeParticipant#,AwayParticipant#8*** 
        //checks selection
        if(selection != null){

            //if teams are odd, add one to number of teams 
            numOfTeams = Integer.parseInt(selection);
            if(numOfTeams % 2 == 1){

                numOfTeams++;
                oddnumOfTeams = true; 

            }   

            totalRounds = numOfTeams - 1;
            numOfMatchesPerRound = numOfTeams / 2;
            fixtures = new String[totalRounds][numOfMatchesPerRound];  

            //needs to be replaced with a method to check the league number 
            for (roundNum = 0; roundNum < totalRounds; roundNum++) 
            {
                for (matchNumber = 0; matchNumber < numOfMatchesPerRound; matchNumber++) 
                {
                    homeTeamNum = (roundNum + matchNumber) % (numOfTeams - 1);
                    awayTeamNum = (numOfTeams - 1 - matchNumber + roundNum) % (numOfTeams - 1);
                    if (matchNumber == 0) 
                        awayTeamNum = numOfTeams - 1;
                    fixtures[roundNum][matchNumber]  = (homeTeamNum + 1) + "," + (awayTeamNum + 1) + "\n";
                }   
            } 

            revisedFixtures = new String[totalRounds][numOfMatchesPerRound];
            even = 0;
            odd = numOfTeams / 2;   
            for (int i = 0; i < fixtures.length; i++) 
            {
                if (i % 2 == 0)     
                    revisedFixtures[i] = fixtures[even++];
                else                
                    revisedFixtures[i] = fixtures[odd++];
            }
            fixtures = revisedFixtures;

            for (roundNum = 0; roundNum < fixtures.length; roundNum++) 
            {
                if (roundNum % 2 == 1) 
                {
                    fixtureAsText = fixtures[roundNum][0];
                    elements = fixtureAsText.split(",");
                    fixtures[roundNum][0] = elements[1] + "," + elements[0];
                }
            } 

            for (roundNum = 0; roundNum < totalRounds; roundNum++) 
            {  
                for (matchNumber = 0; matchNumber < numOfMatchesPerRound; matchNumber++){ 
                    out.printf((matchNumber + 1) + "," 
                        + fixtures[roundNum][matchNumber] + "\n");
                    out.println("");
                }
            }             
            out.close();
        }
    }

    //losg
    public static void createFixtureFile() throws IOException
    {

        File file = new File(" " + "fixtures" + ".txt");
        PrintWriter out = new PrintWriter(file);
        int increase=1;

        while(file.exists())
        {
            increase++;
            file = new File(increase + "_" + "fixtures" + ".txt");

        } 
    }

    //losg - might be able to replace it with Mitch's checkInput
    public static String getnumOfTeams(String windowMessage, String windowTitle)
    {
        boolean validInput = false;    
        int numberOfnumOfTeams;
        String input = "", pattern = "[0-9]{1,2}";
        String errorMessage = "Input invalid.\n\nClick OK to retry."; 
        while (!validInput)
        {
            input = JOptionPane.showInputDialog(null, windowMessage, windowTitle, 3);
            if (input == null){
                validInput = true;

            }

            else if (!input.matches(pattern)) 
                JOptionPane.showMessageDialog(null, errorMessage, "Error in user input", 2);

            else
            {
                numberOfnumOfTeams = Integer.parseInt(input);

                if (numberOfnumOfTeams < 2)
                    JOptionPane.showMessageDialog(null, errorMessage, "Error in user input", 2);

                else 
                    validInput = true;
            }
        }  
        return input;
    }     

    //Mitch,Generation of League Table
    public static void generateLeagueTable() throws IOException
    {
        {
            boolean readFile; 
            readFile = readFilesIntoArrayLists();
            if (!(readFile))
                System.out.println("One or more files do not exist.");
            else
            {
                createEmptyLeaderBoard();
                processResults();
                orderLeaderBoard();
                displayLeaderboard();
            }
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
        leaderBoard[hTN-1][1]++;                    // gamesPlayed
        leaderBoard[hTN-1][2]+= w;                  // homeWin
        leaderBoard[hTN-1][3]+= d;                  // homeDraw
        leaderBoard[hTN-1][4]+= l;                  // homeLoss
        leaderBoard[hTN-1][5]+= hTS;                // homeTeamScore
        leaderBoard[hTN-1][6]+= aTS;                // awayTeamScore
        leaderBoard[hTN-1][12] += (hTS - aTS);      // goalDifference
        leaderBoard[hTN-1][13] += p;                // points
    }

    public static void recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, 
    int hTS, int aTS, int p)
    {
        leaderBoard[aTN-1][1]++;                    // gamesPlayed
        leaderBoard[aTN-1][7]+= w;                  // awayWin
        leaderBoard[aTN-1][8]+= d;                  // awayDraw
        leaderBoard[aTN-1][9]+= l;                  // awayLoss
        leaderBoard[aTN-1][10]+= aTS;               // awayTeamScore
        leaderBoard[aTN-1][11]+= hTS;               // homeTeamScore
        leaderBoard[aTN-1][12] += (aTS - hTS);      // goalDifference
        leaderBoard[aTN-1][13] += p;                // points  
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
    //Love From Ryan
    public static int findLeagueIdentifierNumber () throws IOException
    {   
        int identifierNo;
        FileReader aFileReader = new FileReader("leagues.txt");
        Scanner in = new Scanner ( aFileReader );
        String aLineFromFile = "";

        while ( in.hasNext())
        {
            aLineFromFile = in.nextLine();
        }

        in.close();
        aFileReader.close();

        if(aLineFromFile.isEmpty())
        {
            identifierNo = 0;
        }
        else
        {
            String aLineFromFileSplit []  = aLineFromFile.split(",") ; 
            String identifier = aLineFromFileSplit[0];
            identifierNo = Integer.parseInt(identifier);
        }
        return identifierNo;
    }

    //Love From Ryan
    public static void createLeague() throws IOException
    {
        int leagueNo = findLeagueIdentifierNumber();
        leagueNo = leagueNo + 1;
        FileWriter aFileWriter = new FileWriter("leagues.txt", true); 
        PrintWriter out = new PrintWriter(aFileWriter);
        String leagueName = JOptionPane.showInputDialog(null,"Please enter the league name you wish to use:","League Name",1);
        if (leagueName != null)
        {
            leagueName = leagueName.trim();
            if(stringCheck(leagueName))
            {
                if(doesInputExist(leagues, leagueName, false))
                {
                    out.println( leagueNo + "," + leagueName + "," + usernameID);
                    out.close();
                    aFileWriter.close();
                }
                else
                { 
                    JOptionPane.showMessageDialog(null,"The League Name you picked already exists!","League Name Error", 1);
                }
            }
            else
            { 
                JOptionPane.showMessageDialog(null,"Enter a name between 1 to 20 and only alphabetical characters","League Name Error", 1);
            }
        }
    }
}
