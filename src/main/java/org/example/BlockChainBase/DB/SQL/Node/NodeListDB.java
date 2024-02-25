package org.example.BlockChainBase.DB.SQL.Node;



import org.example.BlockChainBase.Cryptography.AESEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class NodeListDB {
    Connection connection = null;
    AESEncryption encryption = new AESEncryption();
    public NodeListDB() throws SQLException, IOException {
        connectDb();
        Statement statement = connection.createStatement();
        ResultSet resultSet = connection.getMetaData().getTables(null, null, "node_list", null);
        String basePath = System.getProperty("user.dir");
        if (!resultSet.next()) {
            String sql = Files.readString(Paths.get(basePath + "\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\SQL\\NodeList.sql"));
            statement.execute(sql);
        }
    }

    public void connectDb(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.0.102:5432/postgres", "postgres", "1006");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC драйвер не найден!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных!(В NodeListDb укажите свои данные для подключения к бд))");
            e.printStackTrace();
        }
    }
    public ArrayList<String> getAllIp() throws Exception {
        connectDb();
        String sql = "SELECT * FROM node_list where is_active=true";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> IPs = new ArrayList<>();
        while (resultSet.next()) {
            IPs.add(encryption.decode(resultSet.getString("ip")));
        }
        connection.close();
        return IPs;
    }
    public boolean isCreated(String ip) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        connectDb();

        String sql = "SELECT * FROM node_list where ip=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,encryption.encode(ip));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()){
            if (getLastIp()==null) return false;
            preparedStatement.setString(1,encryption.encode(getLastIp()));
            resultSet = preparedStatement.executeQuery();
            connection.close();
            if (!resultSet.next()) return false;
            updateIp(ip);
            return true;
            }

        connection.close();
        return true;
    }
    public void updateIp(String ip) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        connectDb();
        String sql = "UPDATE node_list SET ip = ? WHERE ip = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, encryption.encode(ip));
        preparedStatement.setString(2,encryption.encode(getLastIp()));
        preparedStatement.execute();
        connection.close();
        setLastIp(ip);
    }
    public String getRandomIp() throws Exception {
        ArrayList<String> IPs = getAllIp();
        if (IPs.isEmpty()) return null;
        IpConfigParser ipConfigParser = new IpConfigParser();
        String ipAddress = ipConfigParser.getIpAddress();
        if (IPs.size()==1 && IPs.get(0).equals(ipAddress)) return null;
        Random random = new Random();
        int randomIndex = random.nextInt(IPs.size());
        while (IPs.get(randomIndex).equals(ipAddress)){
            if (IPs.isEmpty()) return null;
            randomIndex=random.nextInt(IPs.size());
        }
        connection.close();
        return IPs.get(randomIndex);
    }
    public void addNode(String ip) throws Exception {
        connectDb();
        String sql = "INSERT INTO node_list (ip,is_active) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,encryption.encode(ip));
        preparedStatement.setBoolean(2,false);
        preparedStatement.execute();
        connection.close();
    }
    public void editStatusActive(String ip,boolean newStatus) throws Exception {
        connectDb();
        String sql = "UPDATE node_list SET is_active=? where ip=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1,newStatus);
        preparedStatement.setString(2,encryption.encode(ip));
        preparedStatement.execute();
    }
    public String getLastIp() throws IOException {
        String basePath = System.getProperty("user.dir");
        String path = basePath+"\\src\\main\\java\\org\\example\\DB\\SQL\\Node\\lastIp.txt";
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        line = bufferedReader.readLine();
        bufferedReader.close();
        return line;
    }
    public void setLastIp(String ip) throws IOException {
        String basePath = System.getProperty("user.dir");
        String path = basePath+"\\src\\main\\java\\org\\example\\DB\\SQL\\Node\\lastIp.txt";
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(ip);
        bufferedWriter.close();
    }

    public static void main(String[] args) throws Exception {
        NodeListDB nodeListDB = new NodeListDB();
//        nodeListDB.addNode("192.343.534.45453");
//        nodeListDB.editStatusActive("192.343.534.45453",tr
        System.out.println(nodeListDB.isCreated("192.168.0.102"));


    }
}
