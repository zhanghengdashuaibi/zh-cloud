package com.csbr.cloud.common.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.text.NumberFormat;
import java.util.UUID;

/**
 * @program: user-auth-service
 * @description: 公共工具
 * @author: Huanglh
 * @create: 2020-07-07 12:05
 **/
public class CommonUtil {
    // region MD5

    /**
     * md5加密方法
     *
     * @param content
     * @param key
     * @return
     */
    public static String md5(String content, String key) {
        return DigestUtils.md5Hex(content + key);
    }

    /**
     * 用户密码加密方法(使用专用的key)
     *
     * @param content
     * @return
     */
    public static String md5ForPwd(String content) {
        String key = "csbr#pwd";
        return md5(content, key);
    }
    // endregion

    /**
     * 生成guid
     *
     * @return
     */
    public static String newGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 左补零返回定长字符串(截取右侧定长字符串)
     *
     * @param strLength 字符串长度
     * @param str       字符串
     * @param fillStr   填充字符串
     * @return
     */
    public static String leftZerofill(Integer strLength, String str, String fillStr) {
        String res = "";
        for (int i = 0; i < strLength; i++) {
            res += fillStr;
        }
        res += str;
        return res.substring(res.length() - strLength);
    }

    /**
     * 不使用科学计数法
     *
     * @param val
     * @return
     */
    public static String noScientificNotation(Double val) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(val);
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        Integer lastIndex = fileName.lastIndexOf('.');
        // 没有后缀时，lastIndex 为 -1,还要排除 . 为最后一个字符的情况
        if (lastIndex > -1 && lastIndex < (fileName.length() - 1)) {
            return fileName.substring(lastIndex);
        }
        return "";
    }
}
