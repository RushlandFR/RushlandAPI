package fr.aquazus.rushland.api.data;

public enum Leveling {
    
    LEVEL_0(0, 0, 0, 0, "§7", null),
    LEVEL_1(1, 7000, 7000, 100, "§7", null),
    LEVEL_2(2, 9000, 16000, 100, "§7", null),
    LEVEL_3(3, 11000, 27000, 100, "§7", null),
    LEVEL_4(4, 13000, 40000, 100, "§7", null),
    LEVEL_5(5, 15000, 55000, 0, "§8", "§eFélicitations ! Vous venez de débloquer le Kit Astronaute en PVPBox !"),
    LEVEL_6(6, 17000, 72000, 200, "§8", null),
    LEVEL_7(7, 19000, 91000, 200, "§8", null),
    LEVEL_8(8, 21000, 112000, 200, "§8", null),
    LEVEL_9(9, 23000, 135000, 200, "§8", null),
    LEVEL_10(10, 25000, 160000, 0, "§3", "§eFélicitations ! Vous venez de débloquer le Kit Robot en PVPBox !"),
    LEVEL_11(11, 27000, 187000, 300, "§3", null),
    LEVEL_12(12, 29000, 216000, 300, "§3", null),
    LEVEL_13(13, 31000, 247000, 300, "§3", null),
    LEVEL_14(14, 33000, 280000, 300, "§3", null),
    LEVEL_15(15, 35000, 315000, 0, "§3", "§eFélicitations ! Vous venez de débloquer un bonus en AntWars : §6Pelle en bois + 5 steaks dés le début §e!"),
    LEVEL_16(16, 37000, 352000, 400, "§3", null),
    LEVEL_17(17, 42000, 394000, 400, "§3", null),
    LEVEL_18(18, 47000, 441000, 400, "§3", null),
    LEVEL_19(19, 52000, 493000, 400, "§3", null),
    LEVEL_20(20, 57000, 550000, 0, "§2", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_21(21, 62000, 612000, 500, "§2", null),
    LEVEL_22(22, 67000, 679000, 500, "§2", null),
    LEVEL_23(23, 72000, 751000, 500, "§2", null),
    LEVEL_24(24, 77000, 828000, 500, "§2", null),
    LEVEL_25(25, 82000, 910000, 0, "§2", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_26(26, 87000, 997000, 600, "§2", null),
    LEVEL_27(27, 92000, 1089000, 600, "§2", null),
    LEVEL_28(28, 97000, 1186000, 600, "§2", null),
    LEVEL_29(29, 102000, 1288000, 600, "§2", null),
    LEVEL_30(30, 107000, 1395000, 0, "§9", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_31(31, 112000, 1507000, 700, "§9", null),
    LEVEL_32(32, 121000, 1628000, 700, "§9", null),
    LEVEL_33(33, 130000, 1758000, 700, "§9", null),
    LEVEL_34(34, 139000, 1897000, 700, "§9", null),
    LEVEL_35(35, 148000, 2045000, 0, "§9", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_36(36, 157000, 2202000, 800, "§9", null),
    LEVEL_37(37, 166000, 2368000, 800, "§9", null),
    LEVEL_38(38, 175000, 2543000, 800, "§9", null),
    LEVEL_39(39, 184000, 2727000, 800, "§9", null),
    LEVEL_40(40, 193000, 2920000, 0, "§b", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_41(41, 202000, 3122000, 900, "§b", null),
    LEVEL_42(42, 211000, 3333000, 900, "§b", null),
    LEVEL_43(43, 220000, 3553000, 900, "§b", null),
    LEVEL_44(44, 229000, 3782000, 900, "§b", null),
    LEVEL_45(45, 238000, 4020000, 0, "§b", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_46(46, 247000, 4267000, 1000, "§b", null),
    LEVEL_47(47, 256000, 4523000, 1000, "§b", null),
    LEVEL_48(48, 265000, 4788000, 1000, "§b", null),
    LEVEL_49(49, 274000, 5062000, 1000, "§b", null),
    LEVEL_50(50, 283000, 5345000, 0, "§a", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_51(51, 292000, 5637000, 1100, "§a", null),
    LEVEL_52(52, 301000, 5938000, 1000, "§a", null),
    LEVEL_53(53, 310000, 6248000, 1100, "§a", null),
    LEVEL_54(54, 319000, 6567000, 1100, "§a", null),
    LEVEL_55(55, 328000, 6895000, 0, "§a", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_56(56, 337000, 7232000, 1200, "§a", null),
    LEVEL_57(57, 346000, 7578000, 1200, "§a", null),
    LEVEL_58(58, 355000, 7933000, 1200, "§a", null),
    LEVEL_59(59, 364000, 8297000, 1200, "§a", null),
    LEVEL_60(60, 373000, 8670000, 0, "§e", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_61(61, 382000, 9052000, 1300, "§e", null),
    LEVEL_62(62, 391000, 9443000, 1300, "§e", null),
    LEVEL_63(63, 400000, 9843000, 1300, "§e", null),
    LEVEL_64(64, 409000, 10252000, 1300, "§e", null),
    LEVEL_65(65, 418000, 10670000, 0, "§e", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_66(66, 427000, 11097000, 1400, "§e", null),
    LEVEL_67(67, 436000, 11533000, 1400, "§e", null),
    LEVEL_68(68, 445000, 11978000, 1400, "§e", null),
    LEVEL_69(69, 454000, 12432000, 1400, "§e", null),
    LEVEL_70(70, 463000, 12895000, 0, "§6", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_71(71, 472000, 13367000, 1500, "§6", null),
    LEVEL_72(72, 481000, 13848000, 1500, "§6", null),
    LEVEL_73(73, 490000, 14338000, 1500, "§6", null),
    LEVEL_74(74, 499000, 14837000, 1500, "§6", null),
    LEVEL_75(75, 508000, 15345000, 0, "§6", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_76(76, 517000, 15862000, 1600, "§6", null),
    LEVEL_77(77, 526000, 16388000, 1600, "§6", null),
    LEVEL_78(78, 535000, 16923000, 1600, "§6", null),
    LEVEL_79(79, 544000, 17467000, 1600, "§6", null),
    LEVEL_80(80, 553000, 18020000, 0, "§c", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_81(81, 562000, 18582000, 1700, "§c", null),
    LEVEL_82(82, 571000, 19153000, 1700, "§c", null),
    LEVEL_83(83, 580000, 19733000, 1700, "§c", null),
    LEVEL_84(84, 589000, 23220000, 1700, "§c", null),
    LEVEL_85(85, 598000, 20920000, 0, "§c", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_86(86, 607000, 21527000, 1800, "§c", null),
    LEVEL_87(87, 616000, 22143000, 1800, "§c", null),
    LEVEL_88(88, 625000, 22768000, 1800, "§c", null),
    LEVEL_89(89, 634000, 23402000, 1800, "§c", null),
    LEVEL_90(90, 643000, 24045000, 0, "§5", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_91(91, 652000, 24697000, 1900, "§5", null),
    LEVEL_92(92, 661000, 25358000, 1900, "§5", null),
    LEVEL_93(93, 670000, 26028000, 1900, "§5", null),
    LEVEL_94(94, 679000, 26707000, 1900, "§5", null),
    LEVEL_95(95, 688000, 27395000, 0, "§5", "§cErreur, veuillez contacter immédiatement un membre du staff"),
    LEVEL_96(96, 697000, 28092000, 2000, "§5", null),
    LEVEL_97(97, 706000, 28798000, 2000, "§5", null),
    LEVEL_98(98, 715000, 29513000, 2000, "§5", null),
    LEVEL_99(99, 724000, 30237000, 2000, "§5", null),
    LEVEL_100(100, 733000, 30970000, 0, "§d", "§cErreur, veuillez contacter immédiatement un membre du staff");
    
    private int level;
    private int requiredXp;
    private int requiredCumulatedXp;
    private int rewardedShopcoins;
    private String chatColor;
    private String customLevelUpMessage;

    Leveling(int level, int requiredXp, int requiredCumulatedXp, int rewardedShopcoins, String chatColor, String customLevelUpMessage) {
        this.level = level;
        this.requiredXp = requiredXp;
        this.requiredCumulatedXp = requiredCumulatedXp;
        this.rewardedShopcoins = rewardedShopcoins;
        this.chatColor = chatColor;
        this.customLevelUpMessage = customLevelUpMessage;
    }

    public int getLevel() {
        return this.level;
    }
    
    public int getRequiredXp() {
        return this.requiredXp;
    }
    
    public int getRequiredCumulatedXp() {
        return this.requiredCumulatedXp;
    }
    
    public int getRewardedShopcoins() {
        return this.rewardedShopcoins;
    }
    
    public String getChatColor() {
        return this.chatColor;
    }
    
    public String getCustomLevelUpMessage() {
        return this.customLevelUpMessage;
    }
}

