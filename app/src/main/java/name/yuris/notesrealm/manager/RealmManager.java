package name.yuris.notesrealm.manager;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Yuri Nevenchenov on 7/20/2017.
 */

public class RealmManager {
    private Realm mRealm;

    public RealmManager(Context context) {
        mRealm = Realm.getInstance(
                        new RealmConfiguration.Builder(context)
                        .name("notesRealm.realm")
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

    public Realm getRealm() {
        return mRealm;
    }
}