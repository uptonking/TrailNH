package com.kingoo.nhtrail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.kingoo.nhtrail.R;
import com.kingoo.nhtrail.ui.MapActivity;

import java.util.List;
import java.util.Map;

/**
 * Description 自定义适配器类：将数据绑定到listview，给不同控件添加监听器
 * @author KINGOO
 * @date 2015/04/20
 */
public class CustomLayerAdapter extends BaseAdapter {

    Context context;
    List<Map<String,Object>> dataList;

    int lyrTiled=1,lyrRS=1,lyrNH=1,lyrMy=1;


    ArcGISTiledMapServiceLayer TiledLayer = MapActivity.mTiledLayer;
    ArcGISDynamicMapServiceLayer DynamicLayer = MapActivity.mDynamicLayer;
    ArcGISImageServiceLayer ImageLayer = MapActivity.mImageLayer;


    public CustomLayerAdapter(Context context,List<Map<String,Object>> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_layer,null,false);

            holder.imgLyr = (ImageView) convertView.findViewById(R.id.lyr_symbol);
            holder.tvLyr = (TextView) convertView.findViewById(R.id.lyr_name);
            holder.imgSwitch = (ImageView) convertView.findViewById(R.id.lyr_switch);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgLyr.setImageResource((Integer)dataList.get(position).get("lyr_symbol"));
        holder.tvLyr.setText((String)dataList.get(position).get("lyr_name"));
        holder.imgSwitch.setImageResource((Integer) dataList.get(position).get("lyr_switch"));

        holder.imgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(position){
                    case 0:
                        if(lyrTiled == 1){
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_off);
                            TiledLayer.setVisible(false);
                            lyrTiled = 0;
                        }else {
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_on);
                            TiledLayer.setVisible(true);
                            lyrTiled = 1;
                        }
                        break;
                    case 1:
                        if(lyrRS == 1){
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_off);
                            DynamicLayer.setVisible(false);
                            lyrRS = 0;
                        }else {
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_on);
                            DynamicLayer.setVisible(true);
                            lyrRS = 1;
                        }
                        break;
                    case 2:
                        if(lyrNH == 1){
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_off);
                            ImageLayer.setVisible(false);
                            lyrNH = 0;
                        }else {
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_on);
                            ImageLayer.setVisible(true);
                            lyrNH = 1;
                        }
                        break;
                    case 3:
                        if(lyrMy == 1){
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_off);
                            //mMapView.removeLayer(mTiledLayer);
                            lyrMy = 0;
                        }else {
                            holder.imgSwitch.setImageResource(R.drawable.layer_switch_on);
                            //mMapView.addLayer(mTiledLayer);
                            lyrMy = 1;
                        }
                        break;

                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        ImageView imgLyr;
        TextView tvLyr;
        ImageView imgSwitch;

    }
}