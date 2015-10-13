import java.io.*;
import java.util.*;

public class Assig3B
{
	public static void main(String[] args)
	{
		new Assig3B();
	}
	
	public Assig3B()
	{
        Scanner inScan = new Scanner(System.in);
		Scanner fReader;
		File fName;
        String fString = "", phrase = "";
       
       	// Make sure the file name is valid
        while (true)
        {
           try
           {
               System.out.println("Please enter grid filename:");
               fString = inScan.nextLine();
               fName = new File(fString);
               fReader = new Scanner(fName);
              
               break;
           }
           catch (IOException e)
           {
               System.out.println("Problem " + e);
           }
        }

		// Parse input file to create 2-d grid of characters
		String [] dims = (fReader.nextLine()).split(" ");
		int rows = Integer.parseInt(dims[0]);
		int cols = Integer.parseInt(dims[1]);
		
		char [][] theBoard = new char[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			String rowString = fReader.nextLine();
			for (int j = 0; j < rowString.length(); j++)
			{
				theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
			}
		}

		// Show user the grid
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(theBoard[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		System.out.println("Please enter phrase (sep. by single spaces):");
        phrase = (inScan.nextLine()).toLowerCase();
		while (!(phrase.equals("")))
		{
			System.out.println("Looking for: " + phrase);
			System.out.println("containing " + phrase.split(" ").length + " words");
			// This will be used to store the starting and ending coordinates of each word
			ArrayList<Integer> coords = new ArrayList<Integer>();
			
			// Search for the word.  Note the nested for loops here.  This allows us to
			// start the search at any of the locations in the board.  The search itself
			// is recursive (see findWord method for details).  Note also the boolean
			// which allows us to exit the loop before all of the positions have been
			// tried -- as soon as one solution has been found we can stop looking.
			boolean found = false;
			for (int r = 0; (r < rows && !found); r++)
			{
				for (int c = 0; (c < cols && !found); c++)
				{
					// Start search for each position at index 0 of the phrase
					found = findPhrase(r, c, phrase, 0, theBoard, coords);
				}
			}

			if (found)
			{
				System.out.println("The phrase: " + phrase);
				System.out.println("was found:");
				String[] words = phrase.split(" ");
				for (int i=0; i<words.length; i++)
				{
					System.out.print(words[i] + ": (");
					System.out.print(coords.get(4*i) + "," + coords.get(4*i+1) + ") to (");
					System.out.println(coords.get(4*i+2) + "," + coords.get(4*i+3) + ")");
				}
				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						System.out.print(theBoard[i][j] + " ");
						theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
					}
					System.out.println();
				}
			}
			else
			{
				System.out.println("The phrase: " + phrase);
				System.out.println("was not found");
			}
			
			System.out.println();
			System.out.println("Please enter phrase (sep. by single spaces):");
        	phrase = (inScan.nextLine()).toLowerCase();
		}
			
	}

	// Recursive method to search for the phrase.  Return true if found and false
	// otherwise. 
	public boolean findPhrase(int r, int c, String phrase, int loc, char [][] bo, ArrayList<Integer> coords)
	{
		// Check boundary conditions
		if (r >= bo.length || r < 0 || c >= bo[0].length || c < 0)
			return false;
		else if (bo[r][c] != phrase.charAt(loc))  // char does not match
			return false;
		
		else  	// current character matches
		{
			bo[r][c] = Character.toUpperCase(bo[r][c]);  // Change it to
				// upper case.  This serves two purposes:
				// 1) It will no longer match a lower case char, so it will
				//    prevent the same letter from being used twice
				// 2) It will show the word on the board when displayed

			// If the character is the first in the phrase or is preceded by a space, save its row and column as
			// the starting coordinates of the word.
			if (loc == 0 || phrase.charAt(loc-1) == ' ')
			{
				coords.add(r);
				coords.add(c);
			}

			// If the character is the last in the phrase or is followed by a space, save its row and column as the
			// ending coordinates of the word. This is done separately from adding starting coordinates in order to
			// allow for one-letter words (which have identical starting and ending coordinates).
			if (loc == phrase.length()-1 || phrase.charAt(loc+1) == ' ')
			{
				coords.add(r);
				coords.add(c);
			}			
				
			boolean answer = false;
			if (loc == phrase.length()-1)		// base case - phrase found and we
				answer = true;				// are done!
			
			else 
			{
				// If the next character is a space, you know that you have reached
				// a word boundary. You need to increment loc so that you end up searching
				// for the character after the space, not for the space itself.
				if (phrase.charAt(loc + 1) == ' ')
					loc++;
				
				answer = findPhrase(r, c+1, phrase, loc+1, bo, coords);  // Right
				if (!answer)
						answer = findPhrase(r+1, c, phrase, loc+1, bo, coords);  // Down
				if (!answer)
						answer = findPhrase(r, c-1, phrase, loc+1, bo, coords);  // Left
				if (!answer)
						answer = findPhrase(r-1, c, phrase, loc+1, bo, coords);  // Up
							
				if (!answer)
				{
					// If loc got incremented earlier because a word boundary had been
					// reached, loc must be decremented now to get back to the letter
					// before the word boundary.
					if (phrase.charAt(loc) == ' ')
						loc--;					
					
					// Remove the coordinates from coords that got added earlier			
					if (loc == 0 || phrase.charAt(loc-1) == ' ')
					{
						coords.remove(coords.size()-1);
						coords.remove(coords.size()-1);
					}
					
					if (loc == phrase.length()-1 || phrase.charAt(loc+1) == ' ')
					{
						coords.remove(coords.size()-1);
						coords.remove(coords.size()-1);
					}
					
					// Make letter lowercase again
					bo[r][c] = Character.toLowerCase(bo[r][c]);
				}
			} // end else	
			return answer;
		} // end else
	} // end findPhrase
}