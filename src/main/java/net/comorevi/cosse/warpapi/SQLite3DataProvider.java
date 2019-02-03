package net.comorevi.cosse.warpapi;

import cn.nukkit.level.Position;

import java.sql.*;
import java.util.*;

public class SQLite3DataProvider {

    private AdvancedWarpAPI plugin;
    private Connection connection = null;

    public SQLite3DataProvider(AdvancedWarpAPI plugin) {
        this.plugin = plugin;
        this.connectSQL();
    }

    private void connectSQL() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().toString() + "/DataDB.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS OriginalPoint (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " owner TEXT NOT NULL," +
                            " name TEXT NOT NULL," +
                            " x INTEGER NOT NULL," +
                            " y INTEGER NOT NULL," +
                            " z INTEGER NOT NULL," +
                            " level TEXT NOT NULL," +
                            " type INTEGER NOT NULL," +
                            " pass TEXT )"
            );
            statement.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void disConnectSQL() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 特定の名前のポイントが作成されているかどうかを確認する。
     *
     * @param pointname 存在するか確かめたいポイントの名前 , not {@code null}
     * @return 作成されていたら真、作成されていなければ偽を返す。
     * @throws Exception 引数がnullだった場合。
     */
    public boolean existsOriginalPointByPointName(String pointname) {
        try {
            String sql = "SELECT name FROM OriginalPoint WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setQueryTimeout(30);
            statement.setString(1, pointname);

            boolean result = statement.executeQuery().next();
            statement.close();

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 特定のポイントを新規に作成する。ポイント名が既存のものである場合処理を行わないため、呼び出す前に既に同じ名前のポイントが存在しないか確認するプロセスを挟むこと。
     *
     * @param username ポイントを作成するプレイヤーの名前 , not {@code null}
     * @param pointname 作成するポイントの名前 , not {@code null}
     * @param type 作成するポイントのタイプ（１から３） , not {@code null}
     * @param pos ポイントを作成するプレイヤーの座標 , not {@code null}
     * @throws Exception 引数がnullだった場合。
     */
    public void addOriginalPoint(String username, String pointname, Position pos, int type, String pass) {
        try {
            if (existsOriginalPointByPointName(pointname)) return;

            String sql = "INSERT INTO OriginalPoint (owner, name, x, y, z, level, type, pass) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, pointname);
            statement.setInt(3, (int) pos.x);
            statement.setInt(4, (int) pos.y);
            statement.setInt(5, (int) pos.z);
            statement.setString(6, pos.level.getName());
            statement.setInt(7, type);
            statement.setString(8, pass);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定された名前のポイントがあるか確認し、ポイントの作成者とユーザー名が一致するか確認して削除
     *
     * @param username ポイントを作成したプレイヤーの名前 , not {@code null}
     * @param pointname 削除する対象のポイントの名前 , not {@code null}
     * @throws Exception 引数がnullだった場合。
     */
    public void removeOriginalPoint(String username, String pointname) {
        try {
            if (!existsOriginalPointByPointName(pointname)) return;

            String sql = "DELETE FROM OriginalPoint WHERE owner = ? AND name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, pointname);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * pointnameのポイントのデータを取得します。
     *
     * @param pointname データを取得したいポイントの名前 , not {@code null}
     * @return 指定されたポイントのデータが入ったマップ
     */
    public Map<String, Object> getOriginalPointData(String pointname) {
        try {
            if (!existsOriginalPointByPointName(pointname)) return Collections.emptyMap();

            String sql = "SELECT owner, name, x, z, y, level, type, pass WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, pointname);

            Map<String, Object> map = new LinkedHashMap<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                map.put("owner", rs.getString("owner"));
                map.put("name", rs.getString("name"));
                map.put("x", rs.getInt("x"));
                map.put("y", rs.getInt("y"));
                map.put("z", rs.getInt("z"));
                map.put("level", rs.getString("level"));
                map.put("type", rs.getInt("type"));
                map.put("pass", rs.getString("pass"));
            }

            statement.close();
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ポイント一覧を取得
     *
     * @return すべてのポイントの名前が入力されたリストを返す
     */
    public List<String> getAllOriginalPoint() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT name FROM OriginalPoint");
            List<String> list = new ArrayList<>();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            statement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 引数に渡されたプレイヤーが管理権を持つ（作成した）ポイントの一覧を返す。
     *
     * @param username 対象のプレイヤーの名前 , not {@code null}
     * @return 引数で渡されたプレイヤーが管理権を持つポイントのリスト
     * @throws Exception 引数がnullだった場合。
     */
    public List<String> getOwnOriginalPointByPlayerName(String username) {
        try {
            String sql = "SELECT name FROM OriginalPoint WHERE owner = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            List<String> list = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }

            statement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * パスワード設定で公開されているポイントの一覧を返す。
     *
     * @return パスワードが設定されているポイントのリストを返す。
     */
    public List<String> getPassLimitedPoint() {
        try {
            String sql = "SELECT name FROM OriginalPoint WHERE type = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, PointType.POINT_TYPE_PASS_LIMITED);

            List<String> list = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }

            statement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * ポイントのタイプが公開で作成されているポイントの一覧を返す。
     *
     * @return 公開設定のポイントのリストを返す。
     */
    public List<String> getPublicOriginalPoint() {
        try {
            String sql = "SELECT name FROM OriginalPoint WHERE type = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, PointType.POINT_TYPE_PUBLIC);

            List<String> list = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("name"));
            }

            statement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
