package httpclient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;

public class filelib {
	private String filepath;

	public filelib(String filepath) {
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
	public JSONObject getjsondata(String datastr) {
		JSONObject jsondata = JSONObject.fromObject(datastr);
		return jsondata;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = "./testdata/http_function_test.data";
		try {
			filelib f = new filelib(filepath);
			String datastr = f.gettestdatastring();
			System.out.println(datastr);
			JSONObject jsondata = f.getjsondata(datastr);
			String method = jsondata.getString("method");
			String url = jsondata.getString("url");
			String content = jsondata.getJSONObject("data").toString();
			System.out.println(method);
			System.out.println(url);
			System.out.println(content);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
