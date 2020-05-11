import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class dummy2 {

	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static ArrayList<String> fileNames;
	public static ArrayList<String>[] fileData;

	public static void main(String[] args) throws Exception {
		int cnt = 0;
		String feature = null, numbers = null;
		List<String> list = new ArrayList<String>();
		HashSet<String> set = new HashSet();

		String projectLocation = System.getProperty("user.dir");
		File src = new File(projectLocation + "/workbook.xlsx");
		FileInputStream fileInputStream = new FileInputStream(src);
		workbook = new XSSFWorkbook(fileInputStream);
		sheet = workbook.getSheet("Sheet1");

		int rowCount = sheet.getLastRowNum() + 1; // number of rows
		int columnCount = sheet.getRow(0).getLastCellNum(); // columns

		for (int i = 0; i < rowCount; i++) {
			System.out.println();
			cnt = 0;
			for (int j = 0; j < columnCount; j++) {

				switch (sheet.getRow(i).getCell(j).getCellType()) { // checking
				// data type
				case STRING:
					if (cnt == 0) {
						feature = sheet.getRow(i).getCell(j).getStringCellValue();
						cnt++;
					} else if (cnt == 1) {
						numbers = sheet.getRow(i).getCell(j).getStringCellValue();
					}
					break;
				case NUMERIC:
					list.add(feature + "=" + numbers + "=" + sheet.getRow(i).getCell(j).getNumericCellValue());
					set.add(sheet.getRow(i).getCell(j).getNumericCellValue() + ""); // for
					// file
					// names
					break;

				}

			}
			System.out.println();

		}

		displayFileNamesData(set);
		displayListData(list);
		writeDataToFiles(fileData, fileNames);

	}

	private static void writeDataToFiles(ArrayList<String>[] fileData2, ArrayList<String> fileNames2) {
		FileWriter writer;
		try {

			for (int i = 0; i < fileNames2.size(); i++) {
				File file = new File("./" + fileNames2.get(i) + "/" + fileNames2.get(i) + ".txt");
				file.getParentFile().mkdir();  //creating directories
				file.createNewFile();
				writer = new FileWriter(file);
				for (String data : fileData2[i]) {
					writer.write(data.substring(0, data.lastIndexOf("=")) + "=1.0" + System.lineSeparator());
				}
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void displayFileNamesData(HashSet<String> set) {
		Iterator<String> iterator = set.iterator();
		System.out.println("###################");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

		fileNames = new ArrayList<String>(set);
	}

	private static void displayListData(List<String> list) {
		fileData = new ArrayList[fileNames.size()];
		for (int i = 0; i < fileNames.size(); i++) {
			fileData[i] = new ArrayList<String>();
		}
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			String data = (String) iterator.next();
			int index = fileNames.indexOf(data.substring(data.lastIndexOf("=") + 1));
			fileData[index].add(data);
		}
		for (int i = 0; i < fileData.length; i++) {
			System.out.println("************" + i + "************");
			for (int j = 0; j < fileData[i].size(); j++) {
				System.out.println(fileData[i].get(j) + " ");
			}
		}
	}

}