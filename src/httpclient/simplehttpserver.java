package httpclient;

public class simplehttpserver {

	public simplehttpserver() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port;
		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		} else {
			port = 4444;
		}
		try {
			new asynchttpserver(port).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
