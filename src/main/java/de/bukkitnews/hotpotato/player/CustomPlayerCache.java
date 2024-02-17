package de.bukkitnews.hotpotato.player;

import java.util.Locale;

/*
CUSTOMPLAYERCACHE IS STORING PLAYER INGAME DATA FROM SQL E.G STATS, ...
 */
public class CustomPlayerCache {

    private boolean isIngame;
    private int playedGames;
    private int wonGames;
    private String locale;

    public CustomPlayerCache(boolean isIngame, int playedGames, int wonGames, String locale){
        this.isIngame = isIngame;
        this.playedGames = playedGames;
        this.wonGames = wonGames;
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
