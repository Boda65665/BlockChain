package org.example;

public class TestTT {

    public static String inc(String num){
        char[] symbols = num.toCharArray();
        for (int i = symbols.length-1;i>=0;i--) {
            if (symbols[i] != '9') {
                num = num.substring(0, i) + String.valueOf(Integer.parseInt(String.valueOf(symbols[i])) + 1) + num.substring(i + 1);
                break;
            }
            num=num.substring(0,i)+"0"+num.substring(i+1);
            if (0 == i) {
                num = "1" + num;
                break;
            }
        }
        return num;
        }

    public static void main(String[] args) {
        String d = "00";
        for (int i =0;i<100;i++){
            d=inc(d);
            System.out.println(d);
        }
    }
    }
