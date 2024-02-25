package org.example.BlockChainBase.DB.SQL.BlockChainInfo;


import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class BlockChainInfoBD {
    Connection connection = null;
    IpConfigParser ipConfigParser = new IpConfigParser();
    NodeListDB nodeListDB = new NodeListDB();
    AESEncryption encryption = new AESEncryption();
    public record BlockChainInfoStruct(String lastHsh,int height){
        public BlockChainInfoStruct(String lastHsh, int height) {
            this.lastHsh = lastHsh;
            this.height = height;
        }
    }

    public BlockChainInfoBD() throws SQLException, IOException, ClassNotFoundException {
        connectDb();
        Statement statement = connection.createStatement();
        ResultSet resultSet = connection.getMetaData().getTables(null, null, "block_chain_info", null);
        String basePath = System.getProperty("user.dir");
        if (!resultSet.next()) {
            String sql = Files.readString(Paths.get(basePath + "\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\SQL\\BlockChainInfo\\BlockChainInfo.sql"));
            statement.execute(sql);
        }

        connection.close();
    }
    public void connectDb() {
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\SQL\\BlockChainInfo\\BlockChainInfo.db";
            connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных!");
            e.printStackTrace();
        }
    }
    public void addInfo(String lastHash,int height) throws Exception {
        if (!isAddingInfo()) {

            connectDb();
            String sql = "INSERT INTO block_chain_info (last_hash,height) VALUES (?,?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, encryption.encode(lastHash));
            stmt.setString(2, encryption.encode(String.valueOf(height)));
            stmt.execute();
            connection.close();
        }
        else {
            updateInfo(lastHash,height);
        }
    }
    public boolean isAddingInfo() throws SQLException {

        connectDb();
        String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END AS result FROM block_chain_info;";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next(); // Переходим к первой строке результата
        String result = resultSet.getString("result"); // Получаем значение столбца "result"
        boolean isAddingInfo = Boolean.parseBoolean(result); // Преобразуем строку в boolean
        connection.close();
        return isAddingInfo;
    }
    public void updateInfo(String lastHash,int height) throws Exception {
        connectDb();

        String sql = "UPDATE block_chain_info SET last_hash=?, height=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,encryption.encode(lastHash));
        stmt.setString(2, encryption.encode(String.valueOf(height)));
        stmt.execute();
        connection.close();
    }
    public BlockChainInfoStruct getBlockChainInfo() throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (!isAddingInfo())return null;
        connectDb();
        String sql = "SELECT * FROM block_chain_info";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        BlockChainInfoStruct blockChainInfoStruct = new BlockChainInfoStruct(encryption.decode(resultSet.getString("last_hash")),Integer.parseInt(encryption.decode(resultSet.getString("height"))));
        connection.close();
        return blockChainInfoStruct;
    }
public static void main(String[] args) throws Exception {
    BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    System.out.println(blockChainInfoBD.getBlockChainInfo());
}

}
