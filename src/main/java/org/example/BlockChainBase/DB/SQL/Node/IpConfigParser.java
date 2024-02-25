package org.example.BlockChainBase.DB.SQL.Node;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpConfigParser {
    private Pattern pattern;
    private Matcher matcher;


    private final String IPADDRESS_PATTERN = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    public IpConfigParser() {
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    public boolean validate(final String ip) {
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
    public boolean isHaveProblemIp(){
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("github.com", 80));
            return false;
        } catch (IOException e) {
            return true;
        }
    }
    public String getMyIpFromFile() throws IOException {
        String basePath = System.getProperty("user.dir");
        String path = basePath+"\\src\\main\\java\\org\\example\\NodeCommunication\\myIp.txt";
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        line = bufferedReader.readLine();
        bufferedReader.close();
        return line;
    }

    public boolean editMyIp(String newIp) throws IOException {
        String basePath = System.getProperty("user.dir");
        String path = basePath+"\\src\\main\\java\\org\\example\\NodeCommunication\\myIp.txt";
        if (newIp!=null && validate(newIp)) {
            FileWriter fileWriter = new FileWriter(path);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(newIp);
            bufferedWriter.close();
            return true;
        }
        return false;
    }


        public String getIpAddress() throws IOException {

            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress("github.com", 80));
                return socket.getLocalAddress().getHostAddress();
            } catch (IOException e) {
            String line;
            line = getMyIpFromFile();
            if (line!=null && validate(line)) return line;
            System.out.println("Извините но не удалось установить ва ipv4,отправте в консоль свой действительный ipv4(можно позже изменить)");
            Scanner scanner = new Scanner(System.in);
            String ip = scanner.nextLine();
            while (!editMyIp(ip)){
                System.out.println("Неккоректный ipv4,отправте в консоль свой действительный ipv4");
                ip=scanner.nextLine();
            }
            return ip;
        }
            }


    public static void main(String[] args) throws IOException {
        IpConfigParser ipConfigParser = new IpConfigParser();
        System.out.println(ipConfigParser.getIpAddress());
    }

}



