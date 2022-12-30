import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
		List<List<String>> list = readValues();
		// System.out.println(list.get(0));
		List<Integer> valueList = new ArrayList<Integer>();
		List<Integer> weightList = new ArrayList<Integer>();

		for (int i = 1; i < list.size(); i++) {
			valueList.add(Integer.parseInt(list.get(i).get(4)));
			weightList.add(Integer.parseInt(list.get(i).get(5)));
		}
		List<List<String>> list1 = readSequential();
		List<ArrayList<Double>> sequential_data = new ArrayList<ArrayList<Double>>();
		for (int i = 1; i < list1.size(); i++) {
			ArrayList<Double> row = new ArrayList<>();
			for (int j = 1; j < list1.get(0).size(); j++) {
				row.add(Double.parseDouble(list1.get(i).get(j)));
			}
			sequential_data.add(row);
		}
		// System.out.println(sequential_data.get(0).get(1));
		calculate30Minute(list);

	}

	public static List<List<String>> readValues() throws IOException {
		try {
			List<List<String>> data = new ArrayList<>();// list of lists to store data
			String file = "term_project_value_data.csv";// file path
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// Reading until we run out of lines
			String line = br.readLine();
			while (line != null) {
				List<String> lineData = Arrays.asList(line.split(","));// splitting lines
				data.add(lineData);
				line = br.readLine();
			}

			// printing the fetched data
			// for(List<String> list : data)
			// {
			// for(String str : list)
			// System.out.print(str + " ");
			// System.out.println();
			// }
			br.close();
			return data;
		} catch (Exception e) {
			System.out.print(e);
			List<List<String>> data = new ArrayList<>();// list of lists to store data
			return data;
		}

	}

	public static List<List<String>> readSequential() throws IOException {
		try {
			List<List<String>> data = new ArrayList<>();// list of lists to store data
			String file = "term_project_sequential_data.csv";// file path
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// Reading until we run out of lines
			String line = br.readLine();
			while (line != null) {
				List<String> lineData = Arrays.asList(line.split(","));// splitting lines
				data.add(lineData);
				line = br.readLine();
			}

			// printing the fetched data
			// for(List<String> list : data)
			// {
			// for(String str : list)
			// System.out.print(str + " ");
			// System.out.println();
			//
			// }
			br.close();
			return data;
		} catch (Exception e) {
			System.out.print(e);
			List<List<String>> data = new ArrayList<>();// list of lists to store data
			return data;
		}

	}

	public static void calculate30Minute(List<List<String>> unSortedList) throws IOException {

		List<List<String>> sortedList = unSortedList.subList(1, unSortedList.size());

		// this.valPerMs = (float)( (float)popularity/(float)length);

		Collections.sort(sortedList, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> list1, List<String> list2) {
				// sort by duration asc unconnebt next 3 lines
				float value1 = (float) (Integer.parseInt(list1.get(4)) / (float) Integer.parseInt(list1.get(5)))
						* 10000000;
				float value2 = (float) (Integer.parseInt(list2.get(4)) / (float) Integer.parseInt(list2.get(5)))
						* 10000000;
				return (int) (value2 - value1);

			}
		});

		sortedList.add(0, unSortedList.get(0));

		ArrayList<Song> allSongs = new ArrayList<>();
		int totalLength = 0;
		int lastTotalLength = 0;

		int totalPopularity = 0;
		for (int i = 1; i < sortedList.size(); i++) {

			int length = Integer.parseInt(sortedList.get(i).get(5));
			totalLength += length;
			if (totalLength > 1800000) {
				totalLength = lastTotalLength;
				break;
			}
			int popularity = Integer.parseInt(sortedList.get(i).get(4));
			totalPopularity += popularity;
			lastTotalLength = totalLength;
			Song s = new Song(i, length, popularity);
			allSongs.add(s);

		}
		for (int j = 0; j < allSongs.size(); j++) {
			System.out.print("Track id: ");
			System.out.print(allSongs.get(j).id);
			System.out.print(" - popularity: ");
			System.out.print(allSongs.get(j).popularity);
			System.out.print(" - length: ");
			System.out.println(allSongs.get(j).length);
		}
		System.out.print("TotalPopularity: ");
		System.out.println(totalPopularity);
		System.out.print("totalLength: ");
		System.out.println(totalLength / 1000 + " second");
		System.out.print("Total Score :");
		System.out.println(totalPopularity - ((1800 - (totalLength / 1000)) * 0.02));

	}

}

class Song {
	int id;
	int length;
	int popularity;

	public Song(int id, int length, int popularity) {
		this.id = id;
		this.length = length;
		this.popularity = popularity;
	}

}
