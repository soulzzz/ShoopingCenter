package com.xmut.shoppingcenter.utils.fragmentUtil;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPageAdatper extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private FragmentManager fragmentManager;
    public MyFragmentPageAdatper(FragmentManager fm,List<Fragment> fragments){
        super(fm);
        this.fragmentManager = fm;
        mFragments=fragments;
    }

    public MyFragmentPageAdatper(FragmentManager fm) {
        super(fm);
    }

    public List<Fragment> getmFragments() {
        return mFragments;
    }

    public void setmFragments(List<Fragment> mFragments) {
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if(fragmentManager ==null){
            System.out.println("老子空了");
        }
        fragmentManager.beginTransaction().show(mFragments.get(position)).commit();
        return mFragments.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = mFragments.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
