package com.abc.p2p.util;

/**
 * @Author yang
 * @Date 2020/10/22 19:42
 * @Description : 生成随机码
 */
public class GenerateCodeUtil {
    //生成6位数随机验证码
    public static String generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            //Math.random()取值范围：[0,1)
            //Math.floor()向下取整
            stringBuilder.append((int)Math.floor(Math.random() * 10));
        }
        String messageCode = stringBuilder.toString();
        System.out.println(messageCode);
        return messageCode;
    }
}
