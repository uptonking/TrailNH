package com.kingoo.nhtrail.news;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;

import com.kingoo.nhtrail.R;
import com.viewpagerindicator.PageIndicator;

/**
 * description 新闻主页
 * @author Administrator
 * @date 2015/5/22
 */
public class NewsActivity extends FragmentActivity {

    ImageButton moreButton;

    Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentPagerAdapter adapter = new TabAdapter(
                getSupportFragmentManager());

        // 视图切换器
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter(adapter);

        // 页面指示器
        PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }


}
