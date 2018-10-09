package net.comorevi.cosse.warpapi;

import cn.nukkit.plugin.PluginBase;
import itsu.mcbe.form.base.Form;

public class AdvancedWarpAPI extends PluginBase {

    private FormProcessor formProcessor;
    private SQLite3DataProvider provider;

    //////////////////////
    ////    A P I     ////
    //////////////////////
    public static Form getFormMenu() {
        return null;
    }

    public static Form getFormWarp() {
        return null;
    }

    public static Form getFormAdd() {
        return null;
    }

    public static Form getFormRemove() {
        return null;
    }

    public FormProcessor getFormProcessor() {
        return null;
    }

    //////////////////////
    ////   NOT  API   ////
    //////////////////////
    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {
        this.getDataFolder().mkdirs();
        this.formProcessor = new FormProcessor();
        this.provider = new SQLite3DataProvider(this);
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        return;
    }

    private SQLite3DataProvider getProvider() {
        return this.provider;
    }

    private boolean passMatch(String value) {
        return false;
    }

    private String convertStringQuotationMark(String value) {
        return null;
    }
}
