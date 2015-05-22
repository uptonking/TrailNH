package com.kingoo.nhtrail.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kingoo.nhtrail.R;
import com.kingoo.nhtrail.adapter.CustomLayerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description 自定义布局类：图层控制弹出菜单fragment
 * @author KINGOO
 * @date 2014/4/11
 */
public class LayerControlFragment extends ListFragment {

    ListView list;
    List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
    CustomLayerAdapter customLayerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layer_control,container,false);
        list = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();

    }

    /**
     * Description 方法：初始化fragment
     */
    private void initListView() {

        int[] intLyr = new int[]{R.drawable.icon_map_tiled, R.drawable.icon_map_rs, R.drawable.icon_map_trail, R.drawable.icon_map_fav, R.drawable.layer_switch_on};
        String[] strLyrName = new String[]{"2D平面图","卫星影像图","登山专题图","我的收藏点"};

        for(int i=0;i<4;i++){
            HashMap<String,Object> map=new HashMap<String,Object>();
            map.put("lyr_symbol",intLyr[i]);
            map.put("lyr_name",strLyrName[i]);
            map.put("lyr_switch",intLyr[4]);
            dataList.add(map);
        }

        customLayerAdapter = new CustomLayerAdapter(getActivity(), dataList);
        setListAdapter(customLayerAdapter);


    }


}
