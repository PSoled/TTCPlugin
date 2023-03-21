package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static com.totalcraft.soled.Configs.BlockProtectData.clearProtectedBLocks;
import static com.totalcraft.soled.Configs.MainConfig.*;

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
        BflyData bflySave = new BflyData(main);
        bflySave.loadFlyData();

        JailData jailData = new JailData(main);
        jailData.loadJailData();

        PriceItems priceItems = new PriceItems(main);
        priceItems.getConfigPrice();
        PriceItems.saveConfigPrice();

        BlockProtectData blockProtectData = new BlockProtectData(main);
        blockProtectData.loadProtectedBlocks();
        clearProtectedBLocks();

        FilterBlockData filterBlockData = new FilterBlockData(main);
        filterBlockData.loadFilterBlock();

        configBanItem();
        configBlockLimit();
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
}
