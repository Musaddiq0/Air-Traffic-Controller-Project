package Middleware;

import java.util.Locale;

public class practice {

    public static String update(String word){
       return word.toUpperCase();
    }
    public static void main(String [] args){
        String word = "hello";
        String oneword = "world";

        System.out.println(update(word));
        System.out.println(update(oneword));
        System.out.println(word);
        System.out.println(oneword);
    }
}
