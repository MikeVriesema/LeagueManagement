import java.io.*;
import javax.swing.*;
public class project
{
    public static void main (String []args) throws IOException											//ROC
    {
        FileWriter aFileWriter = new FileWriter("users.txt");											//ROC
        PrintWriter out = new PrintWriter(aFileWriter);													//ROC
        out.print("This is a list of all the users that are stored in the program:");					//ROC
        out.println("");																				//ROC
        out.close();																					//ROC
        aFileWriter.close();																			//ROC
        project user = new project();																	//ROC
        user.createUser();																				//ROC
    }


    public static void createUser()  throws IOException													//ROC
    {
        FileWriter createUser = new FileWriter("users.txt", true);										//ROC
        PrintWriter out= new PrintWriter(createUser);													//ROC
        String user = JOptionPane.showInputDialog(null, " Enter the username you want to store : \t", "Login",1); //ROC																	//ROC
        user = user.trim();																				//MV trims leading and trailing whitespace of username
        String pattern = "[a-z A-Z]{1,20}";																//MV pattern for check,possible use of a check method if we find we need to check multiple input strings throughout code
        if(!(user.matches(pattern))) 																	//MV if check against pattern
        {
            System.out.println("Use between 1-20 alphabetical characters only!"); 						//MV error outputted if not matching pattern
        }
        else 																							//MV else run normal procedure code
        {
            System.out.println(user);																	//MV trim test [to be removed at end]
            out.print(user);																			//ROC
            out.close();																				//ROC
            createUser.close();																			//ROC
        }
    }
}
