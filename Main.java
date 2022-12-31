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
	 


		List<List<String>> sortedList = list.subList(1, list.size());

		Collections.sort(sortedList, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> list1, List<String> list2) {
			 
				float value1 = (float) (Integer.parseInt(list1.get(4)) / (float) Integer.parseInt(list1.get(5)))
						* 10000000;
				float value2 = (float) (Integer.parseInt(list2.get(4)) / (float) Integer.parseInt(list2.get(5)))
						* 10000000;
				return (int) (value2 - value1);

			}
		});

		sortedList.add(0, list.get(0));

		ArrayList<Track> allSongs = new ArrayList<>();
		int totalLength = 0;
		int lastTotalLength = 0;
		int totalPopularity = 0;

		for (int i = 1; i < sortedList.size(); i++) {

			int length = Integer.parseInt(sortedList.get(i).get(5));
			int songId = Integer.parseInt(sortedList.get(i).get(0));
			totalLength += length;
			if (totalLength > 1800000) {
				totalLength = lastTotalLength;
				break;
			}
			int popularity = Integer.parseInt(sortedList.get(i).get(4));
			totalPopularity += popularity;
			lastTotalLength = totalLength;
			double[] seqValues = new double[50];
			Arrays.fill(seqValues, 0.0);
			for (int k = 1; k < sortedList.size(); k++) {

				if (Integer.parseInt(list1.get(k).get(0)) == Integer.parseInt(sortedList.get(i).get(0))) {
					List<String> tmpArr;
					tmpArr = list1.get(k);

					for (int i2 = 0; i2 < tmpArr.size() - 1; i2++) {
						seqValues[i2] = Double.valueOf(tmpArr.get(i2));
					}

				}
			}

			Track s = new Track(songId, length, popularity, seqValues);
			allSongs.add(s);

		}


		double[][] songOrder = new double[allSongs.size()][2];
		for(int y=0;y<allSongs.size();y++)
		{
			songOrder[y][0]=-1;
		}
		int firstSong = 0;
		int lastSong = 0;
		int lastSongValue = 0;
		int firstSongValue = 0;
		for (int x = 0; x < allSongs.size(); x++) {
			if (allSongs.get(x).track_individual_value > firstSongValue) {
				lastSong = firstSong;
				lastSongValue = firstSongValue;

				firstSong = allSongs.get(x).track_id;
				firstSongValue = allSongs.get(x).track_individual_value;

			} else if (allSongs.get(x).track_individual_value > lastSongValue) {
				lastSong = allSongs.get(x).track_id;
				lastSongValue = allSongs.get(x).track_individual_value;
			}
		}
		songOrder[0][0] = (double) firstSong;
		songOrder[songOrder.length - 1][0] = (double) lastSong;
		int selectThis = -1;
		int n = 0;

		for (int nb = 1; nb <= allSongs.size() - 3; nb++) {
			if (nb == 1) {
				n = firstSong;
			} else {
				n = selectThis;
			}

			 
			double goodNext = -1;

			 
			ArrayList<Double> passScore = sequential_data.get(n);
		 
			for (int m = 0; m < passScore.size()-1 ; m++) {

				if (passScore.get(m+1) > goodNext && Integer.parseInt(list1.get(m+1).get(0)) != lastSong) {
					boolean usedBefore = false;
					 
					for (int cnt = 0; cnt < songOrder.length; cnt++) {
						if (songOrder[cnt][0] == Double.parseDouble(list1.get(m+1).get(0)) && songOrder[cnt][0]!=firstSong ) {
							usedBefore = true;
							break;
						}
					}
					 
					boolean inTheList = false;
					for (int cnt2 = 0; cnt2 < allSongs.size() ; cnt2++) {
						if (m == allSongs.get(cnt2).track_id) {
							inTheList = true;
							break;
						}
					}

					if (usedBefore == false && inTheList == true) {
						goodNext = passScore.get(m+1);

						selectThis = Integer.parseInt(list1.get(m+1).get(0));

					}
				 

				}

			}

			songOrder[nb][0] = (double) selectThis;
			songOrder[nb][1] = goodNext;
			
			
		 

		}

		for (int j = 0; j < allSongs.size(); j++) {

			System.out.print("Track id: ");
			System.out.print(allSongs.get(j).track_id);
			System.out.print(" - popularity: ");
			System.out.print(allSongs.get(j).getIndividualValue());
			System.out.print(" - duration: ");
			System.out.print(allSongs.get(j).track_duration);
			System.out.println();
			 
		}

		for (int j = 0; j < allSongs.size(); j++) {

			System.out.print("OrderId : ");
			System.out.print(j+1);

			System.out.print("  - SongId : ");
			int aa = (int) songOrder[j][0];

			System.out.print(aa);
			System.out.print(" - score : ");
			System.out.println((songOrder[j][1]));
		}

		System.out.print("TotalPopularity: ");
		System.out.println(totalPopularity);
		System.out.print("totalLength: ");
		System.out.println(totalLength / 1000 + " second");
		System.out.print("Total Score :");
		System.out.println(totalPopularity - ((1800 - (totalLength / 1000)) * 0.02));

	}

	public static List<List<String>> readValues() throws IOException {
		try {
			List<List<String>> data = new ArrayList<>();  
			String file = "term_project_value_data.csv"; 
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

		 
			String line = br.readLine();
			while (line != null) {
				List<String> lineData = Arrays.asList(line.split(",")); 
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

}
