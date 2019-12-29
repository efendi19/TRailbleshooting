package com.tehkotak.trailbleshooting;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    //private final List<String> fragmentListTitles = new ArrayList<>();
    private final List<Integer> fragmentIconList = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return fragmentListTitles.get(position);
        return null;
    }

    public void AddFragment(Fragment fragment, String Title, int tabIcon) {
        fragmentList.add(fragment);
        //fragmentListTitles.add(Title);
        fragmentIconList.add(tabIcon);
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        /*TextView tabTextView = view.findViewById(R.id.tv_tab);
        tabTextView.setText(fragmentListTitles.get(position));*/
        ImageView tabImageView = view.findViewById(R.id.img_tab);
        tabImageView.setImageResource(fragmentIconList.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        /*TextView tabTextView = view.findViewById(R.id.tv_tab);
        tabTextView.setText(fragmentListTitles.get(position));
        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));*/
        ImageView tabImageView = view.findViewById(R.id.img_tab);
        tabImageView.setImageResource(fragmentIconList.get(position));
        return view;
    }
}
