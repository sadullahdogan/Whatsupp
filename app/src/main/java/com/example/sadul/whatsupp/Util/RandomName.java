package com.example.sadul.whatsupp.Util;

import java.util.Random;

public class RandomName {

    public static String getRandomString(){

            String randomChrs="ABCDEFGHJKLMNRSQTUVXYZ1234567890";
            StringBuilder str= new StringBuilder();
        Random rnd= new Random();
        while (str.length()<15){
            int index=(int)(rnd.nextFloat()*randomChrs.length());
            str.append(randomChrs.charAt(index));

        }
        String text=str.toString();
        return  text;

    }

}
