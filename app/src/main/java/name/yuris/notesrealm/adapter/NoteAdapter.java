package name.yuris.notesrealm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmList;
import name.yuris.notesrealm.R;
import name.yuris.notesrealm.model.Note;


/**
 * @author Yuri Nevenchenov on 7/23/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final NoteClickListener mNoteClickListener;
    private RealmList<Note> mNoteList;

    public NoteAdapter(NoteClickListener clickListener,
                       RealmList<Note> noteList) {
        mNoteList = noteList;
        mNoteClickListener = clickListener;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.idTextView.setText(note.getId());
        holder.titleTextView.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTextView;
        private TextView idTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            idTextView = (TextView) itemView.findViewById(R.id.id_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mNoteClickListener.onClick(getLayoutPosition());
        }
    }

    public interface NoteClickListener {
        void onClick(int position);
    }
}
