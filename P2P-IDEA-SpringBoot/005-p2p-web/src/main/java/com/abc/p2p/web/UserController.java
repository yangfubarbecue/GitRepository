package com.abc.p2p.web;

import com.abc.p2p.model.User;
import com.abc.p2p.service.RedisService;
import com.abc.p2p.service.UserService;
import com.abc.p2p.util.GenerateCodeUtil;
import com.abc.p2p.util.HttpClientUtils;
import com.abc.p2p.util.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author yang
 * @Date 2020/10/14 11:53
 * @Description :
 */

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class, timeout = 20000, version = "1.0.0")
    UserService userService;
    @Reference(interfaceClass = RedisService.class, timeout = 20000, version = "1.0.0")
    RedisService redisService;

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/loan/page/register")
    public String register() {
        System.out.println("----The method of register has been loaded.---");
        return "register";
    }

    /**
     * 验证手机号是否可用来注册
     * @param phone
     * @return
     */
    @RequestMapping("/loan/page/checkPhone")
    @ResponseBody //json格式
    public Object checkPhone(@RequestParam(name = "phone", required = true) String phone){
        //验证手机号是否可用 ==> 查看数据库是否有这个号码 ==> 根据手机查找相应用户是否存在
        User user = userService.checkPhone(phone);
        System.out.println(user);

        if (user != null) {
            String message = "The user already exists! Change phone number and try again.";
            return Result.ERROR(message);
        }else{
            String message = "The user does not exist!";
            return Result.SUCCESS(message);
        }
    }

    /**
     * 生成验证码，可向手机发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("/loan/page/messageCode")
    @ResponseBody
    public Object messageCode(@RequestParam(name = "phone",required = true) String phone) {
        /*
        Request:
        Url:https://way.jd.com/kaixintong/kaixintong
        ?mobile=19941509056
        &content=【凯信通】您的验证码是：123456
        &appkey=9cbf86929225374d00ffdc298c631ac3

        Response:
        Status Code: 200
        Time: 666ms
        Date: Word 14 Oct 2020 14:21:21 GMT
        Body
        {
            "code": "10000",
            "charge": false,
            "remain": 0,
            "msg": 查询成功"",
            "result": "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms>\n <returnstatus>Success</returnstatus>\n <message>ok</message>\n <remainpoint>-6655663</remainpoint>\n <taskID>158466711</taskID>\n <successCounts>1</successCounts></returnsms>"
        }
        */

        //获取参数
        String responseCode = null;
        String mobile = phone;
        String messageCode = GenerateCodeUtil.generateCode();
        String url = "https://way.jd.com/BABO/sms";
        String appkey = "9cbf86929225374d00ffdc298c631ac3";
        String content = "【巴卜技术】 您的验证码是"+messageCode+"，3分钟内有效！";
        String msg = content;
        //响应报文（模板）
       /* responseCode =
                "{" +
                        "\"code\": \"10000\",\n" +
                        "\"charge\": false,\n" +
                        "\"remain\": 0,\n" +
                        "\"msg\": \"查询成功\",\n" +
                        "\"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?>\n<returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-6655663</remainpoint>\\n <taskID>158466711</taskID>\\n <successCounts>1</successCounts>\n</returnsms>\"\n" +
                        "}";

        responseCode = "" +
                "{" +
                "\"code\":\"10000\"," +
                "\"charge\":false," +
                "\"remain\":0," +
                "\"msg\":\"查询成功\"," +
                "\"result\":" +
                "{" +
                "\"ReturnStatus\":\"Success\"," +
                "\"Message\":\"ok\"," +
                "\"RemainPoint\":1671427," +
                "\"TaskID\":59875930," +
                "\"SuccessCounts\":1" +
                "  }" +
                "}";*/
        /*responseCode = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"resptime\": 20180626000055,\n" +
                "        \"respstatus\": 0,\n" +
                "        \"msgid\": 91240146321\n" +
                "    }\n" +
                "}";*/
        //设置参数（京东万象参数）
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("mobile",mobile);
        paramMap.put("appkey",appkey);
        paramMap.put("msg",msg);

        //创建httpClient
        try {
            responseCode = HttpClientUtils.doPost(url, paramMap);
            System.out.println("responseCode----->"+responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR("服务器繁忙...请稍后再试...");
        }

        //解析"code": "10000",<returnstatus>Success</returnstatus>，如果成功，短信发送成功
        //解析json格式的字符串，使用阿里提供的fastjson来解析
        //将JSON格式的字符串转换为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(responseCode);
        //获取通信标识code
        String code = jsonObject.getString("code");
        //判断是否通信成功
        if (!StringUtils.equals(code, "10000")) {
            return Result.ERROR("通信异常！服务器连接失败...请稍后再试...");
        }

        /*//获取result对应的xml格式的字符串，
        //目的是用来解析"code": "10000",<returnstatus>Success</returnstatus>，如果成功，短信发送成功
        String resultXMLString = jsonObject.getString("result");
        System.out.println(resultXMLString);
        Document document = null;
        try {
            document = DocumentHelper.parseText(resultXMLString);
        } catch (DocumentException e) {
            e.printStackTrace();
            return Result.ERROR("响应报文的result对应的XML解析异常");
        }
        String xpath = "//returnstatus";
        Node returnStatusNode = document.selectSingleNode(xpath);
        String returnStatusNodeText = returnStatusNode.getText();
        if (!"Success".equals(returnStatusNodeText)) {
            return Result.ERROR("通信失败！服务器连接失败...请稍后再试...");
        }*/
        JSONObject result = jsonObject.getJSONObject("result");
        String returnStatus = result.getString("respstatus");
        System.out.println(result);
        System.out.println(returnStatus);
        if (!"0".equals(returnStatus)) {
            return Result.ERROR("通信失败！服务器连接失败...请稍后再试...");
        }

        //保存注册码
        //将验证码保存再redis中，保存的有效时间在RedisServiceImpl中设置为5分钟
        redisService.put(phone, messageCode);

        //返回成功
        return Result.SUCCESS(messageCode);
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping("/loan/page/regist")
    @ResponseBody
    public Object regist(@RequestParam(name = "inputMessageCode", required = true) String messageCode_keyboardInput,
                         User user,
                         HttpServletRequest request) {

        String message = "注册失败！";

        //验证手机验证码的正确性
        String messageCode_redis = redisService.get(user.getPhone());
        if (!StringUtils.equals(messageCode_keyboardInput, messageCode_redis)) {
            message = "验证码输入错误！";
            return Result.ERROR(message);
        }

        //插入一条记录同时领888大红包
        user = userService.registUser(user);
        System.out.println(user);
        if (user != null) {
            message = "注册成功！";
            //注册成功后将user对象放入session中，自动登录
            request.getSession().setAttribute("user", user);
            return Result.SUCCESS(message);
        }
        return Result.ERROR(message);
    }

    /**
     * 跳转到实名认证页面
     * @return
     */
    @RequestMapping("/loan/page/realName")
    public String realName() {
        return "realName";
    }

    /**
     * 确认是登录的手机号
     * @param phone
     * @param request
     * @return
     */
    @RequestMapping("/loan/page/verifyPhone")
    @ResponseBody
    public Object verifyPhone(@RequestParam(name = "phone", required = true) String phone,
                              HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        System.out.println(user);
        if (StringUtils.equals(phone, user.getPhone())) {
            return Result.SUCCESS("此为正确的手机号");
        }
        return Result.ERROR("填写的手机号必须和注册的手机号保持一致！");
    }

    @RequestMapping("/loan/page/realNameAuthentication")
    @ResponseBody
    public String realNameAuthentication() {
        //获得参数：手机号、身份证号、姓名

        //实名认证

        //更新用户表

        //返回认证成功
        return null;
    }

    /**
     * 跳转到登录界面
     * @return
     */
    @RequestMapping("/loan/page/login")
    public String login() {
        System.out.println();
        return "login";
    }


    /**
     * 跳转到密码修改页面
     * @return
     */
    @RequestMapping("/loan/page/toModifyPassword")
    public String toModifyPassword() {
        return "modifyPassword";
    }

    /**
     * 展示手机号
     * @return
     */
    @RequestMapping("/loan/page/showPhone")
    @ResponseBody //json格式
    public Object showPhone(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user != null) {
            return Result.SUCCESS(user.getPhone());
        }else{
            return Result.ERROR("用户不存在");
        }
    }

    /**
     * 确认是登录的手机号
     * @param password
     * @param request
     * @return
     */
    @RequestMapping("/loan/page/verifyOldLoginPassword")
    @ResponseBody
    public Object verifyOldLoginPassword(@RequestParam(name = "password", required = true) String password,
                              HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        System.out.println(user);
        if (StringUtils.equals(password, user.getLoginPassword())) {
            return Result.SUCCESS("密码正确！");
        }
        return Result.ERROR("填写的原密码必须和注册的原密码保持一致！");
    }

    /**
     * 修改登录密码
     * @param user
     * @return
     */
    @RequestMapping("/loan/page/modifyPassword")
    @ResponseBody
    public Object modifyPassword(@RequestParam(name = "inputMessageCode", required = true) String messageCode_keyboardInput,
                         User user,
                         HttpServletRequest request) {

        String message = "修改密码失败！";

        //验证手机验证码的正确性
        if (!StringUtils.equals(messageCode_keyboardInput, redisService.get(user.getPhone()))){
            message = "验证码输入错误！";
            return Result.ERROR(message);
        }

        if (user != null) {
            message = "修改密码成功！";
            //修改密码成功后将user对象放入session中，自动登录
            request.getSession().setAttribute("user", user);
            return Result.SUCCESS(message);
        }
        return Result.ERROR(message);
    }
}
