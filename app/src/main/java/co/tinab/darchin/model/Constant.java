package co.tinab.darchin.model;

import java.util.Locale;

/**
 * Created by A.S.R on 1/29/2018.
 */

public class Constant {
    public static final String Pref_Locale = "pref_locale";
    static final String Pref_Has_Login = "has_logged_in";
    static final String Pref_Has_Selected_Language = "has_selected_language";
    static final String Pref_First_Use = "first_use";
    static final String Pref_Token = "api_token";
    public static final String Pref_Name = "Darchin";

    // Constants:
    public static final String JSON = "application/json";

    // ResponseModel Keys:
    public static final String DATA = "data";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String Auth = "Authorization";

    // ResponseModel Values:
    public static final String SUCCESS = "success";

    // Paths:
    public static final String DOMAIN = "http://darchin.in/";
    public static String BASE_URL = DOMAIN.concat("api/v1/".concat(Locale.getDefault().getLanguage()).concat("/"));
    public static final String LOGO_PATH = DOMAIN.concat("uploads/logo/small/");
    public static final String COURIER_PATH = DOMAIN.concat("uploads/courier/small/");
    public static final String PRODUCT_THUMBNAIL_PATH = DOMAIN.concat("uploads/product/small/");
    public static final String REFERRAL_LINK = DOMAIN.concat("referral/");
}
