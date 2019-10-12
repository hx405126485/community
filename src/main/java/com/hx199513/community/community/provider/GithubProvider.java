package com.hx199513.community.community.provider;
/**
 * getAccessToken方法
 * 提交五个参数获取accesstoken
 *
 * getUser方法
 * 通过github的user接口，提交accesstoken换取用户信息
 * **/
import com.alibaba.fastjson.JSON;
import com.hx199513.community.community.dto.AcesstokenDTO;
import com.hx199513.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AcesstokenDTO accessTokenDTO){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            /**  分隔符获取token  */
//            String[] split=string.split("&");
//            String tokenstr=split[0];
//            String token=tokenstr.split("=")[1];
            System.out.println(string);
            String token=string.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response=client.newCall(request).execute();
            /**获取accessToken页面的内容并以String类型展示出来*/
            String string=response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println(githubUser);
            return githubUser;
        } catch (IOException e) {
        }
        return  null;
    }

}
