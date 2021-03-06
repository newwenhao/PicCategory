package com.we.piccategory.model;

import com.loopj.android.http.RequestParams;
import com.we.piccategory.bean.RgbResult;
import com.we.piccategory.bean.Task;
import com.we.piccategory.mvp.IModel;
import com.we.piccategory.net.HttpUtil;
import com.we.piccategory.net.SuccessRespHandler;
import com.we.piccategory.util.Constant;
import com.we.piccategory.util.LoginManger;
import com.we.piccategory.util.MD5Util;

import java.security.NoSuchAlgorithmException;

/**
 * Created with Android Studio
 * User: 潘浩
 * School 南华大学
 * Date: 2017/5/29
 * Time: 18:38
 * Description:
 */
public class CollectModel implements IModel {

    @Override
    public void load(OnCompletedListener listener) {
        try {
            String dest = "/user/selectTaskAndIntegral";
            int userId = LoginManger.getUserId();
            String token = LoginManger.getToken();
            String all = "" + userId + Constant.ENCRYPT;
            String md5 = MD5Util.getMD5(all);

            RequestParams params = new RequestParams();
            params.put("userId", userId);
            params.put("token", token);
            params.put("md5", md5);

            HttpUtil.doPost(dest, params, new SuccessRespHandler(listener) {
                @Override
                protected RgbResult getResult(String resp) {
                    return RgbResult.formatToPojo(resp, Task.class);
                }

                @Override
                protected void onStatusFail(RgbResult rgbResult) {
                    mListener.onFail("没加载到数据");
                }

                @Override
                protected void onStatusOk(RgbResult rgbResult) {
                    Task task = (Task) rgbResult.getData();
                    if (task != null) {
                        mListener.onCompleted(task);
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
