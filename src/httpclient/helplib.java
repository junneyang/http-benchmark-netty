package httpclient;

public class helplib {

	public helplib() {
		// TODO Auto-generated constructor stub
	}
	
	public static void  disinfo (String pluginname) {
		if (pluginname.equals("httpbenchmark.jar")) {
			System.out.println("======================================================================================");
			System.out.println("|                                 Usage Instructions                                 |");
			System.out.println("======================================================================================");
			System.out.println("|USAGE          : java -jar " + pluginname + " <IP> <PORT> <CLIENT_NUM> <TEST_TIME> <IS_OUTPUT> <TEST_DATA> <RECEIVER>");
			System.out.println("|           IP  : sever ip, eg : 127.0.0.1 ");
			System.out.println("|         PORT  : server port, eg : 18999 ");
			System.out.println("|   CLIENT_NUM  : total socket connection numbers. eg : 20 ");
			System.out.println("|    TEST_TIME  : total test time(min), eg : 1.0 ");
			System.out.println("|    IS_OUTPUT  : flag of whether print the response content. eg : 0 ");
			System.out.println("|    TEST_DATA  : test data file path. eg : ./data/http_benchmark_post.data ");
			System.out.println("|    RECEIVER   : mail receiver list. eg : yangjun03@baidu.com,linqiaoying@baidu.com \n|");
			System.out.println("|EXAMPLE        : java -jar " + pluginname + " 127.0.0.1 18999 20 1.0 0 ./data/http_benchmark_post.data yangjun03@baidu.com");
			System.out.println("|-------------------------------------------------------------------------------------");
			System.out.println("|MORE           : if any questions, please contact 597092663@qq.com ");
			System.out.println("======================================================================================");
		} else if (pluginname.equals("httpclient.jar")) {
			System.out.println("======================================================================================");
			System.out.println("|                                 Usage Instructions                                 |");
			System.out.println("======================================================================================");
			System.out.println("|USAGE          : java -jar " + pluginname + " <IP> <PORT> <TestData>");
			System.out.println("|           IP  : sever ip, eg : 127.0.0.1 ");
			System.out.println("|         PORT  : server port, eg : 18999 ");
			System.out.println("|     TestData  : request data. eg : ./data/http_function_post.data \n|");
			System.out.println("|EXAMPLE        : java -jar " + pluginname + " 127.0.0.1 18999 ./data/http_function_post.data");
			System.out.println("|-------------------------------------------------------------------------------------");
			System.out.println("|MORE           : if any questions, please contact 597092663@qq.com ");
			System.out.println("======================================================================================");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
