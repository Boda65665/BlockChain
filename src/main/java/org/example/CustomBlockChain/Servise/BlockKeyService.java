package org.example.CustomBlockChain.Servise;

public class BlockKeyService {
    private String lastKey = "000000000000000";
    public String nextKey(){
            for (int i = lastKey.length()-1;i>=0;i--) {
                if (lastKey.charAt(i) != '9') {
                    lastKey = lastKey.substring(0, i) + String.valueOf(Integer.parseInt(String.valueOf(lastKey.charAt(i))) + 1) + lastKey.substring(i + 1);
                    break;
                }
                lastKey=lastKey.substring(0,i)+"0"+lastKey.substring(i+1);
                if (0 == i) {
                    lastKey = "1" + lastKey;
                    break;
                }
            }
            return lastKey;
        
    }
    //number in key
    public String valueOf(long num){
        String numString = String.valueOf(num);
        return "0".repeat(lastKey.length()-numString.length())+numString;
    }

    public static void main(String[] args) {
        BlockKeyService generatorKeys = new BlockKeyService();
        System.out.println(generatorKeys.valueOf(12));
    }
}
