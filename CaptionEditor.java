import edu.princeton.cs.introcs.In;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.JOptionPane; 

//TODO
//SOLVE CASE WITH OVERLAPPING SUBTITLE INDICES
// DONE CHECK WHAT HAPPENS WITH SHIFTNUM IF NUMBER IS OUT OF RANGE
//GUARD AGAINST STUPID INPUTS
// DONE FINISH SHIFT TIME WITH TIME PARAMETERS
// DONE SHIFT TIME OVERLOAD WITH SUBTITLE INDEX PARAMETERS
// DONE IMPLEMENT CONVERTINT
// DONE ADD ABILITY TO AUTOMATICALLY GENERAT SUBTITLE INDICES
public class CaptionEditor {
	private In captionFile;
	private String fileString;
	private TreeMap<Integer, TimeNode> timeMap;
	private int currNum = 1;

	public CaptionEditor(String filename) {
		fileString = filename;
		captionFile = new In(filename);
		ArrayList<Integer> time = new ArrayList<Integer>();
		timeMap = new TreeMap<Integer, TimeNode>();
		while(captionFile.hasNextLine()) {
			String line = captionFile.readLine();
			if (line.contains("-->")) {
				String[] twoTimes = line.split(" --> ");
				String start = twoTimes[0];
				String end = twoTimes[1];
				int[] intStarts = convertToArray(start);
				int[] intEnds = convertToArray(end);
				TimeNode tN = new TimeNode(intStarts, intEnds);
				timeMap.put(currNum, tN);
				currNum += 1;
			}
		}
		
	}

	public int convertInt(String time) {
		//POSSIBLE BREAK HERE TIMESECTIONS SHOULD BE FLEXIBLE (ASSUMPTION: RIGID TIME STRUCTURE)
		return convertInt(convertToArray(time));
	}

	public int convertInt(int[] timeArray) {
		//POSSIBLE BREAK HERE TIMESECTIONS SHOULD BE FLEXIBLE (ASSUMPTION: RIGID TIME STRUCTURE)
		int milliseconds = timeArray[3];
		int seconds = timeArray[2]*1000;
		int minutes = timeArray[1]*60000;
		int hours = timeArray[0]*360000;
		int milliTime = hours + minutes + seconds + milliseconds;
		return milliTime;
	}

	public String convertString(int[] array) {
		//POSSIBLE BREAK HERE TIMESECTIONS SHOULD BE FLEXIBLE (ASSUMPTION: RIGID TIME STRUCTURE)
		String milliseconds = String.valueOf(array[3]);
		String seconds = String.valueOf(array[2]);
		String minutes = String.valueOf(array[1]);
		String hours = String.valueOf(array[0]);
		if (milliseconds.length() < 3) {
			for (int i = 0; i <= 3 - milliseconds.length(); i += 1) {
				milliseconds = "0" + milliseconds;
			}
		}
		if (seconds.length() < 2) {
			for (int i = 0; i <= 2 - seconds.length(); i += 1) {
				seconds = "0" + seconds;
			}
		}
		if (minutes.length() < 2) {
			for (int i = 0; i <= 2 - minutes.length(); i += 1) {
				minutes = "0" + minutes;
			}
		}
		if (hours.length() < 2) {
			for (int i = 0; i <= 2 - hours.length(); i += 1) {
				hours = "0" + hours;
			}
		}
		String stringTime = hours + ":" + minutes + ":" + seconds + "," + milliseconds;
		return stringTime;
	}

	public int[] convertToArray(String time) {
		String[] times = time.split(":|,");
		int[] timeSections = new int[times.length];
		for (int i = 0; i < times.length; i += 1) {
			timeSections[i] = Integer.valueOf(times[i]);
		}
		return timeSections;
	}

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
	public void shiftTime(String startTime, String endTime, int[] changeSections) {
		int[] startSections = convertToArray(startTime);
		int[] endSections = convertToArray(endTime);
		// int[] changeSections = convertToArray(change);
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileString));
			String line;
			String allFile = "";
			while((line = file.readLine()) != null) {
				if (line.contains(" --> ")) {
					String[] lineArray = line.split(" --> ");
					String beginning = lineArray[0];
					String ending = lineArray[1];
					int[] begin = convertToArray(beginning);
					int[] finish = convertToArray(ending);
					int[] beginResult = new int[begin.length];
					int[] endResult = new int[finish.length];
					if (convertInt(begin) >= convertInt(startSections) &&
						convertInt(begin) <= convertInt(endSections)) {
						for (int i = 0; i < begin.length; i += 1) {
							beginResult[i] = begin[i] + changeSections[i];
						}
						beginning = convertString(beginResult);
					}
					if (convertInt(finish) >= convertInt(startSections) &&
						convertInt(finish) <= convertInt(endSections)) {
						for (int i = 0; i < finish.length; i += 1) {
							endResult[i] = finish[i] + changeSections[i];
						}
						ending = convertString(endResult);
					}
					line = beginning + " --> " + ending;
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

	public void shiftTimeByIndex(int startIndex, int endIndex, int[] change) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileString));
			String line;
			String allFile = "";
			while((line = file.readLine()) != null) {
				try {
					int value = Integer.valueOf(line);
					if (value >= startIndex && value <= endIndex) {
						allFile += line + '\n';
						if ((line = file.readLine()) != null) {
							if (line.contains(" --> ")) {
								String[] lineArray = line.split(" --> ");
								String beginning = lineArray[0];
								String ending = lineArray[1];
								int[] begin = convertToArray(beginning);
								int[] finish = convertToArray(ending);
								int[] beginResult = new int[begin.length];
								int[] endResult = new int[finish.length];
								for (int i = 0; i < begin.length; i += 1) {
									beginResult[i] = begin[i] + change[i];
								}
								beginning = convertString(beginResult);
								for (int i = 0; i < finish.length; i += 1) {
									endResult[i] = finish[i] + change[i];
								}
								ending = convertString(endResult);
								line = beginning + " --> " + ending;
							}
						}
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

	public void generateSubIndices() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileString));
			String line;
			String allFile = "";
			int num = 1;
			while((line = file.readLine()) != null) {
				// System.out.println("no line break");
				if (line.length() == 0) {
					line = "\n" + num;
					System.out.println("line break: " + line);
					num += 1;
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

	private boolean indiciesCheck() {
		try {
			BufferedReader file = new BufferedReader(new FileReader(fileString));
			String line;
			while((line = file.readLine()) != null) {
				try {
				// WILL NOT WORK IF A STRING SUBTITLE CONTAINS ONLY AN INTEGER
					int val = Integer.valueOf(line);
					return false;
				}
				catch (NumberFormatException e) {
				}
			}
		}
		catch (Exception e) {
			System.out.println("Problem reading file.");
		}
		return true;
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		JOptionPane.showMessageDialog(null, "Welcome to the Caption Editor!");
		String file = JOptionPane.showInputDialog("Please enter the .srt file that you wish to edit (include .srt extension): ");;
		CaptionEditor cE = new CaptionEditor(file);
		while (true) {
		String command = JOptionPane.showInputDialog("Enter an edit command ('help' for command descriptions): ");
		switch (command) {
			case "help":
				In help = new In("help.txt");
                String helpStr = help.readAll();
                JOptionPane.showMessageDialog(null, helpStr);
                break;
            case "quit":
            	return;
            case "shiftNum":
            	//SOLVE CASE WITH OVERLAPPING NUMBERS
            	int startIndex = Integer.valueOf(JOptionPane.showInputDialog("Enter the starting index from which you want to shift: "));
            	int endIndex = Integer.valueOf(JOptionPane.showInputDialog("Enter the ending index at which you want the shift to end: "));
            	int changeIndex = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount you want to shift the indices by: "));
            	JOptionPane.showMessageDialog(null, "Indices from " + startIndex + " to " + endIndex + " have been shifted by " + changeIndex);
            	cE.shiftNum(startIndex, endIndex, changeIndex);
            	break;
            case "shiftTime":
            	String startTime = JOptionPane.showInputDialog("Enter the starting time from which you want to shift the time in this format (##:##:##,##): ");
            	String endTime = JOptionPane.showInputDialog("Enter the ending time at which you want the shift to end in this format (##:##:##,##): ");
            	int[] changeTime = new int[4];
            	changeTime[0] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of hours you want to shift the time by: "));
            	changeTime[1] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of minutes you want to shift the time by: "));
            	changeTime[2] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of seconds you want to shift the time by: "));
            	changeTime[3] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of milliseconds you want to shift the time by: "));
            	cE.shiftTime(startTime, endTime, changeTime);
            	JOptionPane.showMessageDialog(null, "Subtitles from " + startTime + " to " + endTime + " have been shifted by " + cE.convertString(changeTime));
            	break;
            case "shiftTimeByIndex":
            	
            	int startI = Integer.valueOf(JOptionPane.showInputDialog("Enter the starting index from which you want to shift the time: "));
            	int endI = Integer.valueOf(JOptionPane.showInputDialog("Enter the ending index at which you want the shift to end: "));
            	//POSSIBLE BREAK HERE TIMESECTIONS SHOULD BE FLEXIBLE (ASSUMPTION: RIGID TIME STRUCTURE)
            	int[] changeAmount = new int[4];
            	changeAmount[0] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of hours you want to shift the time by: "));
            	changeAmount[1] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of minutes you want to shift the time by: "));
            	changeAmount[2] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of seconds you want to shift the time by: "));
            	changeAmount[3] = Integer.valueOf(JOptionPane.showInputDialog("Enter the amount of milliseconds you want to shift the time by: "));
            	cE.shiftTimeByIndex(startI, endI, changeAmount);
            	JOptionPane.showMessageDialog(null, "Subtitles from indices " + startI + " to " + endI + " have been shifted by " + cE.convertString(changeAmount));
            	break;
            case "generateIndices":
            	if (cE.indiciesCheck()) {
            		cE.generateSubIndices();
            		JOptionPane.showMessageDialog(null, "An index number for each subtitle has been added.");
            	}
            	else {
            		JOptionPane.showMessageDialog(null, "Error: The .srt files already contains indices");
            	}
            	break;
            default:
            	JOptionPane.showMessageDialog(null, "Invalid command.");  
                break;
			}
		}
	}
}

class TimeNode<S, E> {
	protected S start;
	protected E end;

	public TimeNode(S startTime, E endTime) {
		start = startTime;
		end = endTime;
	}

	public String toString() {
		String tN = Arrays.toString((int[]) start) + " to " + Arrays.toString((int[]) end);
		return tN;
	}
}

// class SubtitleNode {
// 	protected TimeNode timeNode;
// 	protected String[] subtitles;

// 	public timeIndexNode(TimeNode tN, String[] subs) {
// 		index = sectionNumber;
// 		time = timeSection;
// 	}
// }
