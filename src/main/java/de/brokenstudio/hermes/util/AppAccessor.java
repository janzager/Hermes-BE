package de.brokenstudio.hermes.util;

import de.brokenstudio.hermes.app.Application;
import de.brokenstudio.hermes.config.Config;

public interface AppAccessor {

    default Config config(){
        return Application.app().getConfig();
    }

}
