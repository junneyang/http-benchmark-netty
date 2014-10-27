package httpclient;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class timmer implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		synchronized (httpbenchmark.lock) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ct = sdf.format(date);
			//System.err.println(ct);
			
			DecimalFormat df=new DecimalFormat("0.00");
			double elapsed = (System.currentTimeMillis() - httpbenchmark.start_time)/1000.0;
			String qps = df.format((httpbenchmark.total_res/(elapsed)));
			httpbenchmark.qps_stat.put(ct, qps);
			//强制垃圾回收，防止内存泄漏
			System.gc();
		}
	}
	
}
public class qpsstattimer {
	private ScheduledExecutorService service;
	public qpsstattimer() {
		// TODO Auto-generated constructor stub
		this.service = Executors.newSingleThreadScheduledExecutor();
		//this.service = Executors.newScheduledThreadPool(4);
	}
	public void run() {
		try {
			this.service.scheduleAtFixedRate(new timmer(), 0, 60, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
		}
	}
	public void disstat() {
		/*Iterator<Map.Entry<String, String>> iter = easysocketbenchmark.qps_stat.entrySet().iterator();
	    while (iter.hasNext()) { 
	    	Map.Entry<String, String> entry = iter.next();
	    	System.out.println(entry.getKey() + "\t" + entry.getValue());
	    }*/
		new statchart().createChart();
	}
	
	public void stop() {
		this.service.shutdown();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		qpsstattimer q = new qpsstattimer();
		try {
			System.err.println("ok");
			q.run();
			System.err.println("ok");
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			System.err.println("stop");
			q.stop();
		}
		
	}
}
