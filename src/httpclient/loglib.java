package httpclient;

import java.io.FileInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class loglib {
	public loglib() {
		// TODO Auto-generated constructor stub
	}
	public static void loginit() {
		ConfigurationSource source;
		try {
			/*String user_dir = System.getProperty("user.dir");
			String conf_file = "\\conf\\log4j2.xml";
			String conf_file_path = user_dir + conf_file;
			System.out.println(conf_file_path);
			source = new ConfigurationSource(new FileInputStream(conf_file_path));
			Configurator.initialize(null, source);*/
			String user_dir=System.getProperty("user.dir");
			String conf_file = "/conf/log4j2.xml";
			String conf_file_path = user_dir + conf_file;
			//System.out.println(conf_file_path);
			source = new ConfigurationSource(new FileInputStream(conf_file_path));
			Configurator.initialize(null, source);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void loginitsrv() {
		ConfigurationSource source;
		try {
			/*String user_dir = System.getProperty("user.dir");
			String conf_file = "\\conf\\log4j2.xml";
			String conf_file_path = user_dir + conf_file;
			System.out.println(conf_file_path);
			source = new ConfigurationSource(new FileInputStream(conf_file_path));
			Configurator.initialize(null, source);*/
			String user_dir=System.getProperty("user.dir");
			String conf_file = "/conf/log4j2-srv.xml";
			String conf_file_path = user_dir + conf_file;
			//System.out.println(conf_file_path);
			source = new ConfigurationSource(new FileInputStream(conf_file_path));
			Configurator.initialize(null, source);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		loglib.loginit();
		Logger logger = LogManager.getLogger(loglib.class.getName());
		logger.trace("我是debug信息");
        logger.debug("我是debug信息");
        logger.info("我是info信息");    //info级别的信息
        logger.warn("我是warn信息");
        logger.error("Did it again!");   //error级别的信息，参数就是你输出的信息
        logger.fatal("我是fatal信息");
	}

}
