package name.yuris.notesrealm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import name.yuris.notesrealm.R;
import name.yuris.notesrealm.model.Category;
import name.yuris.notesrealm.model.Note;

/**
 * Activity to create and save note {@link Note}
 *
 * @author Yuri Nevenchenov on 7/23/2017.
 */

public class CreateNoteActivity extends BaseNoteActivity {

    public static Intent createExplicitIntent(Context context, String categoryName) {
        Intent intent = new Intent(context, CreateNoteActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        return intent;
    }

    //region protected methods

    @Override
    protected int getNoteIdVisibility() {
        return View.GONE;
    }

    @Override
    protected void handleActivityClosing() {
        if (inputDataEmpty()) {
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.close_activity)
                    .setMessage(R.string.lose_input_data)
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

    @Override
    protected void getDataFromIntent() {
        mCategoryName = getIntent().getExtras().getString(EXTRA_CATEGORY_NAME);
    }

    @Override
    protected void onMainButtonClick(View view) {
        saveNote(view);
    }

    //endregion

    //region private methods

    private boolean inputDataEmpty() {
        return mTitleEditText.getText().length() == 0 &&
                mBodyEditText.getText().length() == 0;
    }

    private void saveNote(View view) {
        if (inputDataCorrect()) {
            Snackbar.make(view, R.string.save_note_question, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Note note = fillNoteData();
                            saveNoteToRealm(note);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).show();
        } else {
            showErrorMessage();
        }
    }

    private boolean inputDataCorrect() {
        return mTitleEditText.getText().length() >= INPUT_MIN_LENGTH &&
                mBodyEditText.getText().length() >= INPUT_MIN_LENGTH;
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Поля должны содержать как минимум " + INPUT_MIN_LENGTH + "символов", Toast.LENGTH_LONG).show();
    }

    private Note fillNoteData() {
        Note note = new Note();
        note.setTitle(mTitleEditText.getText().toString());
        note.setBody(mBodyEditText.getText().toString());
        String id = new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm:ss", Locale.ENGLISH).format(new Date());
        note.setId(id);
        return note;
    }

    private void saveNoteToRealm(final Note note) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.where(Category.class)
                        .equalTo("categoryName", mCategoryName).findFirst();
                category.getNotes().add(note);
            }
        });
    }

    //endregion
}