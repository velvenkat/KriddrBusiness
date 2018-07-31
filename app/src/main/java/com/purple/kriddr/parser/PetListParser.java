package com.purple.kriddr.parser;

import com.purple.kriddr.model.PetModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pf-05 on 2/14/2018.
 */

public class PetListParser {

    public static List<PetModel> parseFeed(String content)
    {
        try
        {

            JSONObject jsonObject = new JSONObject(content);
            JSONArray ar = jsonObject.getJSONArray("response");
            List<PetModel> response = new ArrayList<>();
            for(int i=0; i < ar.length(); i++)
            {

                JSONObject parentObject = ar.getJSONObject(i);
                PetModel flower = new PetModel();
                flower.setCreated(parentObject.getString("created"));
                flower.setPet_id(parentObject.getString("pet_id"));
                flower.setOwwner_id(parentObject.getString("owner_id"));
                flower.setOwner_name(parentObject.getString("owner_name"));
                flower.setMobile(parentObject.getString("mobile"));
                flower.setEmail(parentObject.getString("email"));
                flower.setAddress(parentObject.getString("address"));
                flower.setPreferred_contact(parentObject.getString("preferred_contact"));
                flower.setPhoto(parentObject.getString("photo"));
                flower.setPet_name(parentObject.getString("pet_name"));
                flower.setDob(parentObject.getString("dob"));
                flower.setBrand(parentObject.getString("brand"));
                flower.setProtein(parentObject.getString("protein"));
                flower.setPortion_size(parentObject.getString("portion_size"));
                flower.setShared_with_business(parentObject.getString("shared_with_business"));
                flower.setProfile_status(parentObject.getString("profile_status"));
                response.add(flower);
            }

            return response;

        }
        catch (JSONException e)
        {
            return null;
        }
        catch (Exception e)
        {
            return null;
        }

    }

}
