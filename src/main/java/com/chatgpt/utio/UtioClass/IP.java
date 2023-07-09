package com.chatgpt.utio.UtioClass;

import com.alibaba.fastjson.JSONObject;
import com.chatgpt.utio.model.IPRessModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletRequest;

public class IP {

    public static String getClientIp(HttpServletRequest request) { //获取ip地址
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

// 查看当前ip地域
  public static IPRessModel getIPRessModel(String userIp){
      try {
          String url = "http://whois.pconline.com.cn/ipJson.jsp?ip="+userIp+"&json=true";
          Document doc = Jsoup.connect(url).get();
          String jsonStr = doc.text();

          JSONObject jsonObject =JSONObject.parseObject(jsonStr);
          String ip            = jsonObject.getString("ip");
          String province         = jsonObject.getString("pro");
          String provinceCode         = jsonObject.getString("proCode");
          String city                 = jsonObject.getString("city");
          String cityCode             = jsonObject.getString("cityCode");
          String address              = jsonObject.getString("addr");

          IPRessModel ipRessModel = new IPRessModel(ip, province, provinceCode, city, cityCode, address);
          return ipRessModel;

//            System.out.println("IP: " + ip);
//            System.out.println("省份: " + province);
//            System.out.println("省份代码: " + provinceCode);
//            System.out.println("城市: " + city);
//            System.out.println("城市代码: " + cityCode);
//            System.out.println("地址: " + address);

      } catch (Exception e) {
          e.printStackTrace();
          return  null;
      }

  }
}
