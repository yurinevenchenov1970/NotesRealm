package name.yuris.notesrealm.model;

import io.realm.RealmObject;

/**
 * @author Yuri Nevenchenov on 7/21/2017.
 */

public class Category extends RealmObject {

    private String categoryName;

    public Category() {
        //Empty constructor needed by Realm.
            }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}