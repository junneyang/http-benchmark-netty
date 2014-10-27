package httpclient;

public class testreport {

	public testreport() {
		// TODO Auto-generated constructor stub
	}

	public static void  pub(String start_time, String end_time, int thread_num, int client_num, double test_time,
			long total_req, long total_res, long total_err, String qps, String avg_latency,
			long below_10, long between_10_20, long between_20_30, long over_30) throws Exception {
		System.out.println("======================================================================================");
		System.out.println("|                                 TEST REPORT                                        |");
		System.out.println("======================================================================================");
		System.out.println("| START_TIME            : " + start_time);
		System.out.println("| END_TIME              : " + end_time);
		System.out.println("|-------------------------------------------------------------------------------------");
		System.out.println("| THREAD_NUM            : " + thread_num);
		System.out.println("| CLIENT_NUM            : " + client_num);
		System.out.println("| TEST_TIME             : " + test_time + " min");
		System.out.println("|-------------------------------------------------------------------------------------");
		System.out.println("| TOTAL_REQ             : " + total_req);
		System.out.println("| TOTAL_RES             : " + total_res);
		System.out.println("| TOTAL_ERR             : " + total_err);
		System.out.println("| QPS                   : " + qps);
		System.out.println("|-------------------------------------------------------------------------------------");
		System.out.println("| AVG_LATENCY           : " + avg_latency + " ms");
		System.out.println("| BELOW_10              : " + below_10);
		System.out.println("| BT_10_20              : " + between_10_20);
		System.out.println("| BT_20_30              : " + between_20_30);
		System.out.println("| OVER_30               : " + over_30);
		System.out.println("======================================================================================");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
