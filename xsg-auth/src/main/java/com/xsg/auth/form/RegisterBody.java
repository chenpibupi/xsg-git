package com.xsg.auth.form;

/**
 * 用户注册对象
 * 
 * @author Chenpi
 */
public class RegisterBody extends LoginBody
{
    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
