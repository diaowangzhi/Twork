package com.twork.util;

public class Code {
    public static final int SUCCESS = 1000;                             // 操作成功
    public static final int NOLOGIN = 1001;                             // 未登陆
    public static final int PERMISSION_DENIED = 1002;                   // 越权
    public static final int PARAMETER_NOT_MATCH = 1003;                 // 参数不匹配
    public static final int UNKNOWN_ERROR = 1004;                       // 未知错误
    public static final int HTTP_REQUEST_METHOD_NOT_SUPPORTED = 1005;   // 请求方法不支持错误

    public static final int REGIREGIST_FAILED = 2001;                   // 注册失败
    public static final int USER_NOT_EXIST = 2002;                      // 用户不存在
    public static final int USER_NOT_AVALABLE = 2003;                   // 用户禁用
    public static final int ALREADY_LOGIN = 2004;                       // 用户已登陆
    public static final int USERID_OR_PASSWORD_INCORRECT = 2005;        // 账号或密码错误
    public static final int LOGOUT_FAILED = 2006;                       // 登出失败
    public static final int CHANGE_USER_INFO_FAILED = 2007;             // 修改用户信息失败
    public static final int PASSWORD_ERROR = 2008;                      // 密码错误
    public static final int CHANGE_PASSWORD_FAILED = 2009;              // 修改密码失败
    public static final int ALREADY_SIGN = 2010;                        // 已经签到
    public static final int SIGN_FAILED = 2011;                         // 签到失败
    public static final int EXIT_GROUP_FAILED = 2012;                   // 退群失败
    public static final int SHIELD_GROUP_FAILED = 2013;                 // 屏蔽失败

    public static final int ALREADY_FRIEND = 3001;                      // 已经是好友
    public static final int ADD_FRIEND_FAILED = 3002;                   // 添加好友失败
    public static final int FRIEND_NOT_EXIST = 3003;                    // 好友不存在
    public static final int DELETE_FRIEND_FAILED = 3004;                // 删除好友失败
    public static final int NOT_ALLOW_ADD_MIME_AS_FRIEND = 3005;        // 不允许加自己为好友
    public static final int NOT_FRIEND = 3006;                          // 不是好友

    public static final int FRIEND_GROUP_ALREADY_EXIST = 3011;          // 好友分组已存在
    public static final int ADD_FRIEND_GROUP_FAILED = 3012;             // 添加好友分组失败
    public static final int FRIEND_GROUP_NOT_EXIST = 3013;              // 好友分组不存在
    public static final int FRIEND_GROUP_NOT_ALLOW_DELETE = 3014;       // 好友分组不允许删除
    public static final int DELETE_FRIEND_GROUP_FAILED = 3015;          // 删除好友分组失败
    public static final int FRIEND_GROUP_CHANGE_FAILED = 3016;          // 修改好友分组失败
    public static final int USER_ALREADY_IN_FRIEND_GROUP = 3017;        // 用户已经在好友分组中
    public static final int MOVE_FRIEND_FAILED = 3018;                  // 移动好友失败


    public static final int CREATE_GROUP_FAILED = 4001;                 // 新建群组失败
    public static final int CHANGE_GROUP_FAILED = 4002;                 // 修改群组名失败
    public static final int USER_ALREADY_IN_GROUP = 4003;               // 用户已经在群组中
    public static final int USER_NOT_IN_GROUP = 4004;                   // 用户不在在群组中
    public static final int ADD_USER_FAILED = 4005;                     // 添加群组用户失败
    public static final int DELETE_GROUP_USER_FAILED = 4006;            // 删除群组用户失败
    public static final int DELETE_GROUP_FAILED = 4007;                 // 删除群组失败
    public static final int GROUP_NOT_EXIST = 4008;                     // 群组不存在

    public static final int CAN_NOT_CHANGE_ADMIN_LEVEL = 5001;          // 不允许修改
    public static final int CHANGE_USER_LEVEL_FAILED = 5002;            // 修改用户权限失败
    public static final int DELETE_USER_FAILED = 5003;                  // 删除用户失败

    public static final int RELEASE_NOTICE_FAILED = 6001;               // 发布公告失败
    public static final int NOTICE_NOT_EXIST = 6002;                    // 公告不存在
    public static final int DELETE_NOTICE_FAILED = 6003;                // 删除公告失败

    public static final int NOTE_NOT_EXIST = 7001;                      // 记事不存在
    public static final int ADD_NOTE_FAILED = 7002;                     // 添加记事失败
    public static final int DELETE_NOTE_FAILED = 7003;                  // 删除记事失败
    public static final int CHANGE_NOTE_FAILED = 7004;                  // 修改记事失败

    public static final int FILE_NOT_EXIST = 8001;                      // 文件不存在
    public static final int DELETE_FILE_FAILED = 8002;                  // 删除文件失败
    public static final int CHECK_FILE_FAILED = 8003;                   // 删除文件失败
    public static final int FILE_UPLOADING = 8004;                      // 文件上传中
    public static final int UPLOAD_FILE_FAILED = 8005;                  // 文件上传失败

   public static final int GET_MESSAGE_NUMBER_FAILED = 9001;            // 获取消息数量失败

}
