package com.android.internal.util;

import android.util.Log;

//import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Freecom on 2015/8/5.
 */
public class MemoryInfo {
    private final static String[][] FlashInfo={
            //Format:    "CID", "Vendor OurCompanyCode CodeOfVendor DDR2/3", "RomCapacity"    
            {"700100454838434434",  "KINGSTON  H-1401-08G04G-003\n08EMCP04-EL3BT227 DDR3",   "8"},//08EMCP04-EL3BT227,KINGSTON 
            {"150100523832314d42",  "Samsung H-1401-16G16G-003\nKMR820001M_B609 DDR3",   "16"},//KMR820001M_B609 FS031-WK
            {"a80111303546484d42",  "LEAHKINN  H-1401-08G04G-004\nLTLQ0505FS_HMd1_MB DDR3",   "8"},//LTLQ0505FS_HMd1_MB,LEAHKINN 
            {"13014e52314a39364e",  "Micron H-1401-16G16G-005\nSUTJ96NZZ5D6YKFAH-125BT DDR3",  "16"}, // fs031 wk  SUTJ96NZZ5D6YKFAH-125BT,Spectek
            {"700100454838454134",  "KINGSTON H-1401-08G04G-005\n08EMCP04-NL2DT227 DDR2",   "4"},//FS072 CF1  Logicom
            {"150100513832334d42",  "Samsung H-1401-16G16G-007\nKMQ820013M-B419 DDR3",  "16"}, // fs031 trx
            {"90014a483847316505",  "HYNIX H-1401-08G08G-009\nH-1401-08G08G-012 DDR3",     "8"},  //H9TP64A8JDMCPR_KGM ,H9TQ64A8GTMCUR_KUM
            {"150100463832324d42",  "Samsung H-1401-16G08G-009\nKMF820012M-B305 DDR3",   "16"},//KMF820012M-B305 FS032 CF2
            {"700100563130303038",  "KINGSTON H-1401-08G08G-010 08EMCP08-NL2CV100 DDR2\nH-1401-08G08G-014 08EMCP08-EL3CV100 DDR3\nH-1401-08G08G-001"	,  "8"},  //08EMCP08-NL2CV100
            {"450100445331303136",  "SANDISK H-1401-16G08G-011\nSDADB48K-16G DDR3",     "16"}, //SDADB48K-16G,  fs089 TRX
            {"110100303038473730",  "TOSHIBA H-1401-08G08G-015\nTYD0GH121661RA DDR3",  "8"}, // fs030 ark
            {"450100445332303038",  "SANDISK H-1401-08G08G-016\nSD9DS28K-8G DDR3",   "8"},//SD9DS28K-8G FS031-CF2
            {"450100445331303038",  "SANDISK H-1401-08G08G-016\nSD9DS28K-8G DDR3",   "8"},//SD9DS28K-8G FS086-CF8
            {"fe014e50314a353541",  "Micron H-1401-08G08G-017\nSUTJ95KZZ8D5BKFAH-125BT DDR3",    "8"},  //SUTJ95KZZ8D5BKFAH-125BT,Spectek FS031-CF2
            {"90014a484147326505",  "RD H-1401-16G16G-017\nH9TQ17ABJTMCUR-KUM DDR3",	 "16"},	//nH9TQ17ABJTMCUR-KUM,RD,test pass by ShenZhen
            {"fe014e50314a39354c",  "Micron H-1401-0008GB-018\nSXTJ95KZZ8D5BKFAH-125BT DDR3",    "8"},  //SUTJ95KZZ8D5BKFAH-125BT,Spectek FS031-CF2
            {"110100303038473330",  "TOSHIBA H-1401-08G08G-018\nTYD0GH221664RA DDR3",  "8"}, // fs031 GKD
            {"fe014e50314a39354b",  "MICRON H-1401-08G08G-019\nMT29TZZZ8D5BKFAH-125 DDR3",   "8"},//MT29TZZZ8D5BKFAH-125 W.95K,MICRON   64+8 FS087 CF8
            {"700100454838434538",  "KINGSTON-1401-08G08G-020\n08EMCP08-EL3BT227 DDR3",   "8"},//EMMCP(8GB eMMC5.1 + 8Gb LPDDR3 SDRAM),221FBGA,11.5x13.0x1.0mm,08EMCP08-EL3BT227,KINGSTON
            {"150100463732324d42",  "SAMSUNG H-1401-08G08G-021\nKMF720012M-B214 DDR3",   "8"},//H-1401-08G08G-021 KMF720012M-B214 LPDDR3 SAMSUNG 150100463732324d42001eff9a16a300
            {"700100454838454538",  "KINGSTON H-1401-08G08G-023\n08EMCP08-NL3DT227 DDR3",   "8"},//,08EMCP08-NL3DT227,KINGSTON
            {"150100464e58324d42",  "SAMSUNG H-1401-08G08G-024\nKMFNX0012M-B214 DDR3 ",   "8"},///FS089    (P)(T)IC,EMMCP(8GB eMMC5.1 +8Gb LPDDR3 SDRAM),221FBGA,11.5x13.0x1.0mm,KMFNX0012M-B214,SAMSUNG 
            {"110100303034474530",  "TOSHIBA H-1401-04G04G-025\nTYC0FH121638RA DDR2",   "4"},//TOSHIBA  FS087_CF12  32+4
            {"700100533130303034",  "KINGSTON H-1401-04G04G-026\n04EMCP04-NL2AS100 DDR2",  "4"},  //04EMCP04-NL2AS100, fs062 NM
            {"90014a483847346192",  "HYNIX H-1401-08G08G-026\nH9TQ64A8GTACUR-KUM DDR3",   "4"},//8GB eMMC5.1 +8Gb LPDDR3 SDRAM),221FBGA,11.5x13.0x1.0mm,H9TQ64A8GTACUR-KUM,HYNIX FS070-CF1
            {"150100463558354342",  "SAMSUNG H-1401-04G04G-027\nKMF5X0005C-B211 DDR3",   "4"},//SAMSUNG  FS087-CF8   32+4
            {"90014a483447326111",  "HYNIX H-1401-04G04G-028\nH9TP32A4GDDCPR-KGM DDR2",   "4"},//4Gb LPDDR2-S4B(x32)),162Ball FBGA,11.5x13.0mmx1.0mm, H9TP32A4GDDCPR-KGM,HYNIX  FS070_CF1
            {"150100514531334d42",  "SAMSUNG H-1401-16G16G-029\nKMQE10013M-B318 DDR3 ",   "16"},///FS089    16GB eMMC5.1 +16Gb(8Gbx2) LPDDR3 SDRAM),221FBGA,11.5x13.0x1.0mm,KMQE10013M-B318
            {"150100464a32354142",  "SAMSUNG H-1401-04G04G-031\nKMFJ20005A-B213 DDR3",   "4"},//KMFJ20005A-B213,Samsung   32+4 FS072 CF1
            {"fe014e50314a39344d",  "Micron H-1401-04G04G-032\nMT29TZZZ4D4BKERL-125-W.94M DDR3",   "4"},//EMMCP(4GB eMMC4.51 + 4Gbx32 LPDDR3 MCP)  FS087_CF12
            {"1501004e4a325a4d42",  "Samsung H-1401-04G04G-033\nKMNJ2000ZM-B207 DDR2",     "4"}, //KMNJ2000ZM-B207,  fs046 CF1
            {"700100454534454134",  "KINGSTON H-1401-04G04G-034\n04EMCP04-NL2DM627 DDR2",   "4"},//FS285-CF1 )IC,EMMCP(4GB eMMC5.0 + 4Gb LPDDR2 SDRAM),162FBGA,11.5x13.0x1.0mm,04EMCP04-NL2DM627-B01,KINGSTON
            {"c80111344d45594d41",  "GIGADEVICE H-1401-04G04G-036 GD9D4M4EYKM-A6 DDR2\nH-1401-04G04G-037 GD9D4M4JYTM-A9 DDR3",   "4"},///FS070  	EMMCP(4GB eMMC5.0 + 4Gb LPDDR2 SDRAM),162FBGA,11.5x13.0x1.0mm,GD9D4M4EYKM-A6,GIGADEVICE   ,GD9D4M4JYTM-A9,GIGADEVICE

            //  no code
            {"13014e51324a393554",  "Micron H-1401-08G08G-XXX MT29TZZZ8D5JKEPD-125 W.95T DDR3\nSpetek H-1401-08G08G-XXX J95T DDR3",   "8"},///

            //  Discrete flash    
            {"8801034e4361726420",  "FORESEE H-1401-0008GB-022/023/030/036 \nNCEMASD9-08G DDR3\nNCLD3B2256M32 DDR3\nNCEMAD7B-08G DDR3\nNCEMAM6G-08G DDR3",   "8"}, //FORESEE 8801034e436172642030ab0db3224300 ROM:H-1401-0008GB-022 NCEMASD9-08G RAM:H-1401-0008GB-023 NCLD3B2256M32

			{"700100454841434538",  "KINGSTON H-1401-16G08G-012\n16EMCP08-EL3BT527 DDR3",   "16"},//FS088-ZYX IC,EMMCP(16GB eMMC5.0 + 8Gb(4Gbx2) LPDDR3 SDRAM),221FBGA,11.5x13.0x1.0mm,16EMCP08-EL3BT527,KINGSTON
			{"150100464531324d42",  "SAMSUNG H-1401-16G08G-013\nKMFE10012M-B214 DDR3",   "16"},//FS088-ZYX IC,EMMCP(16GB eMMC5.1 +8Gb LPDDR3 SDRAM),221FBGA,11.5x13..0x1.0mm,KMFE10012M-B214,SAMSUNG
    };

    private static int getFlashIndex() {
        int index = -1;

        String cid = getMemoryCid();
        for(int i = 0; i < FlashInfo.length; i++){
            if(cid.contains(FlashInfo[i][0])){
                index = i;
                break;
            }
        }

        return index;
    }

    public static String getMemorySize() {
        String error = "Get info error!";
        int index = getFlashIndex();
        if(index == -1) {
            return error;
        }

        return FlashInfo[index][2] + "GB";
    }

    public static long getMemoryByte() {
        long error = 0;
        int index = getFlashIndex();
        if(index == -1) {
            return error;
        }

        return Long.parseLong(FlashInfo[index][2]) * 1024 *1024 * 1024;
    }

    public static String getMemoryInfo() {
        String error = "Get info error!";
        String info = "------------------------------------------\n[EMMC] : "+ getMemoryName() + getMemoryCid();
        int index = getFlashIndex();
        if(index == -1) {
            return info;
        }

        return info + FlashInfo[index][1];
    }

    public static String getMemoryName() {
        return readfile("/sys/bus/mmc/devices/mmc0:0001/name");
    }

    public static String getMemoryCid() {
        return readfile("/sys/bus/mmc/devices/mmc0:0001/cid");
    }

    private static String readfile(String fileName) {
        String res="Unknown";
        File mFile= new File(fileName);
        try
        {
            if (mFile.exists()) {
                Log.d("avar", fileName + " is exist!");
                FileInputStream is = new FileInputStream(fileName);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
                int length = is.available();
                byte buffer[] = new byte[1024];
                if(bufferedInputStream.read(buffer)!=-1)
                {
                  // res = EncodingUtils.getString(buffer, "UTF-8");
                  res =new String(buffer,"UTF-8");
                }
                bufferedInputStream.close();
            }
        }
        catch (Exception e) {
            Log.d("avar", fileName+" is not exist!");
            e.printStackTrace();
        }
        return res;
    }
}
