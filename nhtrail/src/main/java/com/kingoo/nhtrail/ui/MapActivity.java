package com.kingoo.nhtrail.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.action.IdentifyResultSpinner;
import com.esri.android.action.IdentifyResultSpinnerAdapter;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.kingoo.nhtrail.R;
import com.kingoo.nhtrail.news.NewsActivity;
import com.kingoo.nhtrail.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description 地图功能主类：地图浏览、定位、查询
 * @author KINGOO
 * @date 2015/4/11
 */
public class MapActivity extends Activity {

    static MapView mMapView;
    public static ArcGISTiledMapServiceLayer mTiledLayer;
    public static ArcGISDynamicMapServiceLayer mDynamicLayer;
    public static ArcGISImageServiceLayer mImageLayer;
    GraphicsLayer mGraphicsLayer,graphicsLayer;
    LocationManager locationManager;
    Location location;
    Point wgsPoint;
    Point mapPoint;
    PictureMarkerSymbol locationMarker;
    IdentifyParameters identifyParams;
    ProgressDialog progress;

    String mTiledURL,mDynamicURL,mImageURL;



    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    PopupWindow popupSearch;
    PopupWindow popupNavi,popupKits;
    


    Button btnLayerControl;

    EditText etSearch;

    Drawable image;

    //底部4大主菜单组件声明
    ImageView imgLocate;
    ImageView imgSearch;
    ImageView imgNavigation;
    ImageView imgKits;
    
    ImageView imgPopupSearch;
    ImageView imgPopNavi,imgPopRoutes,imgPopTrack,imgPopPlan;
    ImageView imgPopNews,imgPopWeather,imgPopServices,imgPopHelp,imgPopCharts,imgPopUser,imgPopSetting,imgPopExit;

    public static int isShowing = 1;

    EditText et1;
    Button btn1;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //利用ClientId去除水印
        ArcGISRuntime.setClientId("ZKjWWfssDkAUW4nu");

        btnLayerControl = (Button) findViewById(R.id.btn_layercontrol);

        
        imgLocate = (ImageView) findViewById(R.id.locate);
        imgSearch = (ImageView) findViewById(R.id.search);
        imgNavigation = (ImageView) findViewById(R.id.navi);
        imgKits = (ImageView) findViewById(R.id.kits);



        mMapView = (MapView) findViewById(R.id.map);
        mMapView.enableWrapAround(true);
        mMapView.setMapBackground(Color.parseColor("#ffffffd7"),Color.parseColor("#00ffffff"),2.0f,0.01f);

        mTiledURL = this.getResources().getString(R.string.url_mapall);
        mDynamicURL = this.getResources().getString(R.string.url_nhdatamap);
        mImageURL = this.getResources().getString(R.string.url_maprs);

        mTiledLayer=new ArcGISTiledMapServiceLayer(mTiledURL);
        mDynamicLayer=new ArcGISDynamicMapServiceLayer(mDynamicURL);
        mImageLayer = new ArcGISImageServiceLayer(mImageURL,null);

        mMapView.addLayer(mTiledLayer);
        mMapView.addLayer(mDynamicLayer);
        mMapView.addLayer(mImageLayer);

        //图层控制按钮的两种响应事件
        btnLayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowing == 1)
                {
                    addFragmentLayer();
                }else{
                    removeFragmentLayer();
                }
            }
        });

        identifyParams = new IdentifyParameters();

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v2) {

                identifyParams.setTolerance(20);
                identifyParams.setDPI(98);
                identifyParams.setLayerMode(IdentifyParameters.ALL_LAYERS);

                Point identifyPoint = mMapView.toMapPoint(v,v2);
                identifyParams.setGeometry(identifyPoint);
                identifyParams.setSpatialReference(mMapView.getSpatialReference());
                identifyParams.setMapHeight(mMapView.getHeight());
                identifyParams.setMapWidth(mMapView.getWidth());
                identifyParams.setReturnGeometry(false);

                Envelope env = new Envelope();
                mMapView.getExtent().queryEnvelope(env);
                identifyParams.setMapExtent(env);

                AsyncIdentifyTask myIdentifyTask = new AsyncIdentifyTask(identifyPoint);
                myIdentifyTask.execute(identifyParams);
            }
        });

        //查询过程
        et1= (EditText) findViewById(R.id.et1);
        btn1= (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = et1.getText().toString();

                String targetLayer = mDynamicURL+"/0";
                String where = "NAME like '%"+address+"%'";
                String[] queryArray = {targetLayer,where};
                AsyncQueryTask ayncQuery = new AsyncQueryTask();
                ayncQuery.execute(queryArray);
            }
        });



        imgLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locate();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupSearch!=null){
                    popupSearch.dismiss();

                }
                else{
                    View searchView = getLayoutInflater().inflate(R.layout.popup_search,null,false);
                    popupSearch = new PopupWindow(searchView, ViewGroup.LayoutParams.WRAP_CONTENT,50,false);
                    popupSearch.setAnimationStyle(R.style.AnimationPreview);

                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    //Toast.makeText(MapActivity.this, "" + location[0], Toast.LENGTH_SHORT).show();
                    popupSearch.showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1] - popupSearch.getHeight());
                }
        }});
        
        imgNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupNavi!=null){
                    popupNavi.dismiss();
                    popupNavi=null;
                }else {
                    View popupNaviView = getLayoutInflater().inflate(R.layout.popup_navi, null, false);
                    imgPopNavi = (ImageView) popupNaviView.findViewById(R.id.pop_navi);
                    imgPopRoutes = (ImageView) popupNaviView.findViewById(R.id.pop_routes);
                    imgPopTrack = (ImageView) popupNaviView.findViewById(R.id.pop_track);
                    imgPopPlan = (ImageView) popupNaviView.findViewById(R.id.pop_plan);

                    popupNavi = new PopupWindow(popupNaviView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupNavi.setBackgroundDrawable(new ColorDrawable(0x09000000));
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    popupNavi.showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1]-125);
                }
            }
        });

        imgKits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupKits!=null){
                    popupKits.dismiss();
                    popupKits=null;
                }else{
                    View popupKitsView = getLayoutInflater().inflate(R.layout.popup_kits,null,false);
                    imgPopNews = (ImageView) popupKitsView.findViewById(R.id.pop_news);
                    imgPopWeather = (ImageView) popupKitsView.findViewById(R.id.pop_weather);
                    imgPopServices = (ImageView) popupKitsView.findViewById(R.id.pop_services);
                    imgPopHelp = (ImageView) popupKitsView.findViewById(R.id.pop_help);
                    imgPopCharts = (ImageView) popupKitsView.findViewById(R.id.pop_charts);
                    imgPopUser = (ImageView) popupKitsView.findViewById(R.id.pop_user);
                    imgPopSetting = (ImageView) popupKitsView.findViewById(R.id.pop_setting);
                    imgPopExit = (ImageView) popupKitsView.findViewById(R.id.pop_exit);

                    imgPopNews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MapActivity.this, NewsActivity.class));
                        }
                    });
                    imgPopWeather.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MapActivity.this, WeatherActivity.class));
                        }
                    });
                    imgPopExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });

                    popupKits = new PopupWindow(popupKitsView, TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    popupKits.setBackgroundDrawable(new ColorDrawable(0x09000000));
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    popupKits.showAtLocation(v,Gravity.NO_GRAVITY,0,location[1]-250);
                }
            }
        });


    }


    /**
     * Description 方法：填充callout的布局容器，将adapter注册到spinner上
     * @param resultList 传入参数：识别结果数据
     * @return ViewGroup 返回布局对象
     */
    private ViewGroup createIdentifyContent(List<IdentifyResult> resultList) {

        LinearLayout layout = new LinearLayout(this);

        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        layout.setOrientation(LinearLayout.HORIZONTAL);

        IdentifyResultSpinner spinner = new IdentifyResultSpinner(this, resultList);//实例化显示结果的spinner

        spinner.setClickable(false);
        spinner.canScrollHorizontally(BIND_ADJUST_WITH_ACTIVITY);

        MyIdentifyAdapter adapter = new MyIdentifyAdapter(this, resultList);//用识别结果初始化自定义的adapter
        spinner.setAdapter(adapter);//将adapter绑定到spinner上
        spinner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(spinner);//将spinner添加到线性布局

        return layout;
    }

    /**
     * Description 方法：动态添加图层控制的fragment
     */
    private void addFragmentLayer() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LayerControlFragment layerControlFragment = new LayerControlFragment();
        fragmentTransaction.add(R.id.linear_fragment,layerControlFragment, "fragment_layer");
        fragmentTransaction.commit();
        btnLayerControl.setBackgroundResource(R.drawable.layer_close);
        isShowing = 0;
    }

    /**
     * Description 方法：移除图层控制的fragment
     */
    private void removeFragmentLayer(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("fragment_layer");
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        btnLayerControl.setBackgroundResource(R.drawable.btn_layercontrol);
        isShowing = 1;
    }

    /**
     * Description 方法：定位
     */
    public void Locate(){
        //获取locationManager实例
        locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //provider列表的实例
        final List<String> providers=locationManager.getProviders(true);
        for(String provider:providers){
            location=locationManager.getLastKnownLocation(provider);

            //声明位置服务监听器
            LocationListener ll=new LocationListener() {

                //位置改变时调用
                @Override
                public void onLocationChanged(Location location) {

                    markLocation(location);
                }

                //状态改变时调用
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                //provider失效时调用
                @Override
                public void onProviderEnabled(String provider) {

                }

                //provider生效时调用
                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(provider, 5000, 0, ll);
            if(location!=null)
            {
                //在地图上标记出位置
                markLocation(location);
            }
        }
    }

    /**
     * Description 方法：标记出定位
     * @param location 定位点
     * @return void
     */
    private void markLocation(Location location) {

        //将GPS获得的经纬度投影到地图坐标系上
        double lon_gps=location.getLongitude();
        double lat_gps=location.getLatitude();

        double[] lonlat={0.0,0.0};
        com.kingoo.nhtrail.GpsCorrect gpsCorrect = new com.kingoo.nhtrail.GpsCorrect();
        gpsCorrect.transform(lat_gps, lon_gps, lonlat);
        wgsPoint=new Point(lonlat[1],lonlat[0]);

        mapPoint=(Point) GeometryEngine.project(wgsPoint, SpatialReference.create(4326), mMapView.getSpatialReference());

        mGraphicsLayer=new GraphicsLayer();
        com.kingoo.nhtrail.DrawLocation drawLocation = new com.kingoo.nhtrail.DrawLocation();
        drawLocation.drawPoint(mapPoint, 0.01, mGraphicsLayer,Color.parseColor("#0099FF"), Color.parseColor("#0099FF"));

        mMapView.addLayer(mGraphicsLayer);
        mMapView.centerAt(mapPoint,true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }


    /**
     * Decription 自定义spinner适配器类：将识别结果数据绑定到callout的spinner上
     * @author KINGOO
     * @date 2014/4/11
     * @see com.esri.android.action.IdentifyResultSpinnerAdapter
     * */
    private class MyIdentifyAdapter extends IdentifyResultSpinnerAdapter{

        List<IdentifyResult> resultList;
        Context m_context;

        /**
         * Description 构造函数
         * @param context 当前activity实例
         * @param results 识别结果列表
         */
        public MyIdentifyAdapter(Context context, List<IdentifyResult> results) {
            super(context, results);
            this.resultList = results;
            this.m_context = context;
        }

        // Get a TextView that displays identify results in the callout.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String LSP = System.getProperty("line.separator");//获取系统的行分隔符
            StringBuilder outputVal = new StringBuilder();

            // Resource Object to access the Resource fields
            Resources res = getResources();

            // Get Name attribute from identify results
            IdentifyResult curResult = this.resultList.get(position);

            if (curResult.getAttributes().containsKey(
                    res.getString(R.string.OBJECTID))) {
                outputVal.append("ObjectID: "
                        + curResult.getAttributes()
                        .get(res.getString(R.string.OBJECTID)).toString());
                outputVal.append(LSP);
            }

            if (curResult.getAttributes().containsKey(
                    res.getString(R.string.NAME))) {
                outputVal.append("Name: "
                        + curResult.getAttributes()
                        .get(res.getString(R.string.NAME)).toString());
                outputVal.append(LSP);
            }

            if (curResult.getAttributes().containsKey(
                    res.getString(R.string.TYPE))) {
                outputVal.append("Type: "
                        + curResult.getAttributes()
                        .get(res.getString(R.string.TYPE))
                        .toString());
                outputVal.append(LSP);
            }

            //创建并实例化显示结果的TextView
            TextView txtView = new TextView(this.m_context);
            txtView.setText(outputVal);
            txtView.setTextColor(Color.BLACK);
            txtView.setLayoutParams(new ListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            txtView.setGravity(Gravity.CENTER_VERTICAL);

            return txtView;
        }
    }


    /**
     * Description 自定义异步识别类：实现点击地图，显示属性信息
     * @author KINGOO
     * @date 2014/04/11
     */
    private class AsyncIdentifyTask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]>{

        IdentifyTask identifyTask;
        IdentifyResult[] M_Result;
        Point mAnchor;

        /**
         * Description 构造函数：初始化化Identify Task实例，传入的点
         * @param anchorPoint 传入的点
         */
        AsyncIdentifyTask(Point anchorPoint){
            identifyTask = new IdentifyTask("http://www.nbmap.gov.cn/ArcGIS/rest/services/NHDATA/MapServer");
            mAnchor = anchorPoint;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(MapActivity.this, "Identify Task",
                    "Identify query ...");
        }

        @Override
        protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
            if (params != null && params.length > 0) {
                IdentifyParameters mParams = params[0];//？？？

                try {
                    // Run IdentifyTask with Identify Parameters

                    M_Result = identifyTask.execute(mParams);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return M_Result;
        }

        @Override
        protected void onPostExecute(IdentifyResult[] identifyResults) {
            super.onPostExecute(identifyResults);

            if(progress.isShowing()) {
                progress.dismiss();
            }
            ArrayList<IdentifyResult> resultList = new ArrayList<IdentifyResult>();

            IdentifyResult result_1;

            for (IdentifyResult identifyResult : identifyResults) {

                result_1 = identifyResult;//将结果数据逐一赋值给result_1
                String displayFieldName = result_1.getDisplayFieldName();//返回识别要素的名称
                Map<String, Object> attr = result_1.getAttributes();//返回属性键值
                for (String key : attr.keySet()) {
                    if (key.equalsIgnoreCase(displayFieldName)) {
                        resultList.add(result_1);//遍历map中的键值，将识别结果属性值加入链表
                    }
                }
            }

            Callout callout = mMapView.getCallout();
            callout.setContent(createIdentifyContent(resultList));//填充callout的布局
            callout.show(mAnchor);//在指定的点处显示标注弹出窗口

        }
    }


    /**
     * Description 自定义异步查询类：实现查询，显示高亮结果
     * @author KINGOO
     * @date 2014/4/11
     */
    private class AsyncQueryTask extends AsyncTask<String, Void, FeatureResult> {

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MapActivity.this);

            progress = ProgressDialog.show(MapActivity.this, "",
                    "Please wait....query task is executing");

        }

        @Override
        protected FeatureResult doInBackground(String... queryArray) {

            if (queryArray == null || queryArray.length <= 1)
                return null;

            String url = queryArray[0];
            QueryParameters qParameters = new QueryParameters();
            String whereClause = queryArray[1];
            SpatialReference sr = SpatialReference.create(4326);
            qParameters.setGeometry(mMapView.getExtent());
            qParameters.setOutSpatialReference(sr);
            qParameters.setReturnGeometry(true);
            qParameters.setWhere(whereClause);

            QueryTask qTask = new QueryTask(url);
            FeatureResult results;

            try {
                results = qTask.execute(qParameters);
                return results;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(FeatureResult results) {

            String message = "No result comes back";

            if (results != null) {
                int size = (int) results.featureCount();
                for (Object element : results) {
                    progress.incrementProgressBy(size / 100);
                    if (element instanceof Feature) {

                        Feature feature = (Feature) element;
                        // turn feature into graphic
                        Graphic graphic = new Graphic(feature.getGeometry(),
                                feature.getSymbol(), feature.getAttributes());

                        graphicsLayer = new GraphicsLayer();
                        SimpleRenderer sr = new SimpleRenderer(new SimpleFillSymbol(Color.CYAN));
                        graphicsLayer.setRenderer(sr);
                        // add graphic to layer
                        graphicsLayer.addGraphic(graphic);


                    }
                }
                // update message with results
                message = String.valueOf(results.featureCount())
                        + " results have returned from query.";

            }
            progress.dismiss();
            Toast toast = Toast.makeText(MapActivity.this, message,
                    Toast.LENGTH_LONG);
            toast.show();


        }

    }
}