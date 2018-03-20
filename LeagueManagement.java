public class LeagueManagement
{
 //METHOD NEEDED FOR LUKE, PLEASE REFERENCE IF NEEDS BE CHEERS 
  public static int LeagueNumberFromName ( String leagueName)  throws IOException
{
	FileReader aFileReader = new FileReader("leagues.txt");
		Scanner in = new Scanner (aFileReader) ;
		String leagueN, leagueInfo;
		int leagueNumber=0;
		while(in.hasNext())
		{
			leagueInfo= in.nextLine();
			String leagueInfoArr[] = leagueInfo.split(",");
			leagueN = leagueInfoArr[1];
			
			if (leagueN.equals(leagueName))
			{
				String leagueNum = leagueInfoArr[0] ;
				leagueNumber = Integer.parseInt(leagueNum);
			}
		
			
		}
		return leagueNumber;
}
}
