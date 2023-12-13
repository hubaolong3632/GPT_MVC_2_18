package com.chatgpt.z_ljx;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cs1 {

    public static void main(String[] args) {
        //      更具正则表达式生成密钥
        Pattern pattern = Pattern.compile("sess-[A-Za-z0-9]+");
        String str="" +
                "liam76ljoh@outlook.com----W399u8pl++++----sess-SMIu1ngt1pKy53eb1zZfa36tHtB1Z3BLsuRqKsR5\n" +
                "aolson6er@hotmail.com----i36GgBwA20++----sess-Rrk6GfPIsDbb8Sz6yspSbiWGLSVgojdcxRPy0E8t\n" +
                "ethanc00whe@hotmail.com----m01usuPIY+++----sess-p0DBDnFZeHGuzPKiFNWQ8IoPeLvMZ0zU6vxG4PqJ\n" +
                "ashertlg@hotmail.com----d734POk2g+++----sess-NUfiZUxJo92B0x0yrUgrzDO1UY52SW5EZF71E4WI\n" +
                "wihayesecq@outlook.com----ONqXNvk0++++----sess-ElqTpAWc4Qr6eh8ZB4p6HgWZJibIUC6Oghs0Q2L8";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }

    }
}
