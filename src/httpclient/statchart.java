package httpclient;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.mail.util.ByteArrayDataSource;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

public class statchart {
	public statchart() {
    }
	
    //获得数据集
    public XYDataset createdataset() {
    	TimeSeries linedataset = new TimeSeries("QPS DISTRIBUTION CURVE");
    	Iterator<Map.Entry<String, String>> iter = httpbenchmark.qps_stat.entrySet().iterator();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	int flag = 0;//第一个点丢掉
    	try {
    		while (iter.hasNext()) { 
    			Map.Entry<String, String> entry = iter.next();
    			if (flag == 1) {
    				linedataset.addOrUpdate(new Second(sdf.parse(entry.getKey())), Double.parseDouble(entry.getValue()));
    			}
    			flag = 1;
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	TimeSeriesCollection timeSeriesCollection =new TimeSeriesCollection(linedataset);
		return timeSeriesCollection;
    }

    //生成图标对象JFreeChart
	/*
	*整个大的框架属于JFreeChart
	*坐标轴里的属于 Plot 其常用子类有：CategoryPlot, MultiplePiePlot, PiePlot , XYPlot 
	*/
    public void createChart() {
    	try {
    		/*//创建主题样式
    		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
    		//设置标题字体
    		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
    		//设置图例的字体
    		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
    		//设置轴向的字体
    		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
    		//应用主题样式
    		ChartFactory.setChartTheme(standardChartTheme);*/
    		
    		//定义图标对象
    		JFreeChart chart = ChartFactory.createTimeSeriesChart(
    				"QPS Distribution Curve",// 报表题目，字符串类型
    				"Time", // 横轴
    				"QPS", // 纵轴
    				this.createdataset(), // 获得数据集
    				false, // 显示图例
    				false, // 不用生成工具
    				false // 不用生成URL地址
    		);
    		//整个大的框架属于chart  可以设置chart的背景颜色
    		chart.setBackgroundPaint(Color.LIGHT_GRAY);
    		// 生成图形
    		XYPlot plot = chart.getXYPlot();
    		// 图像属性部分
    		plot.setBackgroundPaint(Color.white);
    		plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
    		plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
    		plot.setRangeGridlinePaint(Color.GRAY);
    		plot.setNoDataMessage("Data Not Available");//没有数据时显示的文字说明
		  
    		//X轴
    		DateAxis dateaxis = (DateAxis) plot.getDomainAxis();
    		dateaxis.setAutoTickUnitSelection(true);
    		dateaxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            //Y轴
    		NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
    		numberaxis.setAutoTickUnitSelection(true);
                  
			// 数据渲染部分,主要是对折线做操作
    		XYItemRenderer renderer = (XYItemRenderer )plot.getRenderer();
			renderer.setSeriesPaint(0, Color.blue);    //设置折线的颜色
			renderer.setBaseItemLabelsVisible(false);
			renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
			renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
			                ItemLabelAnchor.OUTSIDE1, TextAnchor.BASELINE_LEFT));
			renderer.setBaseItemLabelFont(new Font("Dialog", Font.PLAIN, 12));  //设置提示折点数据形状
			plot.setRenderer(renderer);
			
			XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)plot.getRenderer();
			r.setBaseShapesVisible(true);
			r.setBaseShapesFilled(true);
			r.setBaseItemLabelsVisible(false);
			
			// 创建文件输出流
			/*FileOutputStream fos_jpg = new FileOutputStream("./bloodSugarChart.png");
			ChartUtilities.writeChartAsJPEG(fos_jpg, chart, 720, 480);
			fos_jpg.close();*/
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ChartUtilities.writeChartAsJPEG(bos, chart, 720, 480);
			httpbenchmark.datasource = new ByteArrayDataSource(bos.toByteArray(), "image/jpeg");
			bos.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
    }

    public static void main(String[] args) {
    	for (int i = 0; i < 20; i++) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String ct = sdf.format(date);
			//System.err.println(ct);
			
			//DecimalFormat df=new DecimalFormat("0.00");
			//double elapsed = (System.currentTimeMillis() - easysocketbenchmark.start_time)/1000.0;
			//String qps = df.format((easysocketbenchmark.total_res/(elapsed)));
			httpbenchmark.qps_stat.put(ct, String.valueOf(1000+i));
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	new statchart().createChart();
    }
}
