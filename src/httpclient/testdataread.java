package httpclient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class testdataread {
	private String filepath;

	public testdataread(String filepath) {
		// TODO Auto-generated constructor stub
		this.filepath = filepath;
	}

	public String gettestdatastring () throws Exception {
		FileInputStream fis=new FileInputStream(this.filepath);
		InputStreamReader isr=new InputStreamReader(fis,"utf-8");
		BufferedReader br=new BufferedReader(isr);
		
		StringBuilder testdatastring = new StringBuilder();
		String line="";
		while((line=br.readLine()) != null){
			testdatastring.append(line);
		}
		fis.close();
		return testdatastring.toString();
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = "./testdata/http_post_json.data";
		try {
			System.out.println(new testdataread(filepath).gettestdatastring());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
