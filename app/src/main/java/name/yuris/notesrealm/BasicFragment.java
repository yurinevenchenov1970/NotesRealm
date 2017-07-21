package name.yuris.notesrealm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Yuri Nevenchenov on 7/18/2017.
 */

public class BasicFragment extends Fragment {

    private TextView mTitleTextView;
    private String mTitle;

    public static BasicFragment newInstance() {
        return new BasicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
        mTitleTextView = (TextView) view.findViewById(R.id.title_text_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleTextView.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }

    public BasicFragment setTitle(String title) {
        mTitle = title;
        return this;
    }
}