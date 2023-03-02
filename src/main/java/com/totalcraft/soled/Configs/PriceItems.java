package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.totalcraft.soled.Utils.VenderUtils.priceItems;

public class PriceItems {
    private final Main main;

    public PriceItems(Main main) {
        this.main = main;
    }

    public static File PriceFile;
    public static YamlConfiguration PriceConfig;

    public void getConfigPrice() {
        PriceFile = new File(main.getDataFolder(), "priceitems.yml");
        PriceConfig = YamlConfiguration.loadConfiguration(PriceFile);

        PriceConfig.options().header("-- Valor dos itens do comando /Vender --\n\niD : MetaId : Valor");
        Set<String> keys = PriceConfig.getKeys(false);
        for (String key : keys) {
            double value = PriceConfig.getDouble(key);
            priceItems.put(key, value);
        }
    }

    public static void saveConfigPrice() {
        for (Map.Entry<String, Double> entry : priceItems.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            PriceConfig.set(key, value);
        }

        try {
            PriceConfig.save(PriceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
