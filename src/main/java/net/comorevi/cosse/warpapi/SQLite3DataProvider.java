package net.comorevi.cosse.warpapi;

import cn.nukkit.level.Position;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class SQLite3DataProvider {

    public static final int POINT_TYPE_PUBLIC = 0;
    public static final int POINT_TYPE_PRIVATE = 1;
    public static final int POINT_TYPE_PASS_LIMITED = 2;

    private AdvancedWarpAPI plugin;
    private Statement statement;

    public SQLite3DataProvider(AdvancedWarpAPI plugin) {
        this.plugin = plugin;
        this.connectSQL();
    }

    private void connectSQL() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = null;
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().toString() + "/DataDB.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS OriginalPoint (" +
                            " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                            " owner TEXT NOT NULL," +
                            " name TEXT NOT NULL," +
                            " x INTEGER NOT NULL," +
                            " y INTEGER NOT NULL," +
                            " z INTEGER NOT NULL," +
                            " level TEXT NOT NULL," +
                            " type INTEGER NOT NULL," +
                            " pass TEXT )"
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 特定の名前のポイントが作成されているかどうかを確認する。
     *
     * @param pointname 存在するか確かめたいポイントの名前 , not {@code null}
     * @return 作成されていたら真、作成されていなければ偽を返す。
     * @throws Exception 引数がnullだった場合。
     */
    private boolean existsOriginalPointByPointName(String pointname) {
        return false;
    }

    /**
     * 特定のポイントを新規に作成する。引数の中にnullが存在する場合SQLエラーが発生する。
     *
     * @param username ポイントを作成するプレイヤーの名前 , not {@code null}
     * @param pointname 作成するポイントの名前 , not {@code null}
     * @param type 作成するポイントのタイプ（１から３） , not {@code null}
     * @param pos ポイントを作成するプレイヤーの座標 , not {@code null}
     * @throws Exception 引数がnullだった場合。
     */
    private void addOriginalPoint(String username, String pointname, int type, Position pos) {
        return;
    }

    /**
     * 指定された名前のポイントがあるか確認し、ポイントの作成者とユーザー名が一致するか確認して削除
     *
     * @param username ポイントを作成したプレイヤーの名前 , not {@code null}
     * @param pointname 削除する対象のポイントの名前 , not {@code null}
     * @throws Exception 引数がnullだった場合。
     */
    private void removeOriginalPoint(String username, String pointname) {
        return;
    }

    /**
     * 引数に渡されたプレイヤーが管理権を持つ（作成した）ポイントの一覧を返す。
     *
     * @param username 対象のプレイヤーの名前 , not {@code null}
     * @return 引数で渡されたプレイヤーが管理権を持つポイントのリスト
     * @throws Exception 引数がnullだった場合。
     */
    private List<String> getOwnOriginalPointByPlayerName(String username) {
        return null;
    }

    /**
     * パスワード設定で公開されているポイントの一覧を返す。
     *
     * @return パスワードが設定されているポイントのリストを返す。
     */
    private List<String> getPassLimitedPointByPlayerName() {
        return null;
    }

    /**
     * ポイントのタイプが公開で作成されているポイントの一覧を返す。
     *
     * @return 公開設定のポイントのリストを返す。
     */
    private List<String> getPublicOriginalPoint() {
        return null;
    }
}
