package com.chatgpt.idao;


import com.chatgpt.model.login.LoginFromModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository //标记是tomcat的东西
public interface ILoginDao {


    /**查询当前数据库是否存在这条数据*/
    public LoginFromModel from_user_social_uid(@Param("social_uid") String social_uid);


    /**新用户第一次进来需要输入账号密码用于登入   用户注册写的质料 (登入完整添加资料)*/
    public Integer update_UserPassowrdLogin(@Param("name")String name,@Param("password")String password,@Param("social_uid")String social_uid);

    /**查询用户是否有填写账号密码*/
    public LoginFromModel from_user_name(@Param("social_uid")String social_uid);


    /**查询账号密码是否存在 用于登入*/
    public LoginFromModel from_user_passowr(@Param("name")String name,@Param("password")String password);


    /***/


    /***/


    /***/


    /***/


    /***/


    /***/



}
