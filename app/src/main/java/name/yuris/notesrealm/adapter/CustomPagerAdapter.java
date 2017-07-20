package name.yuris.notesrealm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import name.yuris.notesrealm.BasicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuri Nevenchenov on 7/18/2017.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private List<BasicFragment> mFragmentList;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.isEmpty() ? null : mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }

    public void addFragment(BasicFragment fragment) {
        mFragmentList.add(fragment);
        notifyDataSetChanged();
    }
}
