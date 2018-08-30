package co.tinab.darchin.view.navigation_drawer;

/**
 * Created by ali.soleimani on 7/31/2017.
 */

class NavDrawerModel {
    private String title;
    private int imgResID;

    NavDrawerModel(String title,int imgResID) {
        this.title = title;
        this.imgResID = imgResID;
    }

    String getTitle() {
        return title;
    }

    int getImgResID() {
        return imgResID;
    }
}
