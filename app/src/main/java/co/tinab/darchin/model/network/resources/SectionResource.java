package co.tinab.darchin.model.network.resources;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.tinab.darchin.model.Constant;
import co.tinab.darchin.model.section.Section;

/**
 * Created by A.S.R on 4/8/2018.
 */

public class SectionResource extends BasicResource {
    @Expose
    @SerializedName(Constant.DATA)
    private List<Section> sectionList;

    @Nullable
    public List<Section> getSectionList() {
        return sectionList;
    }
}
