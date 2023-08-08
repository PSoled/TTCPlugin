package com.totalcraft.soled.Configs;

import com.totalcraft.soled.Main;

import java.io.FileWriter;
import java.io.IOException;

public class AddressLog {
    private final Main main;
    public AddressLog(Main main) {
        this.main = main;
    }
    private static FileWriter fileWriter;
    public void loadAddressLog() {
        try {
            fileWriter = new FileWriter(main.getDataFolder() + "/log/address.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAddressLog(String date, String address) {
        try {
            fileWriter.write(date + " " + address + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
