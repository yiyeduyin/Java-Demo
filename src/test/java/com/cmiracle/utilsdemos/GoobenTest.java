package com.cmiracle.utilsdemos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmiracle.utilsdemos.password.MultiPassPasswordEncoder;
import com.cmiracle.utilsdemos.utils.csv.NormalReadFile;
import com.cmiracle.utilsdemos.utils.csv.ReadFile;
import okhttp3.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GoobenTest {

    public static PasswordEncoder passwordEncoder = new MultiPassPasswordEncoder();

    public static void main(String[] args) {
        handleShop();
        System.out.println("done");
    }

    public static void createSystemUser() {
        ReadFile<String> normalReadFile = new NormalReadFile();
        List<String[]> data = normalReadFile.readFileData("/Users/a20170509002/Documents/工作资料/果本/果本关联机构.csv");

        String outString = "insert into `sys_service`.`pi_sys_user` ( `id`, `ent_id`, `username`, `password`, `nick_name`, `email`,  `mobile`, `sex`, `status`,  `create_time`, `is_enable`) values " +
                "( %d, 140210,'%s', '%s', '%s', '%s', '%s', 0, '1',  '2019-04-22 20:00:00', '1');";

        String entString = "insert into `sys_service`.`pi_sys_user_ent_xref` ( `ent_id`, `user_id`, `parent_ent_id`, `create_time`) values " +
                "( '140210', '%d', '140208', '2019-04-22 20:00:00');";

        String groupString = "insert into `sys_service`.`pi_sys_user_group_xref` ( `id`, `ent_id`, `user_id`, `group_id`, `create_time`) values " +
                "( '%d', '140210', '%d', '1110424', '2019-04-22 20:00:00');";

        String orgString = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `province`, `province_code`, `city`, `city_code`, `update_time`, `create_time`) values " +
                "( '%d', '140208', '%s', '', '1', '%s', '%s', '%s', '%s', '2019-04-22 20:00:00', '2019-04-22 20:00:00');";

        String orgString2 = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `province`, `province_code`, `update_time`, `create_time`) values " +
                "( '%d', '140208', '%s', '', '1', '%s', '%s', '2019-04-22 20:00:00', '2019-04-22 20:00:00');";

        String orgString3 = "insert into `sys_service`.`pi_sys_organization` " +
                "( `id`, `ga_ent_id`, `org_name`,`org_code`, `org_status`, `update_time`, `create_time`) values " +
                "( '%d', '140208', '%s', '', '1', '2019-04-22 20:00:00', '2019-04-22 20:00:00');";

        String orgXrefString = "insert into `sys_service`.`pi_sys_organization_user_xref` " +
                "( `id`, `user_id`, `org_id`, `deleted`, `create_time`, `update_time`) values " +
                "( '%d', '%d', '%d', '0', '2019-04-22 20:00:00', '2019-04-22 20:00:00');";


        int userId = 100;
        int groupId = 100;
        int orgId = 100;
        int orgXrefId = 100;

        List<String> sql1 = new ArrayList<>();
        List<String> sql2 = new ArrayList<>();
        List<String> sql3 = new ArrayList<>();
        List<String> sql4 = new ArrayList<>();
        List<String> sql5 = new ArrayList<>();

        for (int i = 1; i < data.size(); i++,userId++,groupId++,orgId++,orgXrefId++) {
            String[] row = data.get(i);
            try {
                String email = row[1].trim();
                String username = row[1].trim();
                String password = row[2].trim();
                String encodePassword = passwordEncrypt(password);
                String nick_name = row[4].trim();
                String companyname = row[5].trim();
                String mobile = row[6].trim();

                String provinceName = row[7].trim();
                String provinceCode = row[8].trim();
                String cityName = row[9];
                String cityCode = row[10];

                if(StringUtils.isEmpty(provinceName)){
                    sql4.add(String.format(orgString3, orgId, companyname));
                }else if(StringUtils.isEmpty(cityCode)){
                    sql4.add(String.format(orgString2, orgId, companyname, provinceName, provinceCode));
                }else{
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

    public static void handleShop(){
        ReadFile<String> normalReadFile = new NormalReadFile();
        List<String[]> data = normalReadFile.readFileData("/Users/a20170509002/Documents/工作资料/果本/果本创建机构关联门店.csv");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            try {
                OkHttpClient client = new OkHttpClient();


                List<String> shopIds = Arrays.asList(row[3].split(","));
                List<Integer> collect = shopIds.stream().map(value -> Integer.valueOf(value)).collect(Collectors.toList());

                JSONObject object = new JSONObject();
                object.put("orgId", Integer.valueOf(row[1]));
                object.put("shopIds", collect);
                object.put("childEntIds", "140210");

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, object.toJSONString());
                Request request = new Request.Builder()
                        .url("http://gateway.scrm.xdgroup.net/admin/plat/140208/shop/addOrgAscription")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cookie", "_jwt_ent=140208; jwt=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NjA4MTA5MzcsInVzZXJfbmFtZSI6IkFEX3hkZ29vYmVuIiwidXNlcl9kZXRhaWwiOiJ7XCJ1c2VybmFtZVwiOlwieGRnb29iZW5cIixcInVzZXJJZFwiOjE0MTIwNCxcIm5pY2tOYW1lXCI6XCLpqaznu4_nkIZcIn0iLCJqdGkiOiI2MGFjZjEzNC1lOWI5LTQ4MmMtYThjZC05N2JkOWFlNzNiOTQiLCJjbGllbnRfaWQiOiJwaXBsdXMtYmFja2VuZCIsInNjb3BlIjpbImFkbWluIl19.VWF_s04wes7fJBhnp_Rh4yWLqwZOk4PRnWvphvf64y4BdZWv-NE59QY1JAfg_Stdp5QoSdxZVTb4mbXiM0HzHVMBm4956Dw0zsnHV13QoOggVJJ5j2voE_R8X0GeL46WDykIK4688xmtf057GicbClYLNsLp6WZMrY2eKo2treg; jwt_re=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJBRF94ZGdvb2JlbiIsInVzZXJfZGV0YWlsIjoie1widXNlcm5hbWVcIjpcInhkZ29vYmVuXCIsXCJ1c2VySWRcIjoxNDEyMDQsXCJuaWNrTmFtZVwiOlwi6ams57uP55CGXCJ9Iiwic2NvcGUiOlsiYWRtaW4iXSwiYXRpIjoiNjBhY2YxMzQtZTliOS00ODJjLWE4Y2QtOTdiZDlhZTczYjk0IiwiZXhwIjoxNTYwODEwOTM3LCJqdGkiOiI5ZDEzMDEyZS1mZTVmLTQyMTctODRkNS03OWZiODc5YWQ2ZjgiLCJjbGllbnRfaWQiOiJwaXBsdXMtYmFja2VuZCJ9.QQezUXoZLsI7wEQSOG1h72jyBjCWqoMGpI_SkXULfcxWqvHTEviedVvFNieJ8079DvjBlR2xhME_jstImcwZtaiz8NphZh_JHyvauSIDTyXv-u6sfjMi8tCLFOEOcdZsFFB7thhSJn7rggHLXELd5n0i8hSvZDKgtJH6YYcpbYU")
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
            } catch (Exception e) {
                System.out.println("error::::" + e.getMessage());
            }
        }


    }

}
