/**
 * LeagueManagement
 */
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class LeagueManagement
{
    public static void main(String[] args) throws IOException
    {
        boolean loggedIn = false;
        File admin = new File("administrators.txt");
        File leagues = new File("leagues.txt");
        String options[] = {"Log in", "Create new Admin", "Quit"};
        int choice = 0;
        String loggedInUser = "Not logged in\n\n";
        StringBuilder username = new StringBuilder("");

        checkSetup(admin, leagues);

        do
        {
            if(!loggedIn)
            {
                choice = JOptionPane.showOptionDialog(null, loggedInUser + "select an action", "League Manager",JOptionPane.YES_NO_OPTION, 
                                                    1, null, options, options[0]);

                if(choice == 0)
                {
                    loggedIn = logInSequence(admin, username);
                    if(loggedIn)
                        loggedInUser = "Logged in as: " + username + "\n\n";
                }
            }
            else
                choice = JOptionPane.showOptionDialog(null, loggedInUser + "select an action", "League Manager",JOptionPane.YES_NO_OPTION, 
                                                    1, null, options, options[0]);
        }while(choice != 2);
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
}
