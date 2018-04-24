package com.zzkx.mtool.bean;

/**
 * Created by L.K.X on 2016/4/2.
 */
public class SSO_UserBean {
    public String id;//ID
    public String name;//用户名
    public String userIcon;//头像链接
    public String token;// 获取授权token,令牌
    public String gender;//性别

    public String description;//描述,暂时不需要,没查

    @Override
    public String toString() {
        return "SSO_UserBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", token='" + token + '\'' +
                ", gender='" + gender + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
