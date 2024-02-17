package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;

import java.util.List;

public class Voting {

    private HotPotato hotPotato;
    private List<Map> mapList;

    public Voting(HotPotato hotPotato, List<Map> mapList){
        this.hotPotato = hotPotato;
        this.mapList = mapList;
    }
}
