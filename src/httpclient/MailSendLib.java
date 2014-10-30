package httpclient;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailSendLib {
    public MailSendLib() {
        // TODO Auto-generated constructor stub
    }
    public void send(String mailserver, String sender, String[] receivers, String subject, String content) throws Exception {
        Properties p = new Properties();
        p.put("mail.smtp.host", mailserver);
        //p.put("mail.smtp.auth", "true");
        
        /*Authenticator a = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("xxx@qq.com","password");
            }
        };*/
        Session s = Session.getDefaultInstance(p, null);
        MimeMessage m = new MimeMessage(s);
        m.setFrom(new InternetAddress(sender));
        InternetAddress[] r = new InternetAddress[receivers.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = new InternetAddress(receivers[i]);
        }
        m.setRecipients(Message.RecipientType.TO, r);
        m.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        m.setSentDate(new Date());
        Multipart multipart = new MimeMultipart();
        MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(body);//发件内容
        
        MimeBodyPart imgbody = new MimeBodyPart();
        DataSource fds = HttpBenchmark.datasource;
        imgbody.setDataHandler(new DataHandler(fds));
        imgbody.setHeader("Content-ID","<image>");
        multipart.addBodyPart(imgbody);
        
        m.setContent(multipart);
        Transport.send(m);
    }
    
    public static void  run(String mailserver, String sender, String receivers[],
            String start_time, String end_time, int thread_num, int client_num, double test_time,
            long total_req, long total_res, long total_err, String qps, String avg_latency,
            long below_10, long between_10_20, long between_20_30, long over_30) throws Exception {
        String subject = "【测试报告】easyhttpbenchmark性能测试报告";
        String content = "<div style='color:black;text-align:center;display:block;font-size:20px;font-family:微软雅黑'>easyhttpbenchmark性能测试报告</div>";
        content += "<div style='font-family:微软雅黑;font-size:12px;font-style:italic;text-align:center;'>";
        content += "<a href='https://github.com/junneyang/http-benchmark-netty'>@easyhttpbenchmark</a> 2014";
        content += "</div>";
        content += "<ol><li style='font-weight:bold;'>性能指标</li>";
        content += "======================================================================================<br/>";
        content += "|                                 TEST REPORT                                        |<br/>";
        content += "======================================================================================<br/>";
        content += "| START_TIME            : " + start_time + "<br/>";
        content += "| END_TIME              : " + end_time + "<br/>";
        content += "|-------------------------------------------------------------------------------------<br/>";
        content += "| THREAD_NUM            : " + thread_num + "<br/>";
        content += "| CLIENT_NUM            : " + client_num + "<br/>";
        content += "| TEST_TIME             : " + test_time + " min" + "<br/>";
        content += "|-------------------------------------------------------------------------------------<br/>";
        content += "| TOTAL_REQ             : " + total_req + "<br/>";
        content += "| TOTAL_RES             : " + total_res + "<br/>";
        content += "| TOTAL_ERR             : " + total_err + "<br/>";
        content += "| QPS                   : " + qps + "<br/>";
        content += "|-------------------------------------------------------------------------------------<br/>";
        content += "| AVG_LATENCY           : " + avg_latency + " ms" + "<br/>";
        content += "| BELOW_10              : " + below_10 + "<br/>";
        content += "| BT_10_20              : " + between_10_20 + "<br/>";
        content += "| BT_20_30              : " + between_20_30 + "<br/>";
        content += "| OVER_30               : " + over_30 + "<br/>";
        content += "======================================================================================<br/><br/>";
        content += "<li  style='font-weight:bold;'>性能曲线</li>";
        content += "<img src=\"cid:image\"></ol><br/>";
        
        new MailSendLib().send(mailserver, sender, receivers, subject, content);
    }
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}
