package name.yuris.notesrealm.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import io.realm.Realm;
import name.yuris.notesrealm.R;
import name.yuris.notesrealm.manager.RealmManager;

/**
 * Abstract class for other activities
 *
 * @author Yuri Nevenchenov on 8/1/2017.
 */

public abstract class BaseNoteActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    protected static final String EXTRA_NOTE_TITLE = "extra_note_title";
    protected static final String EXTRA_NOTE_ID = "extra_note_id";
    protected static final String EXTRA_NOTE_BODY = "extra_note_body";
    protected static final int INPUT_MIN_LENGTH = 3;

    protected Toolbar mToolbar;
    protected TextView mNoteIdTextView;
    protected TextInputEditText mTitleEditText;
    protected TextInputEditText mBodyEditText;
    protected FloatingActionButton mMainButton;
    protected FloatingActionButton mSecondButton;

    protected String mCategoryName;
    protected String mNoteTitle;
    protected String mNoteId;
    protected String mNoteBody;

    protected Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);
        mRealm = new RealmManager(this).getRealm();
        getDataFromIntent();
        initUI();
        hideKeyboard(isEditTextEnabled());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleActivityClosing();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        handleActivityClosing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_button:
                onMainButtonClick(v);
                break;
            case R.id.delete_note_button:
                onSecondButtonClick(v);
                break;
        }
    }

    //region protected methods

    /**
     * Define in inherited classes to handle logic before closing activity
     */
    protected abstract void handleActivityClosing();

    /**
     * Define in inherited classes to get data from intent
     */
    protected abstract void getDataFromIntent();

    /**
     * Redefine in inherited class if need to change visibility
     *
     * @return visibility of note id textView
     */
    protected int getNoteIdVisibility() {
        return View.VISIBLE;
    }

    /**
     * Redefine in inherited class if need to change visibility
     *
     * @return visibility of note id textView
     */
    protected int getSecondButtonVisibility() {
        return View.GONE;
    }

    /**
     * Define in inherited classes to handle main button click
     * @param view View
     */
    protected abstract void onMainButtonClick(View view);

    /**
     * Define in inherited class if need to handle second button click event
     *
     * @param view View
     */
    protected void onSecondButtonClick(View view) {
        //implement in inherited classes if needed
    }

    /**
     * Define in inherited classes to handle text inputs enabled flag
     *
     * @return flag, true to be enabled or false
     */
    protected boolean isEditTextEnabled() {
        return true;
    }

    /**
     * Redefine in inherited classes to change drawable
     *
     * @return drawable of the main button
     */
    @IdRes
    protected int getMainButtonImageResource() {
        return android.R.drawable.ic_menu_save;
    }

    //endregion

    //region private methods

    private void initUI() {
        initToolbar();
        initViews();
        initButtons();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        mTitleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        mBodyEditText = (TextInputEditText) findViewById(R.id.body_edit_text);
        mNoteIdTextView = (TextView) findViewById(R.id.note_id_text_view);
        mNoteIdTextView.setVisibility(getNoteIdVisibility());
        mTitleEditText.setEnabled(isEditTextEnabled());
        mBodyEditText.setEnabled(isEditTextEnabled());
    }

    private void initButtons() {
        mMainButton = (FloatingActionButton) findViewById(R.id.main_button);
        mSecondButton = (FloatingActionButton) findViewById(R.id.delete_note_button);
        mSecondButton.setVisibility(getSecondButtonVisibility());
        mMainButton.setOnClickListener(this);
        mSecondButton.setOnClickListener(this);
        mMainButton.setImageResource(getMainButtonImageResource());
    }

    private void hideKeyboard(boolean editTextFieldEnabled) {
        if(!editTextFieldEnabled){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTitleEditText.getWindowToken(), 0);
            mTitleEditText.setFocusable(false);
            mBodyEditText.setFocusable(false);
        }
    }

    //endregion
}