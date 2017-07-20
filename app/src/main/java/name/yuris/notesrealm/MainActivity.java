package name.yuris.notesrealm;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import name.yuris.notesrealm.adapter.CustomPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
   private CustomPagerAdapter mAdapter;

    private int mCount = 0;

    //region Activity LifeCycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case android.R.id.home:
                        closeApp();
                        return true;
                case R.id.new_category:
                        addNewCategory();
                        return true;
            }
        return false;
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }

    //endregion

    //region private methods

    private void initUI() {
        initToolbar();
        initViewPager();
        initTabLayout();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mAdapter);
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void addNewCategory() {
        mAdapter.addFragment(BasicFragment.newInstance("Title " + mCount));
        mCount++;
        mViewPager.setAdapter(mAdapter);
    }

    private void closeApp() {
        new AlertDialog.Builder(this)
                        .setTitle(R.string.exit_app)
                        .setMessage(R.string.are_you_sure)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                })
                        .show();
    }
}
