package com.totalcraft.soled.auction;

import com.totalcraft.soled.Main;

import java.io.FileWriter;
import java.io.IOException;

public class AuctionLog {
    private final Main main;
    public AuctionLog(Main main) {
        this.main = main;
    }
    private static FileWriter fileWriter;
    public void loadAuctionLog() {
        try {
            fileWriter = new FileWriter(main.getDataFolder() + "/log/leilao.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAuctionLog(String log) {
        try {
            fileWriter.write(log + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
