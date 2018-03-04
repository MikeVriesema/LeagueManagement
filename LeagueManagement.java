import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class LeagueManagement 
{
    public static void main(String[] args) throws IOException
    {
        File admin = new File("administrators.txt");
        File leagues = new File("leagues.txt");
        String options[] = {"Log in", "Create new Admin"};
        int choice;

        checkSetup(admin, leagues);
        
        choice = JOptionPane.showOptionDialog(null, "select an action", "League Manager",JOptionPane.YES_NO_OPTION, 1, null, options, options[0]);

        if(choice == 0)
        {
            String inputName = "";
            String inputPassword = "";

            inputName = JOptionPane.showInputDialog(null, "Please enter admin name");
            if(inputName != null)
            {
                if(doesInputExist(admin, inputName))
                {
                    int index = findIdentifierNumber(admin, inputName);
                    String temp[];
                    inputPassword = JOptionPane.showInputDialog(null, "Please enter password for " + inputName);
                    temp = stringAtLineNumber(admin, index).split(",");
                    if(inputPassword.equals(temp[2]))
                        JOptionPane.showMessageDialog(null, "Succesfully logged in!");
                }
            }
        }
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
    public static boolean doesInputExist(File filename, String input) throws IOException
    {
        if(filename.exists())
        {
            Scanner in = new Scanner(filename);
            while(in.hasNext())
            {
                String aLineFromFile = in.nextLine();
                if(aLineFromFile.contains(input))
                    return true;
            }

            return false;
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

                if(temp[1].equals(adminName))
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
