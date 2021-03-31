package com.runner.server.service.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class ChartsUtil {


    public static Color[] CHART_COLORS = {new Color(31, 129, 188), new Color(92, 92, 97), new Color(144, 237, 125), new Color(255, 188, 117),
            new Color(153, 158, 255), new Color(144, 238, 144), new Color(253, 236, 109), new Color(128, 133, 232), new Color(158, 90, 102),
            new Color(255, 204, 102), new Color(47, 79, 79), new Color(124, 205, 124), new Color(255, 246, 143), new Color(238, 99, 99),
            new Color(255, 130, 71), new Color(225, 0, 255), new Color(238, 154, 0), new Color(255, 69, 0), new Color(255, 222, 173)};

    /**
     * 2D饼图
     *
     * @param title    图形的标题
     * @param dataset  数据集对象
     * @param legend   显示标题
     * @param tooltips 启用热键
     * @param urls     启用超键接
     * @return
     */
    private static JFreeChart get2DPieChart(String title, DefaultPieDataset dataset, boolean legend, boolean tooltips, boolean urls) {
        JFreeChart chart = ChartFactory.createPieChart(title, dataset, legend, tooltips, urls);
        PiePlot plot = (PiePlot) chart.getPlot();
        String unitSytle = "{0}={1}({2})";
        plot.setNoDataMessage("无对应的数据，请重新查询。");
        plot.setNoDataMessagePaint(Color.red);
        // 引出标签显示样式
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(unitSytle, NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        // 图例显示样式
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(unitSytle, NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));

        //设置不同饼图颜色
        plot.setSectionPaint("成功数",new Color(144, 238 ,144));
        plot.setSectionPaint("失败数",new Color(238,99 ,99));
        plot.setSectionPaint("异常数",new Color(255,228,181));
        return chart;
    }

    /**
     * 3D饼图
     *
     * @param title
     * @param dataset
     * @return
     */
    public static JFreeChart get3DPieChart(String title, DefaultPieDataset dataset, boolean legend, boolean tooltips, boolean urls) {
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, legend, tooltips, urls);
        PiePlot plot = (PiePlot) chart.getPlot();
        String unitSytle = "{0}={1}({2})";
        plot.setNoDataMessage("无对应的数据，请重新查询。");
        plot.setNoDataMessagePaint(Color.red);
        // 指定 section 轮廓线的厚度(OutlinePaint不能为null)
        plot.setOutlineStroke(new BasicStroke(0));
        // 设置第一个 section 的开始位置，默认是12点钟方向
        plot.setStartAngle(90);
        plot.setToolTipGenerator(new StandardPieToolTipGenerator(unitSytle, NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        // 指定图片的透明度 0.5F为半透明，1为不透明，0为全透明
        plot.setForegroundAlpha(1);
        // 引出标签显示样式
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(unitSytle, NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        // 图例显示样式
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(unitSytle, NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
        return chart;
    }

    /**
     * 垂直柱状图(比较型)
     *
     * @param title    图形的标题
     * @param xAxis    x轴标题
     * @param yAxis    y轴标题
     * @param dataset  数据集对象
     * @param legend   显示标题
     * @param tooltips 启用热键
     * @param urls     启用超键接
     * @return
     */
    public static JFreeChart get3DBarChart(String title, String xAxis, String yAxis, CategoryDataset dataset, boolean legend, boolean tooltips,
                                           boolean urls) {
        JFreeChart chart = ChartFactory.createBarChart3D(title, xAxis, yAxis, dataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 显示每个柱的数值，并修改该数值的字体属性
        BarRenderer3D renderer = new BarRenderer3D();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        // 默认的数字显示在柱子中，通过如下两句可调整数字的显示
        // 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setItemLabelAnchorOffset(10D);
        // 设置每个地区所包含的平行柱的之间距离
        // renderer.setItemMargin(0.3);
        plot.setRenderer(renderer);
        // 设置地区、销量的显示位置
        // 将x轴显示在上方
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        // 将y轴显示在右方
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        CategoryAxis domainAxis = plot.getDomainAxis(); // 获取X 轴
        // 下面设置解决中文乱码问题
//        domainAxis.setLabelFont(FONT); // 设置X轴的标题文字
//        domainAxis.setTickLabelFont(new Font("黑体", Font.BOLD, 12));// 设置X轴坐标上的文字
//        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); // 获取y轴
//        rangeAxis.setLabelFont(FONT); // 设置y轴的标题文字
//        chart.getTitle().setFont(FONT); // 设置标题文字
//        chart.getLegend().setItemFont(FONT); // 设置底部中文乱码
        return chart;
    }

    /**
     * 曲线图
     *
     * @param title    图形的标题
     * @param xAxis    x轴标题
     * @param yAxis    y轴标题
     * @param dataset  数据集对象
     * @param legend   显示标题
     * @param tooltips 启用热键
     * @param urls     启用超键接
     * @param showItem 是否显示各数据点及其数值
     * @return
     */
    public static JFreeChart getLineChart(String title, String xAxis, String yAxis, TimeSeriesCollection dataset, boolean legend, boolean tooltips,
                                          boolean urls, boolean showItem) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, xAxis, yAxis, dataset, legend, tooltips, urls);
        XYPlot plot = (XYPlot) chart.getPlot();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 设置曲线图与xy轴的距离
        plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 0D));
        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        plot.getDomainAxis().setLowerMargin(0.05);
        plot.getDomainAxis().setUpperMargin(0.05);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        if (showItem) {
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) plot.getRenderer();
            // 设置曲线是否显示数据点
            xylineandshaperenderer.setBaseShapesVisible(true);
            // 设置曲线显示各数据点的值
            XYItemRenderer xyitem = plot.getRenderer();
            xyitem.setBaseItemLabelsVisible(true);
            xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
            xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 10));
            plot.setRenderer(xyitem);
        }
        return chart;
    }

    /**
     * 曲线图(非时序图)
     *
     * @param title    图形的标题
     * @param xAxis    x轴标题
     * @param yAxis    y轴标题
     * @param dataset  数据集对象
     * @param plotor
     * @param legend   显示标题
     * @param tooltips 启用热键
     * @param urls     启用超键接
     * @param showItem 是否显示各数据点及其数值
     * @return
     */
    public static JFreeChart getUnequallyLineChart(String title, String xAxis, String yAxis, DefaultCategoryDataset dataset, PlotOrientation plotor,
                                                   boolean legend, boolean tooltips, boolean urls, boolean showItem) {
        JFreeChart chart = ChartFactory.createLineChart(title, xAxis, yAxis, dataset, plotor, tooltips, urls, showItem);
        CategoryPlot plot = chart.getCategoryPlot();
        // 设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        // 设置网格竖线颜色
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.pink);
        // 设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);
        // 设置曲线图与xy轴的距离
        plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));
        if (showItem) {
            LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) plot.getRenderer();
            // 设置曲线是否显示数据点
            lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            lineandshaperenderer.setBaseItemLabelsVisible(true);
            // 设置曲线显示各数据点的值
            CategoryItemRenderer xyitem = plot.getRenderer();
            xyitem.setBaseItemLabelsVisible(true);
            xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
            xyitem.setBaseItemLabelFont(new Font("Dialog", 1, 10));
            plot.setRenderer(xyitem);
        }
        return chart;
    }

    /**
     * 中文主题样式 解决乱码
     */
    public static void setChartTheme() {
        // 设置中文主题样式 解决乱码
//        StandardChartTheme chartTheme = new StandardChartTheme("CN");
//        // 设置标题字体
//        chartTheme.setExtraLargeFont(FONT);
//        // 设置图例的字体
//        chartTheme.setRegularFont(FONT);
//        // 设置轴向的字体
//        chartTheme.setLargeFont(FONT);
//        chartTheme.setSmallFont(FONT);


        //创建主题样式
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        //设置标题字体
        chartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 15));
        //设置图例的字体
        chartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 12));
        //设置轴向的字体
        chartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));


        chartTheme.setTitlePaint(new Color(51, 51, 51));
        chartTheme.setSubtitlePaint(new Color(85, 85, 85));
        chartTheme.setLegendBackgroundPaint(Color.WHITE);// 设置标注
        chartTheme.setLegendItemPaint(Color.BLACK);//
        chartTheme.setChartBackgroundPaint(Color.WHITE);
        // 绘制颜色绘制颜色.轮廓供应商
        // paintSequence,outlinePaintSequence,strokeSequence,outlineStrokeSequence,shapeSequence
        Paint[] OUTLINE_PAINT_SEQUENCE = new Paint[]{Color.WHITE};
        // 绘制器颜色源
        DefaultDrawingSupplier drawingSupplier = new DefaultDrawingSupplier(CHART_COLORS, CHART_COLORS, OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE);
        chartTheme.setDrawingSupplier(drawingSupplier);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);// 绘制区域
        chartTheme.setPlotOutlinePaint(Color.WHITE);// 绘制区域外边框
        chartTheme.setLabelLinkPaint(new Color(8, 55, 114));// 链接标签颜色
        chartTheme.setLabelLinkStyle(PieLabelLinkStyle.CUBIC_CURVE);
        chartTheme.setAxisOffset(new RectangleInsets(5, 12, 5, 12));
        chartTheme.setDomainGridlinePaint(new Color(192, 208, 224));// X坐标轴垂直网格颜色
        chartTheme.setRangeGridlinePaint(new Color(192, 192, 192));// Y坐标轴水平网格颜色
        chartTheme.setBaselinePaint(Color.WHITE);
        chartTheme.setCrosshairPaint(Color.BLUE);// 不确定含义
        chartTheme.setAxisLabelPaint(new Color(51, 51, 51));// 坐标轴标题文字颜色
        chartTheme.setTickLabelPaint(new Color(67, 67, 72));// 刻度数字
        chartTheme.setBarPainter(new StandardBarPainter());// 设置柱状图渲染
        chartTheme.setXYBarPainter(new StandardXYBarPainter());// XYBar 渲染
        chartTheme.setItemLabelPaint(Color.black);
        chartTheme.setThermometerPaint(Color.white);// 温度计
        ChartFactory.setChartTheme(chartTheme);
    }

    /**
     * @Description: 方便获取2D饼图
     */
    public static JFreeChart get2DPieChart(String title, DefaultPieDataset dataset, String path) throws Exception {
        // 设置主题
        ChartsUtil.setChartTheme();
        // 获取chart
        JFreeChart chart = ChartsUtil.get2DPieChart(title, dataset, false, true, true);
        // 保存图片
        ChartUtilities.saveChartAsPNG(new File(path), chart, 350, 250);
        return chart;
    }

    public static JFreeChart getLineChartPic(String title, String xDesc, String yDesc, DefaultCategoryDataset dataset, String path) throws Exception {
        // 设置主题
        ChartsUtil.setChartTheme();
        JFreeChart lineChart = getUnequallyLineChart(title, xDesc, yDesc, dataset, PlotOrientation.VERTICAL, true, true, true, true);
        // 保存图片
        ChartUtilities.saveChartAsPNG(new File(path), lineChart, 900, 550);
        return lineChart;
    }


}
