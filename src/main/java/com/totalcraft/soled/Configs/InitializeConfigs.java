package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Crates.CrateConfig;
import com.totalcraft.soled.Main;
import com.totalcraft.soled.PlayTIme.PlayTimeData;
import com.totalcraft.soled.PlayerManager.MainPData;
import com.totalcraft.soled.PlayerManager.RewardData;
import com.totalcraft.soled.auction.ConfigAuction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static com.totalcraft.soled.Configs.BlockProtectData.clearProtectedBLocks;
import static com.totalcraft.soled.Configs.ItemPrivateLog.saveLogItem;
import static com.totalcraft.soled.Configs.MainConfig.*;
import static com.totalcraft.soled.Main.ServerTest;
import static com.totalcraft.soled.Utils.ChestShopUtils.ItemsCancelled;

public class InitializeConfigs {

    private final Main main;
    public InitializeConfigs(Main main) {
        this.main = main;
    }
    public void onLoad() {
        MainConfig mainConfig = new MainConfig(main);
        mainConfig.setConfigs();
    }
    public void onEnable() {
        new PriceItems(main).getConfigPrice();
        PriceItems.saveConfigPrice();
        new BlockProtectData(main).loadProtectedBlocks();
        clearProtectedBLocks();
        new FilterBlockData(main).loadFilterBlock();
        configBanItem();
        configBlockLimit();
        configCancelSign();
        new ItemPrivateLog(main).loadLogItem();
        saveLogItem();
        new AddressLog(main).loadAddressLog();
        new ConfigAuction(main).loadAuction();
        if (!ServerTest) new CrateConfig(main).loadCrate();
        new PlayTimeData().loadPlayTime();
        new MainPData().LoadPData();
        new RewardData().loadRewardData();
    }

    public void configBanItem() {
        banItemFile = new File(main.getDataFolder(), "banitem.yml");
        banItemConfig = YamlConfiguration.loadConfiguration(banItemFile);
        banItemList = banItemConfig.getStringList("ItensBans");
        banItemConfig.options().header("Itens Banidos do Servidor\n");
    }

    public void configBlockLimit() {
        blockLimitFile = new File(main.getDataFolder(), "blocklimit.yml");
        blockLimitConfig = YamlConfiguration.loadConfiguration(blockLimitFile);
        blockLimit = blockLimitConfig.getStringList("blocksLimit");
        blockLimitConfig.set("blocksLimit", blockLimit);
        blockLimitConfig.options().header("Blocos de limites que serão reconhecidos ao manipular o GriefPrevention\n");
    }

    public void configCancelSign() {
        CancelSignFile = new File(main.getDataFolder(), "CancelSign.yml");
        CancelSignConfig = YamlConfiguration.loadConfiguration(CancelSignFile);
        ItemsCancelled = CancelSignConfig.getStringList("Itens Cancelados");
        CancelSignConfig.set("Itens Cancelados", ItemsCancelled);
        CancelSignConfig.options().header("Itens Cancelados em Criação de Placa de Loja");
        try {
            CancelSignConfig.save(CancelSignFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
;