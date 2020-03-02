package com.example.penview;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Henry
 * @Date 2020-01-23  22:03
 * @Email 2427417167@qq.com
 */
public class main {

    public static void main(String[] args) {
        System.out.println("dsafasdf");
        ArrayList list = new ArrayList();
        list.add(1);
        list.add(2);
        boolean condition1 = false;
        boolean condition2 = false;
        Iterator iterator = list.iterator();
        boolean bool=false;
        while (true){
            System.out.println("while body");
          if (iterator.hasNext()){
              if (condition1){
                  System.out.println("condition1");
              }else if (condition2){
                  System.out.println("condition2");
              }else {
                  continue;
              }
          }
          bool = true;
          break;//这个break,让while（true）可以有退出的可能
        }
        System.out.println("bool = "+bool);
    }
}
