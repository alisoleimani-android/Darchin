package co.tinab.darchin.controller.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import co.tinab.darchin.R;

/**
 * Created by A.S.R on 02/05/2017.
 */

public class ExternalStorage {

    public static File getOutputMediaFile(Context context,String fileName){
        return new File(getDir(context),fileName);
    }

    private static File getDir(Context context) {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(sdDir, context.getString(R.string.app_name));
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        return file;
    }

}

