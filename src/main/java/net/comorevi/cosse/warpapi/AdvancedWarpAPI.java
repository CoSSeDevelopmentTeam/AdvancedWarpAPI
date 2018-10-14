package net.comorevi.cosse.warpapi;

import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;

import java.util.List;
import java.util.Map;

public class AdvancedWarpAPI extends PluginBase {

    private static AdvancedWarpAPI instance;
    private SQLite3DataProvider provider;

    //////////////////////
    ////    A P I     ////
    //////////////////////
    public static AdvancedWarpAPI getInstance() {
        return instance;
    }

    public boolean exsistsPoint(String pointname) {
        return getProvider().existsOriginalPointByPointName(pointname);
    }

    public void addPoint(String username, String pointname, Position pos, int type, String pass) {
        getProvider().addOriginalPoint(username, pointname, pos, type, pass);
    }

    public void removePoint(String username, String pointname) {
        getProvider().removeOriginalPoint(username, pointname);
    }

    public Map<String, Object> getPointData(String pointname) {
        return getProvider().getOriginalPointData(pointname);
    }

    public List<String> getPointNameList() {
        return getProvider().getAllOriginalPoint();
    }

    public List<String> getOwnPointNameList(String username) {
        return getProvider().getOwnOriginalPointByPlayerName(username);
    }

    public List<String> getPassLimitedPointNameList() {
        return getProvider().getPassLimitedPoint();
    }

    public List<String> getPublicPointNameList() {
        return getProvider().getPublicOriginalPoint();
    }

    public Position getDestinationPositon(String pointname) {
        Map<String, Object> pointMap = getProvider().getOriginalPointData(pointname);
        Position pos = new Position((int) pointMap.get("x"), (int) pointMap.get("y"), (int) pointMap.get("z"), this.getServer().getLevelByName(String.valueOf(pointMap.get("level"))));
        return pos;
    }

    //////////////////////
    ////   NOT  API   ////
    //////////////////////
    @Override
    public void onEnable() {
        initialize();
        instance = this;
    }

    @Override
    public void onDisable() {
        getProvider().disConnectSQL();
    }

    private void initialize() {
        this.getDataFolder().mkdirs();
        this.provider = new SQLite3DataProvider(this);
        return;
    }

    public SQLite3DataProvider getProvider() {
        return this.provider;
    }
}
