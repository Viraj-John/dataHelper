package com.qa.api.fetch.helper;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.qa.api.fetch.apiHandler.ApiHandler;

public class Helpers {

	public long countLineFast(String fileName) {

		long lines = 0;

		try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))) {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean endsWithoutNewLine = false;
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n')
						++count;
				}
				endsWithoutNewLine = (c[readChars - 1] != '\n');
			}
			if (endsWithoutNewLine) {
				++count;
			}
			lines = count;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	public void WriteResponse(String key, String value, boolean REST) throws IOException {

		new Thread(new Runnable() {
	        public void run(){
	        	FileWriter myWriter;
	    		try {

	    			if (REST)
	    				myWriter = new FileWriter(key + ".json");
	    			else
	    				myWriter = new FileWriter(key + ".xml");
	    			myWriter.write(value);
	    			myWriter.close();

	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		}
	        }
	    }).start();

	}

	public String PerformAPIActions(String URL, String newRequestLine, String Client, String Filter,
			String outputFolderPath, String key, boolean REST, boolean GET, String Logs) throws IOException {
		String logs = Logs;
		if (!GET) {

			String Response = new ApiHandler().getResponsePost(URL, newRequestLine, Client).body().asPrettyString();
			if (!StringUtils.isBlank(Filter)) {
				if (Response.contains(Filter)) {
					logs = logs.replace("-- executing", "** Data Identified");
					WriteResponse(outputFolderPath + "/" + key, Response, REST);

				} else {
					logs = logs.replace("-- executing", "-- Data Not Available");
				}
			} else {
				logs = logs.replace("-- executing", "-- Data Added Without filter");
				WriteResponse(outputFolderPath + "/" + key, Response, REST);
			}
		} else {
			String Response = new ApiHandler()
					.getResponseGet(URL.replace("{data}", key), Client).body()
					.asPrettyString();
			if (!StringUtils.isBlank(Filter)) {
				if (Response.contains(Filter)) {
					logs = logs.replace("-- executing", "** Data Identified");
				WriteResponse(outputFolderPath + "/" + key, Response, REST);

				} else {
					logs = logs.replace("-- executing", "-- Data Not Available");
				}
			} else {
				logs = logs.replace("-- executing", "-- Data Added Without filter");
				WriteResponse(outputFolderPath + "/" + key, Response, REST);
			}

		}

		return logs;
	}

}
