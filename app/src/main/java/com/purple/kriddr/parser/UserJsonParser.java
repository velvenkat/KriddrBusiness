package com.purple.kriddr.parser;

import com.purple.kriddr.model.UserModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 1/31/2018.
 */

public class UserJsonParser {


    public static List<UserModel> parseFeed(String content)
    {
        List<UserModel> createUser = new ArrayList<>();

        try
        {

            JSONObject parentObject = new JSONObject(content);
            UserModel flower = new UserModel();
            flower.setId(parentObject.getString("user_id"));
            flower.setName(parentObject.getString("user_name"));
            flower.setMobile(parentObject.getString("mobile"));
            flower.setEmail(parentObject.getString("email"));
            flower.setStatus(parentObject.getString("status"));
            flower.setBusiness_status(parentObject.getString("business"));
            flower.setBusiness_id(parentObject.getString("business_id"));
            flower.setBusiness_name(parentObject.getString("business_name"));
            flower.setLogo_url(parentObject.getString("logo"));
            flower.setBusiness_phone(parentObject.getString("phone"));
            flower.setBusiness_address(parentObject.getString("address"));

            createUser.add(flower);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return createUser;

    }


}
