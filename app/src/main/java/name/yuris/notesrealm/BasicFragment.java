package name.yuris.notesrealm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;

import name.yuris.notesrealm.adapter.NoteAdapter;
import name.yuris.notesrealm.manager.RealmManager;
import name.yuris.notesrealm.model.Category;
import name.yuris.notesrealm.model.Note;
import name.yuris.notesrealm.ui.CreateNoteActivity;
import name.yuris.notesrealm.ui.NoteDetailActivity;

import static android.app.Activity.RESULT_OK;

/**
 * @author Yuri Nevenchenov on 7/18/2017.
 */

public class BasicFragment extends Fragment implements NoteAdapter.NoteClickListener{

    private RecyclerView mRecyclerView;
    private FloatingActionButton mAddNoteButton;
    private NoteAdapter mAdapter;
    private Realm mRealm;
    private String mTitle;

    public static BasicFragment newInstance() {
        return new BasicFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = new RealmManager(getContext()).getRealm();
        mAdapter = new NoteAdapter(this, getNotesRealm());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAddNoteButton = (FloatingActionButton) view.findViewById(R.id.add_note_button);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, getString(R.string.create_note_question), Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(CreateNoteActivity.createExplicitIntent(getContext(), mTitle), 1);
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(int position) {
        Note note = getNotesRealm().get(position);
        startActivityForResult(NoteDetailActivity.createExplicitIntent(getContext(),
                mTitle,
                note.getTitle(),
                note.getId(),
                note.getBody()), 1);
    }

    public String getTitle() {
        return mTitle;
    }

    public BasicFragment setTitle(String title) {
        mTitle = title;
        return this;
    }

    private RealmList<Note> getNotesRealm() {
        RealmList<Note> noteList = null;
        for (Category category : mRealm.allObjects(Category.class)) {
            if (category.getCategoryName().equals(mTitle)) {
                noteList = category.getNotes();
                break;
            }
        }
        return noteList;
    }

}