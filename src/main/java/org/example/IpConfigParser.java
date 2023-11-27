package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IpConfigParser {

    public  String getEthernetIp() throws IOException {
        String command = "ipconfig";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains("Ethernet Ethernet")) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains("IPv4-")) {
                        return line.split(": ")[1];
                    }
                }
            }
        }

        return null;
    }

    public  String getWifiIp() throws IOException {
        String command = "ipconfig";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Wireless LAN adapter Wi-Fi")) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains("IPv4 Address")) {
                        return line.split(": ")[1];
                    }
                }
            }
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        IpConfigParser ipConfigParser = new IpConfigParser();
        System.out.println(ipConfigParser.getEthernetIp());
    }
}