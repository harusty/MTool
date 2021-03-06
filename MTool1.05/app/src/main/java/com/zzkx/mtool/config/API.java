package com.zzkx.mtool.config;

/**
 * Created by sshss on 2017/6/23.
 */

public class API {

    //    private static final String HOST = "http://mtool.zhaomini.com";
    public static final String HOST = "http://39.106.205.201";
    public static final String BASE_URL = HOST + "/mtool/portal/api/";

    public static final String UPLOAD_URL = "http://39.106.205.201/fileService/file/upload/png";
    public static final String MENU_LIST = BASE_URL + "foodinfo/find_foodinfo_by_restaurantsid";
    public static final String LOGIN = BASE_URL + "user/member/login";
    public static final String CART_INFO = BASE_URL + "foodcar/find_foodcar_byfood";
    public static final String SUBMIT_ORDER = BASE_URL + "/payinfo/create_pay_order";
    public static final String ADD_ADDRESS = BASE_URL + "member/receive/addr/create_user_address";
    public static final String UPDATE_ADDRESS = BASE_URL + "member/receive/addr/update_user_address";
    public static final String DELETE_ADDRESS = BASE_URL + "member/receive/addr/delete_user_address";
    public static final String ADD_LIST = BASE_URL + "member/receive/addr/find_user_address";
    public static final String ORDER_DETAIL = BASE_URL + "orderdining/find_order_dining_by_order_id";
    public static final String HISTORY_ORDER = BASE_URL + "orderbase/order_dining";
    public static final String SEARCH_NEARYBY_SHOP_FOOD = BASE_URL + "merchantrestaurants/find_food_by_key_word";
    public static final String HOT_SEARCH = BASE_URL + "comm/searchkeyword/find_search_keyword_by_hot";
    public static final String NO_ENVALUATE_ORDER_LIST = BASE_URL + "orderdining/find_no_comment_orders";
    public static final String SUBMIT_ORDER_ENVALUATE = BASE_URL + "merchantRestaurantsComment/create_order_restaurants_comments";
    public static final String PUBLISH_STATE = BASE_URL + "forum/post/createforumPost";
    public static final String SHARE_STATE = BASE_URL + "forum/post/shareforumPost";
    public static final String STATE_LIST = BASE_URL + "forum/post/findforumPost";
    public static final String TO_SUPPORT = BASE_URL + "forum/postsuppopp/create_suppopp";
    public static final String SHOP_COMMENT_TO_SUPPORT = BASE_URL + "merchantrestaurantscommentreply/createCommentsSuppopp";
    public static final String STATE_DETAIL = BASE_URL + "forum/post/forumPost_detail";
    public static final String STATE_COMMENT_LIST = BASE_URL + "forum/postcomment/find_forumpost_comment";
    public static final String REPLY = BASE_URL + "forum/postcomment/create";
    public static final String SHOP_COMMENT_REPLY = BASE_URL + "/merchantrestaurantscommentreply/create_comments_reply";
    public static final String SHOP_COLLECT = BASE_URL + "member/shopcollect/create_user_shopcollect";
    public static final String SHOP_DETAIL_INFO = BASE_URL + "member/shopcollect/findUserShopCollectByUserAndMerchant";
    public static final String REGIST_CODE = BASE_URL + "communication/smsverificode/registuser";
    public static final String PWD_RESET_CODE = BASE_URL + "communication/smsverificode/forgetpassword";
    public static final String PUBLISH_REST_INFO = BASE_URL + "communication/smsverificode/identification";
    public static final String UPDATE_PASSWORD = BASE_URL + "user/member/updatepassword";
    public static final String UPDATE_USER_INFO = BASE_URL + "user/member/perfect";
    public static final String DELETE_STATE = BASE_URL + "forum/post/delete";
    public static final String MY_SUPPOSED_ACT = BASE_URL + "forum/post/find_suppopp_byuserid";
    public static final String COLLECT_GOOD = BASE_URL + "member/goodscollect/create_user_goodscollect";
    public static final String REGIST = BASE_URL + "user/member/create";
    public static final String SHOP_COMMENT_LIST = BASE_URL + "merchantRestaurantsComment/find_restaurants_comments_by_restaurantid";
    public static final String MENU_COMMENT_LIST = BASE_URL + "foodcomment/find_foodcomment_by_foodid";
    public static final String SHOP_GALLARY = BASE_URL + "merchantrestaurants/user_view_restaurants_msg";
    public static final String USER_COLLECT = BASE_URL + "member/membercollectcatalog/view_user_goodscollect_catalog";
    public static final String ADD_CAT = BASE_URL + "member/membercollectcatalog/create_user_goodscollect_catalog";
    public static final String COLLECT_CATNAME_LIST = BASE_URL + "member/membercollectcatalog/look_user_goodscollect_catalog";
    public static final String EDIT_COLLECTION_CAT = BASE_URL + "member/membercollectcatalog/edit_user_goodscollect_catalog";
    public static final String DELETE_COLLECTION_CAT = BASE_URL + "member/membercollectcatalog/del_user_goodscollect_catalog";
    public static final String SET_COLLECTION_CAT = BASE_URL + "member/membercollectcatalog/add_user_goodscollect_catalog_detail";
    public static final String DEL_SHOP_COLLECTION = BASE_URL + "member/shopcollect/cancle_user_shopcollect";
    public static final String DEL_GOODS_COLLECTION = BASE_URL + "member/goodscollect/cancle_user_goodscollect";
    public static final String DEL_STATE_COLLECTION = BASE_URL + "forum/postcollect/delete_corumpost_collect";
    public static final String MENU_DETAIL = BASE_URL + "foodinfo/find_foodinfo_with_sku_by_id";
    public static final String ADD_STATE_COLLECTION = BASE_URL + "forum/postcollect/create";
    public static final String CANCLE_SUPPORT = BASE_URL + "forum/postsuppopp/delete_suppopp";
    public static final String SHOP_COMMENT_CANCLE_SUPPORT = BASE_URL + "merchantrestaurantscommentreply/deleteCommentsSuppopp";
    public static final String MY_STATE_LIST = BASE_URL + "forum/post/find_myforumpost_byuserid";
    public static final String USER_STATE_GALERY = BASE_URL + "forum/post/find_myresource_byuserid";
    public static final String SEARCH_STATE = BASE_URL + "comm/searchkeyword/find_forum_by_content";
    public static final String LOG_OUT = BASE_URL + "user/member/logout";
    public static final String SEARCH_COLLECTION = BASE_URL + "comm/searchkeyword/find_collect_by_keyword";
    public static final String LOGIN_MSG_CODE = BASE_URL + "communication/smsverificode/loginuser";
    public static final String MSG_LOGIN = BASE_URL + "user/member/loginUser";
    public static final String NO_PWD_LOGIN = BASE_URL + "user/member/free_login";
    public static final String USER_INFO = BASE_URL + "user/member/userImfo";
    public static final String CONTACT_INFO_UPDATE = BASE_URL + "user/member/finduser_by_hxid";
    public static final String SEARCH_CONTACT = BASE_URL + "comm/searchkeyword/find_chat_relation_by_name";
    public static final String USER_DETAIL = BASE_URL + "forum/post/findUserShopCollectByUserAndMerchant";
    public static final String ADD_ATTENTION = BASE_URL + "chatrelation/create_chat_relation";
    public static final String DEL_ATTENTION = BASE_URL + "chatrelation/deleteChatRelation";
    public static final String GET_USER_SETTING = BASE_URL + "chatfriend/finduserSeting";
    public static final String SET_USER = BASE_URL + "chatfriend/user_seting";
    public static final String CITY_DATA = BASE_URL + "system/region/query_city_region";
    public static final String CITY_DISTRICT_DATA = BASE_URL + "system/region/query_district_region";
    public static final String CITY_LAT = BASE_URL + "system/region/query_city_msg";
    public static final String VIDEO_UPLOAD_ADD = BASE_URL + "forum/videoUpload/createUploadVideo";
    public static final String TOP_STATE_COMMENTS = BASE_URL + "forum/postcomment/find_selected_comment";
    public static final String ATTENTION_SHOPS = BASE_URL + "chatrelation/find_shopcollect_byuserid";
    public static final String ATTENTION_USERS = BASE_URL + "chatrelation/find_chatrelation_idol_byuserid";
    public static final String MY_FANS = BASE_URL + "chatrelation/find_chatrelation_fans_byuserid";
    public static final String CONTACT_TAGS = BASE_URL + "chatbooklabel/find_book_labelBymemId";
    public static final String CREATE_CONTACT_TAG = BASE_URL + "chatbooklabel/create_book_labelBymemId";
    public static final String TAG_MEMBER_LIST = BASE_URL + "chatbooklabel/findUserMemberInfoBylabelId";
    public static final String DEL_CONTACT_TAG = BASE_URL + "chatbooklabel/delete_book_labelBymemId";
    public static final String EDIT_CONTACT_TAG = BASE_URL + "chatbooklabel/update_book_labelBymemId";
    public static final String ADD_TAG_MEMBER = BASE_URL + "chatbooklabel/create_book_labelinfoBymemId";
    public static final String SEARCH_USER = BASE_URL + "user/member/findUserInfo";
    public static final String GROUP_MEMBERS = BASE_URL + "user/member/finduser_by_hxgroupId";
    public static final String DEL_TAG_USER = BASE_URL + "chatbooklabel/delete_book_labelinfoBymemId";
    public static final String BLACK_LIST = BASE_URL + "user/member/findBlacklistUserByHxName";
    public static final String REMOVE_FROM_BLACK_LIST = BASE_URL + "user/member/deleteBlacklistUserByHxName";
    public static final String ADD_SHOP_ATTENTION = BASE_URL + "chatrelation/create_shop_relation";
    public static final String CANCLE_SHOP_ATTENTION = BASE_URL + "chatrelation/deleteshopRelation";
    public static final String SEARCH_USER_PRIVACY = BASE_URL + "user/usermemberseting/finduserSeting";
    public static final String SET_USER_PRIVACY = BASE_URL + "user/usermemberseting/userSeting";
    public static final String SET_MULTI_TAG = BASE_URL + "chatbooklabel/create_book_labelinfoByLabel";
    public static final String SEARCH_USER_TAG = BASE_URL + "chatbooklabel/findlabelNameBymemberId";
    public static final String BILL_LIST = BASE_URL + "user/memberaccountdetail/account_income_expenses_current_month";
    public static final String WALLETE_INFO = BASE_URL + "user/memberaccount/account_detail";
    public static final String WALLETE_CHARGE = BASE_URL + "user/memberaccount/add_user_account_number";
    public static final String BANK_CARD_LIST = BASE_URL + "merchantuserbankinfo/list_bank";
    public static final String ADD_BANK_CARD = BASE_URL + "merchantuserbankinfo/add_bank";
    public static final String WITHDRAW = BASE_URL + "comm/commwithdrawinfo/withdrawinfo";
    public static final String OTHER_LOGIN = BASE_URL + "user/member/bindthridlogin";
    public static final String BIND_PHONE = BASE_URL + "user/member/bindthridmyinfo";
    public static final String SHOP_COMMENT_REPLY_LIST = BASE_URL + "merchantrestaurantscommentreply/findCommentsReplybycommentId";
    public static final String SHOP_COMMENT_DETAIL = BASE_URL + "merchantRestaurantsComment/find_comments_by_restaurantid_detail";
    public static final String CHANGE_GROUP_OWNER = BASE_URL + "chatgroup/group_to_new";
    public static final String COMPLAIN = BASE_URL + "comm/customerappexccomplain/create_exccomplain";
    public static final String SYSTEM_RECOMMEND_USER = BASE_URL + "userpushseting/find_user_push";
    public static final String SYSTEM_NOTI_LIST = BASE_URL + "system/noticeinfo/querynoticeinfo";
    public static final String SERV_CENTER_QUES_CAT = BASE_URL + "content/contentinfo/querycatelogIds_byparentid";
    public static final String QUES_DETAIL_LIST = BASE_URL + "content/contentinfo/querycontent_by_catelogid";
    public static final String SERVIVCE_PHONE = BASE_URL + "system/setting/querysettinginfo";
    public static final String ARTICLE_DETAIL = BASE_URL + "content/contentinfo/querydetail";
}
