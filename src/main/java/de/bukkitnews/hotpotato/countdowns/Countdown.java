package de.bukkitnews.hotpotato.countdowns;

/*
COUNTDOWN CLASS WILL BE THE TOP CLASS OF ALL RUNNING INGAME COUNTDOWNS
 */
public abstract class Countdown {

    protected int taskID;

    public abstract void start();
    public abstract void stop();
}
