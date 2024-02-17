package de.bukkitnews.hotpotato.maps;

import de.bukkitnews.hotpotato.HotPotato;
import de.bukkitnews.hotpotato.utils.PotatoConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Voting {

    private HotPotato hotPotato;
    private List<Map> mapList;
    private Map[] votingMaps;

    public Voting(HotPotato hotPotato, List<Map> mapList){
        this.hotPotato = hotPotato;
        this.mapList = mapList;
        votingMaps = new Map[PotatoConstants.VOTING_MAP_AMOUNT];

        chooseRandomMaps();
    }

    /*
    CHOOSING RANDOM MAPS FOR MAP VOTING
     */
    private void chooseRandomMaps(){
        for(int i = 0; i < votingMaps.length; i++){
            Collections.shuffle(mapList);
            votingMaps[i] = mapList.remove(0);
        }
    }

    /*
    METHOD FOR GETTING MAP WITH MOST VOTES
     */
    public Map getWinnerMap(){
        Map map = votingMaps[0];
        for(int i = 1; i < votingMaps.length; i++){
            if(votingMaps[i].getVotes() >= map.getVotes())
                map = votingMaps[i];
        }
        return map;
    }
}
