package name.yuris.notesrealm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import name.yuris.notesrealm.adapter.CustomPagerAdapter;
import name.yuris.notesrealm.manager.RealmManager;
import name.yuris.notesrealm.model.Category;

public class MainActivity extends AppCompatActivity {

    private static final int CATEGORY_NAME_MIN_LENGTH = 3;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;


    private CustomPagerAdapter mAdapter;
    private Realm mRealm;

    //region Activity LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();
        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mRealm = new RealmManager(this).getRealm();
        fullAdapter();
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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void addNewCategory() {
        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.create_dialog, null);
        final EditText editText = (EditText) view.findViewById(R.id.input_category_edit_text);
        new AlertDialog.Builder(this)
                .setTitle(R.string.input_category_name)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().length() >= CATEGORY_NAME_MIN_LENGTH) {
                            saveCategoryName(editText.getText().toString().toLowerCase());
                            fullAdapter();
                        } else {
                            Toast.makeText(getApplicationContext(), "Минимум " + CATEGORY_NAME_MIN_LENGTH + " символов", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(view)
                .show();
    }

    private void saveCategoryName(String categoryName) {
        boolean contains = false;
        for (Category category : mRealm.allObjects(Category.class)) {
            if (category.getCategoryName().equals(categoryName)) {
                contains = true;
            }
        }
        if (contains) {
            Toast.makeText(getApplicationContext(), categoryName + " уже содержится", Toast.LENGTH_LONG).show();
        } else {
            mRealm.beginTransaction();
            Category category = mRealm.createObject(Category.class);
            category.setCategoryName(categoryName);
            mRealm.commitTransaction();
        }
    }

    private void fullAdapter() {
        mAdapter.clear();
        List<BasicFragment> fragments = new ArrayList<>();
        for (Category category : mRealm.allObjects(Category.class)) {
            fragments.add(BasicFragment.newInstance().setTitle(category.getCategoryName()));
        }
        mAdapter.addFragments(fragments);
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

    //endregion
}