/**
 * LeagueManagement
 */
import javax.swing.*;

import java.awt.Font;
import java.io.*;
import java.util.*;
public class LeagueManagement
{
    static StringBuilder username; //global logged in current user name
    static int usernameID; //global logged in current user ID
    static File admin; //global admin file
    static File leagues; //global league file
    public static ArrayList<ArrayList<String>>  teams;
    public static ArrayList<ArrayList<Integer>> fixtures;   
    public static ArrayList<ArrayList<Integer>> results;
    public static int [][] leaderBoard;

    /**
     * The main creates the 2 necessary files for the program to operate. 
     * It also calls the menuStart method which initiates the program.
     */
    public static void main(String[] args) throws IOException
    {
        admin = new File("administrators.txt");
        leagues = new File("leagues.txt");
        menuStart();
    }

    /**
     * menuStart method runs the main menu for the application
     * and allows all other methods in the program to be accessed.
     * The entirety of the program runs on a JOptionPane interface.
     */
    public static void menuStart() throws IOException
    {
        boolean loggedIn = false;
        String options[] = {"Log in", "Create New Admin", "Quit"};
        String tableOptions[] = {"Create League", "Manage Existing League", "Delete Account", "Log Out"};
        String leagueManageOptions[] = {"View Leaderboard","View Teams","Edit Results", "Delete this League","Return to menu"};
        int choice = -1;
        int choice2 = -1;
        int choice3 = -1;
        String selectedLeague = ""; //League currently selected at league management
        String loggedInUser = "You are currently not logged in:\n\n";
        String upperCaseUser = "";
        username = new StringBuilder("");
        checkSetup(); //Creates admin and leagues files
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
                        upperCaseUser = username.substring(0, 1).toUpperCase() + username.substring(1);
                    loggedInUser = "Logged in: " + upperCaseUser + "\n\n";
                    usernameID = findAdminIdentifierNumber(username.toString());
                }
                else if(choice == 1)
                {
                    createAdmin();
                }
                else if(choice == JOptionPane.CLOSED_OPTION)
                {
                    System.exit(1);
                }
            }
            else
            {
                choice2 = JOptionPane.showOptionDialog(null, loggedInUser + "Select a Menu:", "League Manager",JOptionPane.YES_NO_OPTION, 
                    1, null, tableOptions, options[0]);
                if(choice2 == 0) //Create new league
                {
                    createLeague();
                    choice2 = -1;
                }
                else if(choice2 == 1) //Manage existing league
                {
                    selectedLeague = editLeague();
                    while(true)
                    {
                        if(selectedLeague == null)
                        {
                            choice2 = -1;
                            break;
                        }
                        else
                        {
                            String temp = "League Manager for: "+ selectedLeague;
                            choice3 = JOptionPane.showOptionDialog(null, temp + "\nSelect a league option:","League Manager",JOptionPane.YES_NO_OPTION, 
                                1, null, leagueManageOptions, leagueManageOptions[0]);
                        }
                        if(choice3 == 0)
                        {
                            generateLeagueTable(selectedLeague);
                        }
                        else if(choice3 == 1)
                        {
                            displayTeams(selectedLeague);
                        
                        }
                        else if(choice3 == 2)
                        {
                            int leagueNum = getLeagueIDFromName(selectedLeague);
                            editResults(leagueNum);
                        }
                        else if(choice3 == 3)
                        {
                            if(JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)
                            {
                                deleteSelectedLeague(selectedLeague);
                                choice2 = 0;
                                break;
                            }
                        }
                        else if(choice3 == 4)
                        {
                            choice2 = 1;
                            break;
                        }
                    }
                }
                else if(choice2 == 2) //Delete Admin
                {
                    if(JOptionPane.showConfirmDialog(null, "Are you sure?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        deleteAdmin(username.toString());
                        username = new StringBuilder("");
                        loggedIn = false;
                        loggedInUser = "You are currently not logged in:\n\n";
                        choice2 = 0;
                    }
                }
                else if (choice2 == 3) //Log out
                {
                    username = new StringBuilder("");
                    loggedIn = false;
                    loggedInUser = "You are currently not logged in:\n\n";
                    choice2 = 0;    
                }
                else if(choice2 == JOptionPane.CLOSED_OPTION)
                {
                    System.exit(1);
                }
            }
        }while(choice != 2 && choice2 !=4);
    }

    /**
     * Log in sequence when log in is pressed in main menu
     * Asks for admin name and gives the user three attempts
     * to enter the admins password.
     * @param adminFile Admin file passed to check if provided admin exists
     * @param user      Used to set global Stringbuilder object to the logged in users name
     * @return          Boolean to see if log in was successful 
     */
    public static boolean logInSequence(File adminFile, StringBuilder user) throws IOException
    {
        boolean loggedIn = false;
        String inputName = "";
        String inputPassword = "";
        inputName = JOptionPane.showInputDialog(null, "Please enter an admin name", "Log In", 3);
        if(inputName != null)
        {
            if(stringCheck(inputName))
            {
                if(doesInputExist(admin, inputName, false))
                {
                    String temp[];
                    int attempts = 0;
                    int index = findAdminIdentifierNumber(inputName);
                    temp = stringAtIndexNumber(admin, index).split(",");
                    while(attempts < 3)
                    {
                        String upperCaseUser = temp[1];
                        upperCaseUser = upperCaseUser.substring(0, 1).toUpperCase() + upperCaseUser.substring(1);
                        inputPassword = JOptionPane.showInputDialog(null, "Please enter password for " + upperCaseUser, "Enter Password", 3);
                        if(inputPassword != null)
                        {
                            if(passwordCheck(inputPassword) && inputPassword.equals(temp[2]))
                            {
                                JOptionPane.showMessageDialog(null, "Succesfully logged in!", "Log In Successful", 1);
                                user.append(temp[1]);
                                loggedIn = true;
                                break;
                            }
                            else if(attempts < 3)
                            {
                                if(attempts == 2)
                                {
                                    attempts++;
                                    JOptionPane.showMessageDialog(null, "No attempts left!", "Error", 2);
                                    System.exit(1);
                                }
                                else
                                {    
                                    attempts++;
                                    JOptionPane.showMessageDialog(null, "Incorrect password\n" + (3 - attempts) 
                                                            + " attempts left", "Wrong Password", 2);
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
            else
                JOptionPane.showMessageDialog(null, "Incorrect format for admin");
        }

        return loggedIn;
    }

    /**
     * Checks to see whether there is the administartor and leagues file in the file directory,
     * if not, then they are created
     */
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

    /**
     * Returns a boolean to see if the input provided exists in the passed in file.
     * Can also check for case sensitive inputs
     * @param filename      The file for the method to search through
     * @param input         The input for the method to search for
     * @param caseSensitive Boolean to say whether the search should be case sensitive or not
     * @return              boolean based on if the input exists or not 
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
                    String temp[] = aLineFromFile.split(",");
                    if(input.equals(temp[1]))
                    {
                        in.close();
                        return true;
                    }
                }
                else
                {
                    String aLineFromFile = in.nextLine().toLowerCase();
                    String temp[] = aLineFromFile.split(",");
                    if(input.equals(temp[1].toLowerCase()))
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

    /**
     * Returns the string at a line in the file provided based on the index number
     * which is the first number on each line.
     * @param file        The file to search
     * @param indexNumber The first number on a line to search for
     * @return            Line matching the indexNumber
     */
    public static String stringAtIndexNumber(File file, int indexNumber) throws IOException
    {
        String result = "";
        if(file.exists())
        {
            Scanner in = new Scanner(file);
            while(in.hasNext())
            {
                String temp = in.nextLine();
                String index = temp.substring(0, temp.indexOf(","));
                if(index.equals(String.valueOf(indexNumber)))
                {
                    result = temp;
                    break;
                }       
            }
            in.close();
        }
        return result;
    }

    /**
     * Return an int which corresponds to the admin name provided.
     * Searches the admin file for the provided admin name and if found returns
     * the admins ID number.
     * @param adminName The admin name to search for
     * @return          The ID of the provided admin
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

    /** 
     * Returns the last admin ID in the administrators.txt file.
     * Goes through the file until the last line is reached and returns
     * the ID found at the last line
     * @return ID of the last admin in the admin file
     */
    public static int findLastAdminNumber() throws IOException
    {
        int idNumber = 0;
        Scanner in = new Scanner(admin);
        String lastLine = "";
        if(admin.length() == 0)
        {    
            in.close();
            return idNumber;
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

    /**
     * Display teams takes in the name of a league, it then finds the league number associated with the name of the leauge. Then
     * looks at the participants text file related to the league and shows all the participants in the file.
     * @param selectedLeague  The name of the league selected is passed in to get the names of the participants in the league.
     */
    public static void displayTeams(String selectedLeague) throws IOException //this method needs some work to make it more general
    {
        int i = 1;
        String teams="";
        int numOfLeague = getLeagueIDFromName(selectedLeague);
        FileReader aFileReader = new FileReader(numOfLeague+"_participants.txt");
        Scanner in = new Scanner(aFileReader);
        while(in.hasNext())
        {
            String aLineFromFile = in.nextLine();
            String[] temp =  aLineFromFile.split(",");
            teams += "Team "+i+": "+temp[1]+"\n";
            i++;
        }
        JOptionPane.showMessageDialog(null,teams,"Teams in league",1); 
        in.close();
        aFileReader.close();
    }

    /**
     *	Create Admin asks the user to input a username and password , it checks both to see if they fit the pattern assigned to each. 
     *	It also checks if the username is already being used and displays an error if needed. If the username and password fit the 
     *	criteria they are stored to the admin text file.
     */
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

    /** Returns a boolean if a string fits pattern.
     * @param String input
     * @return boolean
     */
    public static boolean stringCheck(String input) 
    {
        String pattern = "[a-z A-Z]{1,20}"; 
        return(input.matches(pattern));
    }

    /** Returns a boolean if integer fits pattern.
     * @param int input
     * @return boolean
     */
    public static boolean integerCheck(int input) 
    {
        String resultInt = ""+input;
        String pattern = "[0-9]{1,3}"; 
        return(resultInt.matches(pattern));
    }

    /** Returns a boolean if integer(passed as string) fits pattern.
     * @param String input
     * @return boolean
     */
    public static boolean integerCheck(String input) 
    {
        String pattern = "[0-9]{1,3}"; 
        return(input.matches(pattern));
    }

    /** Returns a boolean if a string (of password order) fits pattern.
     * @param String input
     * @return boolean
     */
    public static boolean passwordCheck(String input) 
    {
        String resultPassword = ""+input;
        String pattern = "[A-Za-z0-9]{5,30}"; 
        return(resultPassword.matches(pattern));
    }

    /** getAdminLeagues returns an arraylist of leagues that match the logged in admin and forwards 
     *it to the dropdown option pane for league management.
     * @return ArrayList<String> tableDropDown 
     */
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

    /**
     * Displays a JOption pane asking the user to enter the name of a participant in their league.
     * The names entered are placed in an array list which is then printed to a file
     * with the league number followed by '_participants.txt'.
     * @param maxTeamNum The maximum number of teams allowed in the league
     */
    public static void createLeagueParticipants(int maxTeamNum) throws IOException
    {
        ArrayList<String> participants = new ArrayList<String>();
        String input = "";
        int teamCount = 0;
        while(teamCount < maxTeamNum)
        {
            input = JOptionPane.showInputDialog(null, "Input participant");
            if(input != null)
                if(stringCheck(input))
                {
                    participants.add(input);
                    teamCount++;
                }
                else
                    JOptionPane.showMessageDialog(null, "Incorrect format of participant");
            else
                break;
        }
        int fileIDNumber = findLeagueIdentifierNumber();
        File participantFile = new File(fileIDNumber + "_participants.txt");
        PrintWriter out = new PrintWriter(participantFile);
        for(int i = 0; i < participants.size(); i++)
            out.println((i + 1) + "," + participants.get(i));
        out.close();
    }	

    /** This method is used to provide results to each corresponding game
     *  A results is opened and the user is prompted to select from a match from the dropdown menu
     *  Once entered the result is saved the file 
     * @param leagueNumber Provides method with the relevant league number  
     */
    public static void editResults(int leagueNumber) throws IOException
    {
        ArrayList<String> results = new ArrayList<String>();
        int inputInt = 0, matchIndex = 0;
        int homeScore = 0, awayScore = 0;
        File resultsFile = new File(leagueNumber + "_results.txt");
        ArrayList<String> allMatches = getMatches(leagueNumber);
        String[] choices = allMatches.toArray(new String[allMatches.size()]);
        while(true)
        {
            String matchChoice = (String)(JOptionPane.showInputDialog(null, "Choose a Fixture:",
                        "League Management",JOptionPane.QUESTION_MESSAGE, null, choices,choices[0]));
            if(matchChoice != null)
            {
                matchIndex = allMatches.indexOf(matchChoice);
                while(true)
                {
                    String input = JOptionPane.showInputDialog(null, "Input score for home team in match: " + matchChoice);
                    if(!(input == null))
                    {
                        if(integerCheck(input))
                        {
                            inputInt = Integer.parseInt(input);
                            homeScore = inputInt;
                            break;
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Incorrect format for score");
                    }
                }
                while(true)
                {
                    String input = JOptionPane.showInputDialog(null, "Input score for away team in match: " + matchChoice);
                    if(!(input == null))
                    {
                        if(integerCheck(input))
                        {
                            inputInt = Integer.parseInt(input);
                            awayScore = inputInt;
                            break;
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Incorrect format for score");   
                    }
                }
                Scanner in = new Scanner(resultsFile);
                while(in.hasNext()) //Copy results file
                    results.add(in.nextLine());
                in.close();
                results.remove(matchIndex);
                results.add(matchIndex, (matchIndex + 1)+ "," + homeScore + "," + awayScore);
                PrintWriter out = new PrintWriter(resultsFile);
                for(int i = 0; i < results.size(); i++)
                    out.println(results.get(i));
                out.close();
                results.clear(); // Empty the Arraylist before looping
            }
            else
                break;
        } 
    }

    /**
     * Deletes the admin with the provided name. This includes first deleting the teams, results
     * and fixtures associated with the admin. Then removes the leagues owned by that admin from 
     * the leagues file and finally removes the admin name from the admin file.
     * @param adminToDelete The name of the admin to delete
     */
    public static void deleteAdmin(String adminToDelete) throws IOException
    {
        if(doesInputExist(admin, adminToDelete, false)) //Check if the admin exists in the admin file
        {
            deleteLeagueTeamsAndResults(adminToDelete); //First delete teams and results
            deleteAdminLeagues(adminToDelete); //Second delete the league
            ArrayList<String> adminArray = new ArrayList<String>();
            Scanner in = new Scanner(admin); //Delete admin
            while(in.hasNext())
            {
                String data = in.nextLine();
                if(!data.toLowerCase().contains(adminToDelete.toLowerCase()))
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

    /**
     * Returns an array list of Integers which correspond to the league IDs of the leagues
     * owned by the provided admin.
     * @param adminName Name of the admin whose owned league IDs to be returned
     * @return          ArrayList of Integers containing league IDs of the admin
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

    /**
     * Deletes the fixtures, participants nad results files 
     * associated with a the admin name provided. 
     * @param adminName Name of the admin whose files should be deleted
     */
    public static void deleteLeagueTeamsAndResults(String adminName) throws IOException
    {
        String participant = "_participants.txt";
        String results = "_results.txt";
        String fixture = "_fixtures.txt";   
        ArrayList<Integer> leagueIDNumbers = getAdminLeagueIDs(adminName);
        for(int i = 0; i < leagueIDNumbers.size(); i++)
        {
            String tempParticipant = leagueIDNumbers.get(i) + participant;
            File participantFile = new File(tempParticipant);
            if(participantFile.exists())
            {   
                participantFile.delete();
            }
            String tempFixture = leagueIDNumbers.get(i) + fixture;
            File fixtureFile = new File(tempFixture);
            if(fixtureFile.exists())
            {
                fixtureFile.delete();

            }
            String tempResults = leagueIDNumbers.get(i) + results;
            File resultsFile = new File(tempResults);  
            if(resultsFile.exists())
            {
                resultsFile.delete();
            }
        }
    }

    /** editLeague does a check when called to see if a league belonging to the admin exists
     * then it reurns a selected league to edit as a string from a JOptionPane drop down menu.
     * @return String input
     */
    public static String editLeague() throws IOException //identify league
    {   
        ArrayList<String> tableDropDown = getAdminLeagues();
        if(tableDropDown.size()==0)
        {
            JOptionPane.showMessageDialog(null,"No leagues associated with this account!\nPlease create a league!"
            ,"Manage Leagues",1);
            return null;
        }
        else
        {
            String[] choices = new String[tableDropDown.size()];
            choices = tableDropDown.toArray(choices);
            String input =(String)(JOptionPane.showInputDialog(null, "Choose a league:",
                        "League Management",JOptionPane.QUESTION_MESSAGE, null, choices,choices[0]));
            return input;
        }
    }

    /**
     * Creates the fixtures as part of the league creation process 
     * Using a number provided by the user, fixtures including multiple rounds are generated
     * @param maxNum    Provides the method with the number of teams 
     * @return          Int i
     */
    public static int generateFixtures(int maxNum) throws IOException
    {
        int numOfTeams = 0, totalRounds, numOfMatchesPerRound;
        int roundNum, matchNumber, homeTeamNum, awayTeamNum, even, odd;
        File file = createFixtureFile();
        PrintWriter out = new PrintWriter(file);
        String [][] fixtures;
        String [][] revisedFixtures;
        String []   elements;
        String fixtureAsText;

        //if teams are odd, add one to number of teams 
        numOfTeams = maxNum;

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
                fixtures[roundNum][matchNumber]  = (homeTeamNum + 1) + "," + (awayTeamNum + 1);
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
        int i = 1;
        for (roundNum = 0; roundNum < totalRounds; roundNum++) 
        {  
            for (matchNumber = 0; matchNumber < numOfMatchesPerRound; matchNumber++){ 
                out.println((i) + "," + fixtures[roundNum][matchNumber]);
                i++;
            }
        }             
        out.close();
        return i;
    }

    /**
     * Creates the fixture file. File structure is leagueNumber + "_fixtures.txt"
     * @return          Returns file 
     */
    public static File createFixtureFile() throws IOException
    {

        int leagueNum = findLeagueIdentifierNumber();
        File file = new File(leagueNum + "_fixtures.txt");
        return file;
    }

    /** This method deletes the corresponding fixture file to the selected league number
     * Uses ArrayLists to check what needs to be kept and what needs to be deleted
     * @param LeagueNum     Provides the method with the relevant league number 
     */
    public static void deleteFixture(String leagueNum) throws IOException{
        ArrayList<Integer> deleteLeagueNumbers = new ArrayList<Integer>();
        ArrayList<String> leaguesToKeep = new ArrayList<String>();
        Scanner in = new Scanner(leagueNum +"_" + "fixtures.");
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
                if(leaguesToKeep.get(j).startsWith((deleteLeagueNumbers.get(i).toString())))
                    leaguesToKeep.remove(j);
            }
        }
        PrintWriter out = new PrintWriter(leagues);
        for(int i = 0; i < leaguesToKeep.size(); i++)
            out.println(leaguesToKeep.get(i));
        out.close();
    } 

    /**
     * Removes the leagues owned by provided admin name from the leagues.txt file.
     * If more than one league is owned then it deltes all of them.
     * @param adminName Name of the admin whose leagues will be delted
     */
    public static void deleteAdminLeagues(String adminName) throws IOException
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

    /** This method checks the input for fixture generatation and ensures it is valid.
     * @param windowMeassage 
     * @param windowTitle 
     * @return Returns the input to method generateFixture
     */
    public static String getnumOfTeams(String windowMessage, String windowTitle)
    {
        boolean validInput = false;    
        int numberOfnumOfTeams;
        String input = "";
        String errorMessage = "Input invalid.\n\nClick OK to retry."; 
        while (!validInput)
        {
            input = JOptionPane.showInputDialog(null, windowMessage, windowTitle, 3);
            if (input == null){
                validInput = true;
            }
            else if ((validInput = integerCheck(input)) == false) 
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

    /**
     * Returns the amount of participants in a specific league
     * @param leagueNum league ID num to be searched
     * @return          Number of participants in the league
     */	
    public static int getAmountOfParticipants(int leagueNum) throws IOException
    {
        File aFile = new File(leagueNum + "_participants.txt");
        Scanner in = new Scanner(aFile);
        String data = "";
        while(in.hasNext())
            data = in.nextLine();
        in.close();
        return Integer.parseInt(data.substring(0,1));
    }

    public static ArrayList<String> getMatches(int leagueID) throws IOException
    {
        ArrayList<String> matches = new ArrayList<String>();
        ArrayList<String> participants = new ArrayList<String>();
        File fixtureFile = new File(leagueID + "_fixtures.txt");
        File participantFile = new File(leagueID + "_participants.txt");
        Scanner inPart = new Scanner(participantFile);
        Scanner inFix = new Scanner(fixtureFile);
        while(inPart.hasNext())
        {
            String temp = inPart.nextLine();
            temp = temp.substring(temp.indexOf(',') + 1);
            participants.add(temp);
        }
        while(inFix.hasNext())
        {
            String temp = inFix.nextLine();
            String tempArray[] = temp.split(",");
            int team1 = Integer.parseInt(tempArray[1]);
            int team2 = Integer.parseInt(tempArray[2]);
            matches.add(participants.get(team1 - 1) + " v " + participants.get(team2 - 1));
        }
        inFix.close();
        inPart.close();
        return matches;
    }

    /** generateLeagueTable method calls all of the subsequent methods required for
     *the creation of the league table. It has an initial check to see if all the files
     *exist by running the readFilesIntoArrayLists method.
     */
    public static void generateLeagueTable(String selectedLeague) throws IOException
    {
        {
            boolean readFile; 
            readFile = readFilesIntoArrayLists(selectedLeague);
            if (!(readFile))
                System.out.println("One or more files do not exist.");
            else
            {
                createEmptyLeaderBoard();
                processResults();
                orderLeaderBoard();
                displayLeaderboard(selectedLeague);
            }
        }
    } 

    /** readFilesIntoArrayLists method reads the data inside the files
     * of participants,fixtures and results to allow them to be used in the
     * generation of a league table. It saves the need for unnecessary disk access
     * for files and easier comparison of data.
     */
    public static boolean readFilesIntoArrayLists(String selectedLeague) throws IOException
    {
        int leagueID = getLeagueIDFromName(selectedLeague);
        String filePart = leagueID+"_participants.txt";
        String fileFixt = leagueID+"_fixtures.txt";
        String fileResu = leagueID+"_results.txt";

        String filesList[];
        File inputFile1 = new File(filePart);
        File inputFile2 = new File(fileFixt);
        File inputFile3 = new File(fileResu);

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

    /** createEmptyLeaderBoard method receives the dimensions
     * needed to create the blank leaderboard in the form of a
     * 2D array.
     */
    public static void createEmptyLeaderBoard()
    {  
        int rows = teams.get(0).size();
        int columns = 14;  
        leaderBoard = new int[rows][columns];
        for (int i = 0; i < leaderBoard.length; i++)
        {
            leaderBoard[i][0] = Integer.parseInt(teams.get(0).get(i));
        }
    }     

    /** processResults method retrieves all the results from the arraylist
     * and assigns the corresponding values to several integers that are used 
     * in the creation of the filled in leaderBoard. There are 3 if checks to
     * test home team score against away team score and sends them to the
     * corresponding methods for fixture result.
     */
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

    /** Receives the home team details and carries out the necessary calculations
     * to create the statistics for each column in the leaderBoard.
     * @param int homeTeamNum = Home Team Number
     * @param int wins = Home Team Wins
     * @param int draws = Home Team Draws
     * @param int losses = Home Team Losses
     * @param int homeTeamScore = Home Team Score
     * @param int awayTeamScore = Away Team Score
     * @param int totalPoints = Home Team Total Points
     */
    public static void recordFixtureResultForHomeTeam(int homeTeamNum, int wins, int draws, int losses, 
    int homeTeamScore, int awayTeamScore, int totalPoints)
    {
        leaderBoard[homeTeamNum-1][1]++;                    				// gamesPlayed
        leaderBoard[homeTeamNum-1][2]+= wins;                  				// homeWin
        leaderBoard[homeTeamNum-1][3]+= draws;                  			// homeDraw
        leaderBoard[homeTeamNum-1][4]+= losses;                  			// homeLoss
        leaderBoard[homeTeamNum-1][5]+= homeTeamScore;                		// homeTeamScore
        leaderBoard[homeTeamNum-1][6]+= awayTeamScore;                		// awayTeamScore
        leaderBoard[homeTeamNum-1][12] += (homeTeamScore - awayTeamScore);  // goalDifference
        leaderBoard[homeTeamNum-1][13] += totalPoints;                		// points
    }

    /** Receives the away team details and carries out the necessary calculations
     * to create the statistics for each column in the leaderBoard.
     * @param int awayTeamNum = Away Team Number
     * @param int wins = Away Team Wins
     * @param int draws = Away Team Draws
     * @param int losses = Away Team Losses
     * @param int homeTeamScore = Home Team Score
     * @param int awayTeamScore = Away Team Score
     * @param int totalPoints = Away Team Total Points
     */
    public static void recordFixtureResultForAwayTeam(int awayTeamNum, int wins, int draws, int losses, 
    int homeTeamScore, int awayTeamScore, int totalPoints)
    {
        leaderBoard[awayTeamNum-1][1]++;                    				// gamesPlayed
        leaderBoard[awayTeamNum-1][7]+= wins;                  				// homeWin
        leaderBoard[awayTeamNum-1][8]+= draws;                  			// homeDraw
        leaderBoard[awayTeamNum-1][9]+= losses;                  			// homeLoss
        leaderBoard[awayTeamNum-1][10]+= homeTeamScore;                		// homeTeamScore
        leaderBoard[awayTeamNum-1][11]+= awayTeamScore;                		// awayTeamScore
        leaderBoard[awayTeamNum-1][12] += (awayTeamScore - homeTeamScore);  // goalDifference
        leaderBoard[awayTeamNum-1][13] += totalPoints;                		// points
    }    

    /** This arranges the leaderBoard as long as continues remains true
     * and orders the leaderboard in order of totalPoints.
     */
    public static void orderLeaderBoard()
    {
        int [][] temporaryArray = new int[leaderBoard.length][leaderBoard[0].length];
        boolean continues = false;
        while (!continues) 
        {
            continues = true;
            for (int i = 0; i < leaderBoard.length - 1; i++) 
            {
                if (leaderBoard[i][13] < leaderBoard[i + 1][13])
                {
                    for (int j = 0; j < leaderBoard[i].length; j++) 
                    {
                        temporaryArray[i][j] = leaderBoard[i][j];
                        leaderBoard[i][j] = leaderBoard[i + 1][j];
                        leaderBoard[i + 1][j] = temporaryArray[i][j];
                    }
                    continues = false;
                }
            }
        }
    }         

    /** This displays the filled in leaderboard with the respective
     * details and formats the column widths accordingly taking the longest team name
     * and prints out the details from the 2D leaderBoard array.
     */
    public static void displayLeaderboard(String leagueTitle)
    {
        int aTeamNumber;
        String aTeamName, format;
        String longestTeamName       = teams.get(1).get(0);
        int    longestTeamNameLength = longestTeamName.length();
        for (int i = 1; i < teams.get(1).size(); i++)
        {
            longestTeamName = teams.get(1).get(i);  
            if (longestTeamNameLength < longestTeamName.length())
            longestTeamNameLength = longestTeamName.length();
        }
        format = "%-" + (longestTeamNameLength + 10) + "s";
        
        String leaderBoardOutPut = "\nLeague: "+leagueTitle.toUpperCase()+"\n";
        leaderBoardOutPut += String.format(format,"Team Name");
        leaderBoardOutPut += String.format("%20s%20s%20s%20s%20s%24s%20s%20s%20s%20s%20s%23s%20s\n","GamesPlayed", "HomeWins",  "HomeDraws",  "HomeLosses",  "GoalsScored",  "GoalsConceded", 
        "AwayWins",  "AwayDraws",  "AwayLosses"  ,"GoalsScored" , "GoalsConceded"  , "GoalDifference",   "TotalPoints");
        //leaderBoardOutPut +=("    GamesPlayed  HomeWins  HomeDraws  HomeLosses  GoalsScored  GoalsConceded  "+  
        //    "AwayWins  AwayDraws  AwayLosses  GoalsScored  GoalsConceded   GoalDifference   TotalPoints");
        leaderBoardOutPut += "\n";
        for (int i = 0; i < leaderBoard.length; i++)
        {
            aTeamNumber       = leaderBoard[i][0];
            aTeamName         = teams.get(1).get(aTeamNumber - 1);
            leaderBoardOutPut += String.format(format, aTeamName);
            leaderBoardOutPut += String.format("%15d%21d%20d%20d%20d%22d%21d%21d%21d%20d%20d%20d%20d\n",leaderBoard[i][1], leaderBoard[i][2],  leaderBoard[i][3],  leaderBoard[i][4],  
                leaderBoard[i][5],  leaderBoard[i][6], leaderBoard[i][7],  leaderBoard[i][8],  leaderBoard[i][9]  ,leaderBoard[i][10] , leaderBoard[i][11]  , leaderBoard[i][12],   leaderBoard[i][13]);
        }
        JTextArea ta = new JTextArea(); //Create JTextArea to properly display leaderboard
        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setWrapStyleWord(false);
        ta.setText(leaderBoardOutPut);
        ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JOptionPane.showMessageDialog(null,ta,"LeaderBoard",1); 
    }   

    /**
     * It looks at the leagues text file and goes through each line , it takes the string that is on that line and splits 
     * it at each comma. It then returns the last number at first position on the last line. This is the League Identifier Number.
     *@return identifierNo		The first number on the last line of the file leagues.
     */
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

    /**
     * It writes to the Leagues text file, writing in the last position so it doesn't overwrite any of the other leagues. 
     * It takes the last league number used and adds one to it. 
     * In the file it prints the league number, then the league Name and then the usernames Identifier Number, all of which 
     * are divided by commas.
     */
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
                if(!(doesInputExist(leagues, leagueName, false)))
                {
                    out.println( leagueNo + "," + leagueName + "," + usernameID);
                    out.close();
                    aFileWriter.close();
                    int maxNum = 0;
                    while(true)
                    {
                        String temp = getnumOfTeams("Number of Teams", 
                                "Please enter a number in the range 2 to 99");
                        if(!(temp == null))
                        {
                            maxNum = Integer.parseInt(temp);
                            if(maxNum % 2 == 1)
                            {
                                JOptionPane.showMessageDialog(null, "You cannot use an odd number of teams");
                            }
                            else
                                break;
                        }
                    }
                    int fixtureAmount = generateFixtures(maxNum);
                    createLeagueParticipants(maxNum); //Enter league participants
                    File resultsFile = new File(leagueNo + "_results.txt");
                    out = new PrintWriter(resultsFile);
                    for(int i = 0; i < fixtureAmount - 1; i++) // Fill results with 0s
                        out.println((i+1) + "," + 0 + "," + 0);
                    out.close();

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

    /**
     *	It gets passed the league name , then it looks at the leagues text file. Going through each line it looks for a league name 
     *	that is the same as the one that was passed in. It can only be accessed by the edit league option, so when one is found it returns the 
     *	league number associated with the league name passed in.
     *	@param leagueName			The name of the selected league.
     * 	@return leagueNumber		The league number associated with the league name passed in.
     */
    public static int getLeagueIDFromName(String leagueName) throws IOException
    {
        Scanner in = new Scanner(leagues);
        while(in.hasNext())
        {
            String temp = in.nextLine();
            if(temp.contains(leagueName))
            {
                String tempArray[] = temp.split(",");
                return Integer.parseInt(tempArray[0]);
            }
        }
        in.close();
        return 0;
    }

    /**
     *	Takes in the name of the league that has to be deleted. From the league name it gets the league number associated to it. 
     *	It makes an array list of all the Leagues, then it looks at the league numbers and it deletes the league associated with the 
     *	name of the league that was passed in. It then writes the remaining leagues into the leagues text file.
     *	@param leagueNameToDel	the name of the league that needs to be deleted.
     */
    public static void deleteSelectedLeague(String leagueNameToDel) throws IOException
    {
        int leagueNumToDelete = getLeagueIDFromName(leagueNameToDel);
        ArrayList<String> leaguesToKeep = new ArrayList<String>();
        Scanner in = new Scanner (leagues) ;
        while(in.hasNext())
        {
            String leagueInfo= in.nextLine();
            leaguesToKeep.add(leagueInfo);

        }
        for(int i = 0; i < leaguesToKeep.size(); i++)
        {
            if(leaguesToKeep.get(i).startsWith(Integer.toString(leagueNumToDelete)))
            {
                leaguesToKeep.remove(i);
            }
        }

        PrintWriter out = new PrintWriter(leagues);
        for( int j = 0 ; j<leaguesToKeep.size() ; j++)
        {
            String temp = leaguesToKeep.get(j);
            out.println(temp);
        }
        deleteLeagueTeamsAndResults(leagueNumToDelete);
        out.close();
        in.close();
    }

    /** This follows from deleteSelectedLeague by deleting its
     * respective participant,fixtures, and results files.
     * It uses the files native .delete to remove the entire file.
     * @param int value of the LeagueID 
     */
    public static void deleteLeagueTeamsAndResults(int leagueID) throws IOException
    {
        String participant = "_participants.txt";
        String results = "_results.txt";
        String fixture = "_fixtures.txt";   
        String tempParticipant = leagueID + participant;
        File participantFile = new File(tempParticipant);
        if(participantFile.exists())
        {   
            participantFile.delete();
        }
        String tempFixture = leagueID + fixture;
        File fixtureFile = new File(tempFixture);

        if(fixtureFile.exists())
        {
            fixtureFile.delete();
        }
        String tempResults = leagueID + results;
        File resultsFile = new File(tempResults);  

        if(resultsFile.exists())
        {
            resultsFile.delete();
        }
    }
}
