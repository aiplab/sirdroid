package edu.uprm.aiplab;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class SirlabAndroid {

	private byte[] wav;

	private String buf1;
	private List<String> images;
	private String log;
	// private static final int TIMEOUT_MS = 5000;
	private static String url = "http://136.145.56.222/sirlab/android.php";
	String twoHyphens = "--";
	private static String boundary = "----*****";
	private static String lineEnd = "\r\n";

	public SirlabAndroid(byte[] wav) {
		this.wav = wav;
		this.log = new String();
		this.buf1 = new String();
		// this.log=new String();
	}

	public void send() {

		DataOutputStream outputStream;
		URL url1;
		try {
			url1 = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) url1
					.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			// Enable POST method connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// Allow Inputs & Outputs connection.setDoInput(true);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.write(addParam("enviar", "yes"));
			outputStream.write(addParam("numsamplesframe", "8192"));
			outputStream.write(addParam("rand", "1000"));
			outputStream.write(addParam("jumpsamples", "25"));
			outputStream.write(addParam("windowwidth", "256"));
			outputStream.write(addParam("zeropadding", "16384"));
			outputStream.write(addParam("freqmin", "0"));
			outputStream.write(addParam("freqmax", "1200"));
			outputStream.write(addParam("frameoverlay", "100"));
			outputStream.write(addParam("startpercent", "0"));
			outputStream.write(addParam("endpercent", "100"));
			outputStream.write(addParam("file", "1000"));
			outputStream.write(addRawFile("wavfile", "value3"));
			outputStream.write(wav);
			outputStream.write(lineEnd.getBytes());

			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// connection.getResponseCode(); // serverResponseMessage =

			int response = connection.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {

				connection.getResponseMessage();
				outputStream.flush();
				outputStream.close();

				//InputStream is = (InputStream) connection.getInputStream();

				parseData(connection.getInputStream());

				String data = new String(buf1);

				StringTokenizer st = new StringTokenizer(data,
						"$$$$$$$$$$$$$$$$");
				log = st.nextToken();

				StringTokenizer st1 = new StringTokenizer(st.nextToken(),
						"****");
				images = new ArrayList<String>();
				while (st1.hasMoreTokens()) {
					images.add(st1.nextToken());
				}
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	private void parseData(InputStream is)
	{
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(is));

		String temp;
		buf1 = new String("");
		try {
			while ((temp = bufferedReader.readLine()) != null) {
				// Parsing of data here
				buf1 += temp+"\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private byte[] addParam(String name, String value) {
		String proc = new String();
		proc += twoHyphens;
		proc += boundary;
		proc += lineEnd;
		proc += "Content-Disposition: form-data; name=\"";
		proc += name;
		proc += "\"";
		proc += lineEnd;
		proc += lineEnd;
		proc += value;
		proc += lineEnd;
		return proc.getBytes();
	}

	private byte[] addRawFile(String name, String filename) {
		String proc = new String();
		proc += twoHyphens;
		proc += boundary;
		proc += lineEnd;
		proc += "Content-Disposition: form-data; name=\"";
		proc += name;
		proc += "\"";
		proc += ";filename=\"android.wav\"";
		proc += lineEnd;
		proc += "Content-Type: application/octet-stream";
		proc += lineEnd;
		proc += lineEnd;
		// proc += wav;
		// proc += lineEnd;
		return proc.getBytes();

	}

	public String getResult() {
		return buf1;
	}

	public String getLog() {
		return log;
	}

	public List<String> getImages() {
		return images;
	}
}
