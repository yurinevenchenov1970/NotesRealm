package name.yuris.notesrealm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import io.realm.Realm;
import name.yuris.notesrealm.R;
import name.yuris.notesrealm.model.Category;
import name.yuris.notesrealm.model.Note;

/**
 * Activity to show note details
 *
 * @author Yuri Nevenchenov on 7/30/2017.
 */

public class NoteDetailActivity extends BaseNoteActivity {

    private boolean mNoteEdited;

    public static Intent createExplicitIntent(Context context,
                                              String categoryName,
                                              String noteTitle,
                                              String noteId,
                                              String noteBody) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        intent.putExtra(EXTRA_NOTE_BODY, noteBody);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillUIData();
    }

    @Override
    protected boolean isEditTextEnabled() {
        return false;
    }

    @Override
    protected int getSecondButtonVisibility() {
        return View.VISIBLE;
    }

    @Override
    protected int getMainButtonImageResource(){
        return android.R.drawable.ic_menu_edit;
    }

    @Override
    protected void onMainButtonClick(View view) {
        Snackbar.make(view, R.string.edit_note_question, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editNote();
                    }
                }).show();
    }

    @Override
    protected void onSecondButtonClick(View view) {
        Snackbar.make(view, R.string.delete_note_question, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteNote();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String newTitle = data.getStringExtra(EXTRA_NOTE_TITLE);
                String newBody = data.getStringExtra(EXTRA_NOTE_BODY);
                mTitleEditText.setText(newTitle);
                mBodyEditText.setText(newBody);
                mNoteEdited = true;
                mNoteTitle = newTitle;
                mNoteBody = newBody;
            }
        }
    }

    protected void handleActivityClosing() {
        if (mNoteEdited) {
            finishWithResultOk();
        } else {
            finish();
        }
    }

    protected void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        mCategoryName = bundle.getString(EXTRA_CATEGORY_NAME);
        mNoteTitle = bundle.getString(EXTRA_NOTE_TITLE);
        mNoteId = bundle.getString(EXTRA_NOTE_ID);
        mNoteBody = bundle.getString(EXTRA_NOTE_BODY);
    }

    //region private methods

    private void fillUIData() {
        mTitleEditText.setText(mNoteTitle);
        mNoteIdTextView.setText(mNoteId);
        mBodyEditText.setText(mNoteBody);
    }

    private void deleteNote() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.where(Category.class)
                        .equalTo("categoryName", mCategoryName)
                        .findFirst();
                Note note = realm.where(Note.class)
                        .equalTo("id", mNoteId)
                        .findFirst();
                category.getNotes().remove(note);
            }
        });
        finishWithResultOk();
    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void editNote() {
        startActivityForResult(EditNoteActivity.createExplicitIntent(getApplicationContext(),
                mNoteTitle,
                mNoteId,
                mNoteBody), 2);
    }
    //endregion
}