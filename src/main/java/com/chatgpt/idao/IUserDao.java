package com.chatgpt.idao;

import com.chatgpt.model.Paid.PaidModel;
import com.chatgpt.model.login.LoginFromModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository //标记是tomcat的东西
public interface IUserDao {

    /**查找用户信息*/
    public LoginFromModel from_user(@Param("social_uid") String social_uid);

    /**添加用户*/
    public Integer insert_user(@Param("login") LoginFromModel login);

    /**查询用户剩余的次数*/
    public PaidModel from_user_uid(@Param("uid")String uid);

    /**修改用户剩余次数 少一次*/
    public Integer update_user_uid(@Param("uid")String uid);


}
