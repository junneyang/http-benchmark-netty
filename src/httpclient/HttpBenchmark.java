package httpclient;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.util.ByteArrayDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;

public class HttpBenchmark {
    public static long start_time = 0;
    public static long end_time = 0;
    public static long total_req = 0;
    public static long total_res = 0;
    public static long total_err = 0;
    public static long below_10 = 0;
    public static long between_10_20 = 0;
    public static long between_20_30 = 0;
    public static long over_30 = 0;
    public static long total_res_time = 0;
    
    public static byte[] lock = new byte[0];
    
    public static Map<String, String> qps_stat = new LinkedHashMap<String, String>();
    public static ByteArrayDataSource datasource;
    
    public HttpBenchmark() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LogLib.loginit();
        Logger logger = LogManager.getLogger(HttpClient.class.getName());
        logger.info("httpbenchmark start");
        
        String host;
        int port;
        int client_num;
        double test_time;
        int print_res;
        String filepath;
        JSONObject jsondata;
        String[] receivers;

        if (args.length == 7) {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                client_num = Integer.parseInt(args[2]);
                test_time = Double.parseDouble(args[3]);
                print_res = Integer.parseInt(args[4]);
                filepath = args[5];
                receivers = args[6].split(",");
            } catch (Exception e) {
                // TODO: handle exception
                logger.error(e.toString());
                return;
            }
        } else {
            HelpLib.disinfo("httpbenchmark.jar");
            logger.info("httpbenchmark help info");
            logger.info("httpbenchmark complete");
            return;
        }
        
        try {
            FileLib f = new FileLib(filepath);
            String datastr = f.gettestdatastring();
            jsondata = f.getjsondata(datastr);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("ERROR : test data file parse error");
            return;
        }
        
        
        QPSStatTimer q = new QPSStatTimer();
        try {
            q.run();
            logger.info("httpbenchmark qps stat timer start");
            new HttpBenchmarkClient(host, port, client_num, test_time, print_res, jsondata).start();
        } catch (Exception e) {
            if (print_res == 1) {
                logger.error(e.toString());
            }    
        } finally {
            Date dates = new Date(start_time);
            Date datee = new Date(end_time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String st = sdf.format(dates);
            String et = sdf.format(datee);
            
            DecimalFormat df = new DecimalFormat("0.00");
            int tn = Runtime.getRuntime().availableProcessors() * 2;
            
            String qps = df.format((total_res / (test_time * 60)));
            String avg_latency;
            if (total_res == 0) {
                avg_latency = "0";
            } else {
                avg_latency = df.format((double) total_res_time / (double) total_res);
            }
            
            try {
                TestReportLib.pub(st, et, tn, client_num, test_time, total_req, total_res, total_err, 
                    qps, avg_latency, below_10, between_10_20, between_20_30, over_30);
                Properties prop = new Properties();
                prop.load(new FileInputStream("./conf/httpbenchmark.conf"));
                String mailserver = prop.getProperty("mailserver");
                String sender = prop.getProperty("sender");
                q.disstat();
                MailSendLib.run(mailserver, sender, receivers, st, et, tn, client_num, test_time, 
                    total_req, total_res, total_err, qps, avg_latency, below_10, between_10_20, between_20_30, over_30);
                q.stop();
                logger.info("httpbenchmark mail sending success");
            } catch (Exception e2) {
                // TODO: handle exception
                logger.error(e2.toString());
                return;
            } finally {
                logger.info("httpbenchmark complete");
            }
        }
    }

}
