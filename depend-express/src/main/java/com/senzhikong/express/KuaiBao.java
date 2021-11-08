package com.senzhikong.express;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.http.HttpUtil;
import com.senzhikong.util.http.RequestEntity;
import com.senzhikong.util.string.sign.MD5Util;

import java.net.URLEncoder;

/**
 * @author Shu.zhou
 * @date 2018年12月12日上午10:31:11
 */
public class KuaiBao {

    public static ExpressResponse query(KuaiBaoConfig config, String no, String company_code) {
        ExpressResponse response = new ExpressResponse();
        response.setSuccess(false);
        try {
            RequestEntity re = new RequestEntity();
            re.setMethod("GET");
            re.setUrl(KuaiBaoConfig.API_URL);
            re.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // 根据API的要求，定义相对应的Content-Type
            JSONObject object = new JSONObject();
            object.put("waybill_no", no);
            object.put("exp_company_code", company_code);
            object.put("result_sort", 0);
            String timestamp = String.valueOf(System.currentTimeMillis());
            String signStr = config.getAppid() + KuaiBaoConfig.QUERY_API_NAME + timestamp + config.getAppsecret();
            String sign = MD5Util.getInstance()
                    .encode(signStr, null, null);
            String param = "app_id=" + config.getAppid() + "&method=" + KuaiBaoConfig.QUERY_API_NAME;
            param += "&ts=" + timestamp;
            param += "&sign=" + sign;
            param += "&data=" + URLEncoder.encode(object.toJSONString(), "utf-8");
            re.setParam(param);
            String reString = HttpUtil.http(re);
            JSONObject json = JSON.parseObject(reString);
            response.setMsg(json.getString("msg"));
            if (json.getInteger("code") != 0) {
                return response;
            }
            json = json.getJSONArray("data")
                    .getJSONObject(0);
            response.setSuccess(true);
            response.setSerialNumber(no);
            switch (json.getString("status")) {
                case "collected":
                    response.setStatus(Status.COLLECTED);
                    break;
                case "sending":
                    response.setStatus(Status.SENDING);
                    break;
                case "delivering":
                    response.setStatus(Status.DELIVERING);
                    break;
                case "signed":
                    response.setStatus(Status.SIGNED);
                    break;
                case "question":
                    response.setStatus(Status.QUESTION);
                    break;
                default:
                    response.setStatus(Status.UNKNOWN);
                    break;
            }
            JSONArray array = json.getJSONArray("data");
            if (array != null) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    item.put("description", item.get("context"));
                }
            }
            response.setExpressList(array);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        ExpressResponse response =
                query(new KuaiBaoConfig("101618", "27068766d205ad1963206a77a9d805f7b2cf6e3a"), "3867320126210", "yd");
        System.out.println(response);
    }
}
