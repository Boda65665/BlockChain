package org.example.DB.SQL.Wallets;

import org.example.Cryptography.AESEncryption;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Wallet;
import org.example.NodeCommunication.IpConfigParser;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class WalletDB {
    Connection connection = null;
    private final AESEncryption encryption = new AESEncryption();
    private final IpConfigParser ipConfigParser = new IpConfigParser();
    public WalletDB() throws SQLException, IOException, ClassNotFoundException {
        connectDb();
        Statement statement = connection.createStatement();
        ResultSet resultSet = connection.getMetaData().getTables(null, null, "wallets", null);
        String basePath = System.getProperty("user.dir");
        if (!resultSet.next()) {
            String sql = Files.readString(Paths.get(basePath + "\\src\\main\\java\\org\\example\\DB\\SQL\\Wallets\\Wallets.sql"));
            statement.execute(sql);
        }
    }
    public void connectDb() {

        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\DB\\SQL\\wallets.db";
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\shors\\IdeaProjects\\noda_app\\node\\src\\main\\java\\org\\example\\DB\\SQL\\Wallets\\Wallets.db");
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных!");
            e.printStackTrace();
        }
    }
    public void createNewWallet(String publicKey,String secretPhrase,String privateKey,String password) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        connectDb();
        String sql = "INSERT INTO wallets (address,private_key,secret_phrase,password) VALUES (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,publicKey);
        stmt.setString(2,encryption.encode(privateKey));
        stmt.setString(3,encryption.encode(secretPhrase));
        stmt.setString(4,new HashEncoder().SHA256(password));
        stmt.execute();
    }
    public void createNewWallet(Wallet wallet) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        connectDb();
        String sql = "INSERT INTO wallets (address,private_key,secret_phrase,password) VALUES (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,wallet.getPublicKey());
        stmt.setString(2,encryption.encode(wallet.getPrivateKey()));
        stmt.setString(3,encryption.encode(wallet.getSecretPhrase()));
        stmt.setString(4,new HashEncoder().SHA256(wallet.getPassword()));
        stmt.execute();
    }
        public ArrayList<Wallet> getAllWallets() throws SQLException {
        ArrayList<Wallet> wallets = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from wallets");
        while (resultSet.next()){
            Wallet wallet = new Wallet(resultSet.getString("password"),resultSet.getString("address"),resultSet.getString("private_key"),resultSet.getString("secret_phrase"));
            wallets.add(wallet);
        }
        return wallets;
    }
    public Wallet getWalletByAddress(String address) throws SQLException {
        String sql = "select * from wallets where address=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,address);
        ResultSet resultSet = statement.executeQuery();
        return new Wallet(resultSet.getString("password"),resultSet.getString("address"),resultSet.getString("private_key"),resultSet.getString("secret_phrase"));
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        WalletDB walletDB = new WalletDB();
        walletDB.createNewWallet("f","s","fs","d");


    }
}
