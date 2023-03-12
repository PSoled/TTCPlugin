package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;

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
    }
}
