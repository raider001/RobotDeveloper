package com.kalynx.robotdeveloper.datastructure;

import com.kalynx.robotdeveloper.configuration.ConfigWriter;
import com.kalynx.robotdeveloper.configuration.PalletConfig;
import com.kalynx.robotdeveloper.notify.NotifyController;
import com.kalynx.robotdeveloper.notify.NotifyKey;

import java.awt.*;
import java.util.function.Consumer;

public class TextFormatModel {
    private final NotifyKey<PalletConfig> PALLET_UPDATED_KEY = new NotifyKey<>();
    private final NotifyController notifyController = new NotifyController();
    private PalletConfig palletConfig = new PalletConfig(Color.WHITE, Color.GREEN, Color.RED, Color.ORANGE, Color.LIGHT_GRAY, Color.MAGENTA, Color.PINK, Color.GRAY, 10);
    private PalletConfig config;

    public TextFormatModel() {
        PalletConfig c = ConfigWriter.loadConfig(PalletConfig.class);
        if(c == null) {
            c = new PalletConfig(Color.WHITE, Color.GREEN, Color.RED, Color.ORANGE, Color.LIGHT_GRAY, Color.MAGENTA, Color.PINK, Color.GRAY, 10);
        }
        config = c;
    }

    public void setModel(PalletConfig config) {
        this.config = config;
        ConfigWriter.writeConfig(config);
    }

    public PalletConfig getModel() {
        return config;
    }

    public void addModelChangeNotifier(Consumer<PalletConfig> listener) {
        notifyController.addListener(PALLET_UPDATED_KEY, listener);
        listener.accept(palletConfig);
    }

    public void removeModelChangeNotifier(Consumer<PalletConfig> listener) {
        notifyController.removeListener(PALLET_UPDATED_KEY, listener);
    }
}
