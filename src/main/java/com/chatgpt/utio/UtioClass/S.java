package com.chatgpt.utio.UtioClass;



import java.util.ArrayList;
import java.util.List;

//自定义输出语句可以让语句按照自己选择的输出
public class S {

   private static List<Integer> list=new ArrayList();

    static { //设置不显示的内容
//        list.add(1);
    }
//输出
    public static void P(Object print,Integer bol) {

        if (!list.contains(bol)) { //如果不存在那么输出
            System.out.println(print);
        }
    }

}
