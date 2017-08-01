package name.yuris.notesrealm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import name.yuris.notesrealm.R;
import name.yuris.notesrealm.manager.RealmManager;
import name.yuris.notesrealm.model.Note;

/**
 * Activity to edit note {@link Note}
 *
 * @author Yuri Nevenchenov on 7/31/2017.
 */

public class EditNoteActivity extends AppCompatActivity {

    private static final int INPUT_MIN_LENGTH = 3;

    private static final String EXTRA_NOTE_TITLE = "extra_note_title";
    private static final String EXTRA_NOTE_ID = "extra_note_id";
    private static final String EXTRA_NOTE_BODY = "extra_note_body";

    private Toolbar mToolbar;
    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private TextView mIdTextView;
    private FloatingActionButton mSaveButton;

    private String mNoteTitle;
    private String mNoteId;
    private String mNoteBody;

    private Realm mRealm;

    public static Intent createExplicitIntent(Context context,
                                              String noteTitle,
                                              String noteId,
                                              String noteBody) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        intent.putExtra(EXTRA_NOTE_BODY, noteBody);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        mRealm = new RealmManager(this).getRealm();
        getDataFromIntent();
        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            closeActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    //region private methods

    private void closeActivity() {
        if (dataWasNotChanged()) {
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.close_activity)
                    .setMessage(R.string.lose_edit_input_data)
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

    private boolean dataWasNotChanged() {
        return mNoteTitle.equals(mTitleEditText.getText().toString()) &&
                mNoteBody.equals(mBodyEditText.getText().toString());
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();

        mNoteTitle = bundle.getString(EXTRA_NOTE_TITLE);
        mNoteId = bundle.getString(EXTRA_NOTE_ID);
        mNoteBody = bundle.getString(EXTRA_NOTE_BODY);
    }

    private void initUI() {
        initToolbar();
        initViews();
        initButton();
    }


    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        mTitleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        mBodyEditText = (TextInputEditText) findViewById(R.id.body_edit_text);
        mIdTextView = (TextView) findViewById(R.id.id_text_view);

        mTitleEditText.setText(mNoteTitle);
        mBodyEditText.setText(mNoteBody);
        mIdTextView.setText(mNoteId);
    }

    private void initButton() {
        mSaveButton = (FloatingActionButton) findViewById(R.id.save_note_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataWasNotChanged()) {
                    finish();
                } else if (inputDataCorrect()) {
                    showSnackBar(v);
                } else {
                    showErrorMessage();
                }
            }
        });
    }

    private void showSnackBar(View view) {
        Snackbar.make(view, R.string.save_note_question, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNote();
                        finishActivityWithResultOK();
                    }
                }).show();
    }

    private boolean inputDataCorrect() {
        return mTitleEditText.getText().length() >= INPUT_MIN_LENGTH &&
                mBodyEditText.getText().length() >= INPUT_MIN_LENGTH;
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Поля должны содержать как минимум " + INPUT_MIN_LENGTH + "символов", Toast.LENGTH_LONG).show();
    }

    private void editNote() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note note = mRealm.where(Note.class)
                        .equalTo("id", mNoteId)
                        .findFirst();
                note.setTitle(mTitleEditText.getText().toString());
                note.setBody(mBodyEditText.getText().toString());
            }
        });
    }

    private void finishActivityWithResultOK() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NOTE_TITLE, mTitleEditText.getText().toString());
        intent.putExtra(EXTRA_NOTE_BODY, mBodyEditText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
    //endregion
}