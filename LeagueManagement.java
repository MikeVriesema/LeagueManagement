import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;

public class LeagueCreate 
{
    public static void main(String[] args) throws IOException 
    {
      
    }

    public static void createTeams()
    {
        ArrayList<String> participants = new ArrayList<String>();
        boolean nextTeam = true;
        while(nextTeam == true)
        {
            String input = JOptionPane.showInputDialog(null, "Enter team name");

            if(input == null)
                nextTeam = false;
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
