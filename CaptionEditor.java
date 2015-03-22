import edu.princeton.cs.introcs.In;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;

//TODO
//SOLVE CASE WITH OVERLAPPING SUBTITLE INDICES
//CHECK WHAT HAPPENS WITH SHIFTNUM IF NUMBER IS OUT OF RANGE
//IMPLEMENT SHIFTNUM FOR SINGLE NUMBER
//GUARD AGAINST STUPID INPUTS
//FINISH SHIFT TIME WITH TIME PARAMETERS
//SHIFT TIME OVERLOAD WITH SUBTITLE INDEX PARAMETERS
//IMPLEMENT CONVERTINT?
public class CaptionEditor {
	private In captionFile;
	private String fileString;
	private TreeMap<Integer, timeNode> timeMap;
	private int currNum = 1;

	public CaptionEditor(String filename) {
		fileString = filename;
		captionFile = new In(filename);
		ArrayList<Integer> time = new ArrayList<Integer>();
		timeMap = new TreeMap<Integer, timeNode>();
		while(captionFile.hasNextLine()) {
			String line = captionFile.readLine();
			if (line.contains("-->")) {
				String[] twoTimes = line.split(" --> ");
				String start = twoTimes[0];
				String end = twoTimes[1];
				String[] startIndices = start.split(":|,");
				String[] endIndices = end.split(":|,");
				int[] intStarts = new int[startIndices.length];
				int[] intEnds = new int[endIndices.length];
				for (int i = 0; i < startIndices.length; i += 1) {
					intStarts[i] = Integer.valueOf(startIndices[i]);
				}
				for (int i = 0; i < endIndices.length; i += 1) {
					intEnds[i] = Integer.valueOf(endIndices[i]);
				}
				timeNode tN = new timeNode(intStarts, intEnds);
				timeMap.put(currNum, tN);
				currNum += 1;
			}
		}
		
	}

	// public int convertInt(String time) {
	// 	return 0;
	// }

	public void shiftNum(int start, int end, int change) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileString));
			String line;
			String allFile = "";
			while((line = file.readLine()) != null) {
				try {
					int value = Integer.valueOf(line);
					if (value >= start && value <= end) {
						line = line.replace(line, String.valueOf(value + change));
					}
				}
				catch (NumberFormatException e) {
				}
				allFile += line + '\n';
			}
			FileOutputStream File = new FileOutputStream(fileString);
        	File.write(allFile.getBytes());

		}
		catch (Exception e) {
			System.out.println("Problem reading file.");
		}
	}
	public void shiftTime(String startTime, String endTime, String change) {
		String[] startSections = startTime.split(":|,");
		String[] endSections = endTime.split(":|,");
		String[] changeSections = change.split(":|,");
		int[] startIndices = new int[startSections.length];
		int[] endIndices = new int[endSections.length];
		int[] changeIndices = new int[changeSections.length];
		for (int i = 0; i < startSections.length; i += 1) {
			startIndices[i] = Integer.valueOf(startSections[i]);
		}
		for (int i = 0; i < endSections.length; i += 1) {
			endIndices[i] = Integer.valueOf(endSections[i]);
		}
		for (int i = 0; i < changeSections.length; i += 1) {
			changeIndices[i] = Integer.valueOf(changeSections[i]);
		}
		
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the Caption Editor!");
		System.out.println("Please enter the .srt file that you wish to edit (include .srt extension): ");
		System.out.print("> ");
		String file = in.next();
		CaptionEditor cE = new CaptionEditor(file);
		while (true) {
			System.out.println("Enter an edit command ('help' for list of commands): ");
		System.out.print("> ");
		String command = in.next();
		switch (command) {
			case "help":
				In help = new In("help.txt");
                String helpStr = help.readAll();
                System.out.println(helpStr);
                break;
            case "quit":
            	return;
            case "shiftNum":
            	//SOLVE CASE WITH OVERLAPPING NUMBERS
            	System.out.println("Enter the range of subtitle indices you want to shift in this format (start-end): ");
            	String[] range = in.next().split("-");
            	int start = Integer.valueOf(range[0]);
            	int end = Integer.valueOf(range[1]);
            	System.out.println("Enter the amount you want to shift the indices by: ");
            	int change = Integer.valueOf(in.next());
            	cE.shiftNum(start, end, change);
            	break;
            default:
            	System.out.println("Invalid command.");  
                break;
			}
		}
	}
}

class timeNode<S, E> {
	protected S start;
	protected E end;

	public timeNode(S startTime, E endTime) {
		start = startTime;
		end = endTime;
	}

	public String toString() {
		String tN = Arrays.toString((int[]) start) + " to " + Arrays.toString((int[]) end);
		return tN;
	}
}
// 
// class timeIndexNode<I, T> {
// 	protected I index;
// 	protected T time;

// 	public timeIndexNode(I sectionNumber, T timeSection) {
// 		index = sectionNumber;
// 		time = timeSection;
// 	}
// }
