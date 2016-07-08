package th.ac.mahidol.rama.emam.manager;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StoreManager {

    private String server=" testsql2008", db="emam", username="emam", passwords="emam@1";
    private String EMAM = "EMAM";


    private Connection CONN(String _username, String _passwords, String _db, String _server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        String JTDS_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

        Log.d("check", "username = "+_username);
        Log.d("check", "passwords = "+_passwords);
        Log.d("check", "db = "+_db);
        Log.d("check", "server = "+_server);

        try {
            Log.d("check", "Pre Connecting...");
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Log.d("check", "Connecting...");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"+ "databaseName=" + _db + ";user=" + _username + ";password="+ _passwords + ";";
            conn = DriverManager.getConnection(ConnURL);
            Log.d("check", "conn = "+conn);
        } catch (SQLException se) {
            Log.e("ERRO SQLException", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO ClassNotFoundException ", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO Exception", e.getMessage());
        }
        return conn;
    }

    public void selectData(){
        Connection connect;
        ResultSet rs;
        List<ListWardBean> result = new ArrayList<ListWardBean>();
        ListWardBean listWard = null;
        connect = CONN(username, passwords, db, server);
        int i = 0;
        try {
            PreparedStatement statement = connect.prepareStatement("EXEC [dbo].[ListWard] '" + EMAM + "'");
            final ArrayList list = new ArrayList();
            rs = statement.executeQuery();
            while (rs.next()) {
                listWard = new ListWardBean();
                listWard.setId(rs.getInt("id"));
                listWard.setWardname(rs.getString("wardName"));
                result.add(listWard);
                Log.d("check", "id = "+result.get(i));
                i++;
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {
            Log.d("check", "SQLException: "+ e);
        }
    }
}
