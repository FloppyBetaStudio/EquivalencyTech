package io.github.sefiraat.equivalencytech.configuration;

import io.github.sefiraat.equivalencytech.EquivalencyTech;

public class Config {

    private final ConfigStrings strings;
    private final ConfigEMC emc;
    private final ConfigBooleans bools;

    public ConfigStrings getStrings() {
        return strings;
    }
    public ConfigEMC getEmc() {
        return emc;
    }
    public ConfigBooleans getBools() {
        return bools;
    }

    public Config(EquivalencyTech plugin) {
        strings = new ConfigStrings(plugin);
        emc = new ConfigEMC(plugin);
        bools = new ConfigBooleans(plugin);
    }
}