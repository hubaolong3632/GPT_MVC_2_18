package com.chatgpt.utio.UtioClass;

public class HTMLReplace {
    //    防止html注入攻击
    public  static String htmlEscape(String input) {
        if (input == null) {
            return null;
        }
        String output = input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("/", "&#x2F;")
                .replaceAll("\n", "<br>");  //\n 转换成换行
        return  output;
    }
}
