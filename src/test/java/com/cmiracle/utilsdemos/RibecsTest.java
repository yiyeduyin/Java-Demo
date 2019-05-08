package com.cmiracle.utilsdemos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmiracle.utilsdemos.password.MultiPassPasswordEncoder;
import com.cmiracle.utilsdemos.utils.csv.NormalReadFile;
import com.cmiracle.utilsdemos.utils.csv.ReadFile;
import okhttp3.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RibecsTest {

    public static PasswordEncoder passwordEncoder = new MultiPassPasswordEncoder();

    public static void main(String[] args) {
        handleShop();
        System.out.println("done");
    }

    public static void createSystemUser() {
        ReadFile<String> normalReadFile = new NormalReadFile();
        List<String[]> data = normalReadFile.readFileData("/Users/a20170509002/Documents/工作资料/伊贝诗/伊贝诗机构.csv");

        String outString = "insert into `sys_service`.`pi_sys_user` ( `id`, `ent_id`, `username`, `password`, `nick_name`, `email`,  `mobile`, `sex`, `status`,  `create_time`, `is_enable`) values " +
                "( %d, 140214,'%s', '%s', '%s', '%s', '%s', 0, '1',  '2019-04-25 20:00:00', '1');";

        String entString = "insert into `sys_service`.`pi_sys_user_ent_xref` ( `ent_id`, `user_id`, `parent_ent_id`, `create_time`) values " +
                "( '140214', '%d', '140212', '2019-04-25 20:00:00');";

        String groupString = "insert into `sys_service`.`pi_sys_user_group_xref` ( `id`, `ent_id`, `user_id`, `group_id`, `create_time`) values " +
                "( '%d', '140214', '%d', '1110436', '2019-04-25 20:00:00');";

        String orgString = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `province`, `province_code`, `city`, `city_code`, `update_time`, `create_time`) values " +
                "( '%d', '140212', '%s', '', '1', '%s', '%s', '%s', '%s', '2019-04-25 20:00:00', '2019-04-25 20:00:00');";

        String orgString2 = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `province`, `province_code`, `update_time`, `create_time`) values " +
                "( '%d', '140212', '%s', '', '1', '%s', '%s', '2019-04-25 20:00:00', '2019-04-25 20:00:00');";

        String orgString3 = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `update_time`, `create_time`) values " +
                "( '%d', '140212', '%s', '', '1', '2019-04-25 20:00:00', '2019-04-25 20:00:00');";

        String orgXrefString = "insert into `sys_service`.`pi_sys_organization_user_xref` " +
                "( `id`, `user_id`, `org_id`, `deleted`, `create_time`, `update_time`) values " +
                "( '%d', '%d', '%d', '0', '2019-04-25 20:00:00', '2019-04-25 20:00:00');";


        int userId = 300;
        int groupId = 300;
        int orgId = 300;
        int orgXrefId = 300;

        List<String> sql1 = new ArrayList<>();
        List<String> sql2 = new ArrayList<>();
        List<String> sql3 = new ArrayList<>();
        List<String> sql4 = new ArrayList<>();
        List<String> sql5 = new ArrayList<>();

        for (int i = 1; i < data.size(); i++, userId++, groupId++, orgId++, orgXrefId++) {
            String[] row = data.get(i);
            try {
                String email = row[1].trim();
                String username = row[1].trim();
                String password = row[2].trim();
                String encodePassword = passwordEncrypt(password);
                String mobile = row[3].trim();
                String nick_name = row[4].trim();
                String companyname = row[5].trim();

                String provinceName = row[7].trim();
                String provinceCode = row[8].trim();
                String cityName = row[9];
                String cityCode = row[10];

                if (StringUtils.isEmpty(provinceName)) {
                    sql4.add(String.format(orgString3, orgId, companyname));
                } else if (StringUtils.isEmpty(cityCode)) {
                    sql4.add(String.format(orgString2, orgId, companyname, provinceName, provinceCode));
                } else {
                    sql4.add(String.format(orgString, orgId, companyname, provinceName, provinceCode, cityName, cityCode));
                }

                sql1.add(String.format(outString, userId, username, encodePassword, nick_name, email, mobile));
                sql2.add(String.format(entString, userId));
                sql3.add(String.format(groupString, groupId, userId));
                sql5.add(String.format(orgXrefString, orgXrefId, userId, orgId));

            } catch (Exception e) {
                System.out.println("error::::" + e.getMessage());
            }
        }
        sql1.forEach(s -> System.out.println(s));
//        System.out.println("===================");
        sql2.forEach(s -> System.out.println(s));
//        System.out.println("===================");
        sql3.forEach(s -> System.out.println(s));
//        System.out.println("===================");
        sql4.forEach(s -> System.out.println(s));
//        System.out.println("===================");
        sql5.forEach(s -> System.out.println(s));

    }

    public static String passwordEncrypt(String password) {
        return passwordEncoder.encode(password);
    }

    public static void handleShop() {
        ReadFile<String> normalReadFile = new NormalReadFile();
        List<String[]> shopData = normalReadFile.readFileData("/Users/a20170509002/Documents/工作资料/伊贝诗/tempShopXref.csv");
        shopData.remove(0);
        Map<Integer, Integer> shopMap = new HashMap<>();
        shopData.forEach(s -> {
            shopMap.put(Integer.valueOf(s[0]), Integer.valueOf(s[1]));
        });


        List<String[]> data = normalReadFile.readFileData("/Users/a20170509002/Documents/工作资料/伊贝诗/伊贝诗机构.csv");
        int orgId = 300;

        for (int i = 1; i < data.size(); i++, orgId++) {
            String[] row = data.get(i);
            try {
                List<Integer> newShopIds = new ArrayList<>();
                String adminId = row[0].trim();
                String areaIds = row[12].trim();
                if(!StringUtils.isEmpty(areaIds)){
                    Arrays.stream(areaIds.split(",")).forEach(s -> {
                        Integer oldShopId = Integer.valueOf(s);
                        Integer newShopId = shopMap.get(oldShopId);
                        if (newShopId != null) {
                            newShopIds.add(Integer.valueOf(newShopId));
                        }
                    });
                    if (newShopIds.size() > 0) {
                        System.out.println("adminId="+adminId+"|orgId=" + orgId + "|newShopIds=" + newShopIds.toString());
                        sendRequest(orgId, newShopIds);
                    }
                }
            } catch (Exception e) {
                System.out.println("error::::" + e.getMessage());
            }
        }
    }

    public static void sendRequest(Integer orgId, List<Integer> shopIds) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        object.put("orgId", orgId);
        object.put("shopIds", shopIds);
        object.put("childEntIds", "140212");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, object.toJSONString());
        Request request = new Request.Builder()
                .url("http://gateway.scrm.xdgroup.net/admin/plat/140212/shop/addOrgAscription")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "_jwt_ent=140212; jwt=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjA2NDYzNjIsInVzZXJfbmFtZSI6IkFEX3hkcmliZWNzIiwidXNlcl9kZXRhaWwiOiJ7XCJ1c2VybmFtZVwiOlwieGRyaWJlY3NcIixcInVzZXJJZFwiOjE0MTIwNixcIm5pY2tOYW1lXCI6XCLlsI_ln45cIn0iLCJqdGkiOiI0YjhmYmY5Yi1jNTc0LTQyY2QtYTM1OS00NDEwOTMyZmU5MTAiLCJjbGllbnRfaWQiOiJwaXBsdXMtYmFja2VuZCIsInNjb3BlIjpbImFkbWluIl19.ogdZzrXmwyb6wc8XYBoLlEcnGPik-ytxWxXQY9ZOQd7OPvZVVVLEGgUJ_s1qnFOzllv6jxy3cEOTYjQehyfi-FPGpPFhU6RfYFMkpmT4MQXdiZz6YveNUdrV60pmcrmFg_vwb6bhG-End0TwqYIaHWCJvF6mdSBavNXFGNwKCiY; jwt_re=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJBRF94ZHJpYmVjcyIsInVzZXJfZGV0YWlsIjoie1widXNlcm5hbWVcIjpcInhkcmliZWNzXCIsXCJ1c2VySWRcIjoxNDEyMDYsXCJuaWNrTmFtZVwiOlwi5bCP5Z-OXCJ9Iiwic2NvcGUiOlsiYWRtaW4iXSwiYXRpIjoiNGI4ZmJmOWItYzU3NC00MmNkLWEzNTktNDQxMDkzMmZlOTEwIiwiZXhwIjoxNTYwNjQ2MzYyLCJqdGkiOiIzYzY3MTg0Yy1iNWRlLTRhMTEtODVhZS02NDEwYzNmZWE3OTciLCJjbGllbnRfaWQiOiJwaXBsdXMtYmFja2VuZCJ9.elq7DNUtu3Kn6LX4bnkMD0IkjR0Z--7Bp4lbkNWutFzz0gspoKlsJ2V5NsdGPq0VU13b1wmYLxnaOqR-ZH3q0GMUitwAvloEq3WTHtMLcLnDzvSz0xqSUoT1TPW6_0SGxHMxjBC524l-7Fj_8b7nOQOb88kk84rqjJrPPZ6g20s")
                .addHeader("User-Agent", "PostmanRuntime/7.11.0")
                .addHeader("Accept", "*/*")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "ffd161fe-9850-4df9-af33-721b4ccac5a8,4c215b5f-377e-4e5c-87c2-242aa5e1ad58")
                .addHeader("Host", "gateway.dev.terran.wxpai.cn")
                .addHeader("accept-encoding", "gzip, deflate")
                .addHeader("content-length", "87")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(JSON.toJSONString(response));
    }
}
