package com.kingoo.nhtrail;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * Description 自定义定位绘图类：将坐标点绘制在图层上显示出来,还显示一个误差圆
 * @author KINGOO
 * @date 2015/4/12
 */
public class DrawLocation {
    private static Graphic graphicCenterPoint;
    private static Graphic graphicErrorRound;

    /**
     * Description 方法：以点状符号绘制坐标点
     * @param center 中心点
     * @param radius 误差圆半径（单位：米）
     * @param graphicsLayer 绘制图层
     *
     * @param errorRoundColorFill 误差圆颜色
     * @param errorRoundColorLine 误差圆边线颜色
     */
    public static void drawPoint(Point center, double radius, GraphicsLayer graphicsLayer,int errorRoundColorFill, int errorRoundColorLine) {

        /* 清除图层 */
        if(graphicsLayer != null){
            graphicsLayer.removeAll();
        }

        /* 绘制中心点 */
       /* PictureMarkerSymbol symbol = new PictureMarkerSymbol(centerImage);
        symbol.setOffsetY(10.0f);*/
        graphicCenterPoint = new Graphic(center, new SimpleMarkerSymbol(Color.CYAN, 10, SimpleMarkerSymbol.STYLE.CIRCLE));

        /* 绘制误差圆 */
        if(radius > 0){

            Polygon polygon = new Polygon();
            getCircle(center, radius, polygon);//调用方法，获取圆对象
            FillSymbol fillSymbol = new SimpleFillSymbol(errorRoundColorFill);//实例化填充符号对象
            fillSymbol.setAlpha(4);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(errorRoundColorLine, 0.05f, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setOutline(lineSymbol);//设置填充符合对象的轮廓
            graphicErrorRound = new Graphic(polygon, fillSymbol);

        }

        /* 绘制上图 */
        graphicsLayer.addGraphics(new Graphic[]{graphicCenterPoint, graphicErrorRound});

    }

    /**
     * 获取圆的图形对象
     *
     * @param center
     * @param radius
     * @return
     */
    public static Polygon getCircle(Point center, double radius) {
        Polygon polygon = new Polygon();
        getCircle(center, radius, polygon);
        return polygon;
    }

    /**
     * Description 方法：通过绘制线段来绘制圆
     * @param center 中心点
     * @param radius 半径（米）
     * @param circle 圆的图形对象
     */
    private static void getCircle(Point center, double radius, Polygon circle) {
        circle.setEmpty();
        Point[] points = getPoints(center, radius);
        circle.startPath(points[0]); //以给定的点为起点开始绘制新路径
        for (int i = 1; i < points.length; i++)
            circle.lineTo(points[i]);//以给定的点为终点绘制一条线段
    }

    /**
     * Description 方法：通过中心点和半径计算得出圆形的边线点集合
     * @param center 中心点
     * @param radius 半径
     * @return Points[] 圆的边缘点数组
     */
    private static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }
}
