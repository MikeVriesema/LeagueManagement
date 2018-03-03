import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;

public class LeagueCreate 
{
    public static void main(String[] args) throws IOException 
    {
        inputTeams();
    }

    public static void inputTeams() throws IOException
    {
        ArrayList<String> participants = new ArrayList<String>();
        boolean nextTeam = true;
        String pattern = "[a-zA-Z\\s]{1,}";
        while(nextTeam == true)
        {
            String input = JOptionPane.showInputDialog(null, "Enter team name");

            if(input == null)
                nextTeam = false;
            else if(!(input.matches(pattern)))
                JOptionPane.showMessageDialog(null, "Incorrect input format");
            else
                participants.add(input);
        }

        FileWriter aFileWriter = new FileWriter("participants.txt");
        PrintWriter out = new PrintWriter(aFileWriter);

        for(int i = 0; i < participants.size(); i++)
            out.println((i + 1) + "," + participants.get(i));
            
        aFileWriter.close();
        out.close();
    }
}
