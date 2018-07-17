package com.maiya.genericity;

import com.maiya.beans.MemberContacts;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubinsu
 * Date: 2018/7/12 15:47
 * Desc: 泛型和反射测试
 */
public class Genericity {

    private <T> void printBean(T bean) {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.print("\t".concat(String.valueOf(field.get(bean))));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public <T> void test(T bean) {
        if (bean instanceof List) {
            ((List) bean).forEach(this::printBean);

        } else {
            printBean(bean);
        }
    }

    public static void main(String[] args) {
        List<MemberContacts> memberContacts = new ArrayList<>();
        memberContacts.add(new MemberContacts("list member1", "tel1"));
        memberContacts.add(new MemberContacts("list member2", "tel2"));
        memberContacts.add(new MemberContacts("list member3", "tel3"));
        memberContacts.add(new MemberContacts("list member4", "tel4"));

        System.out.println("测试List");
        Genericity genericity = new Genericity();
        genericity.test(memberContacts);

        System.out.println("");
        System.out.println("测试Obj");
        MemberContacts memberContacts1 = new MemberContacts("single Member1", "single tel1");
        genericity.test(memberContacts1);

    }
}
