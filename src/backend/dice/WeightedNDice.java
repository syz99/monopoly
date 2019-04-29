package backend.dice;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeightedNDice extends AbstractDice {
    private Map<Integer, Integer> sideToWeight;

    public WeightedNDice(int nSides, Map<Integer, Integer> sideToWeight) {
        super(nSides);
        this.sideToWeight = sideToWeight;
    }

    @Override
    public int roll(){
        Map<Integer, Integer> valueToSide = new HashMap<>(  );
        Integer lowerLimit = 0;
        for(int side: sideToWeight.keySet()){
            Integer pseudoNum = sideToWeight.get( side );
            for(int i = 0; i < pseudoNum; i++){
                valueToSide.put( side, lowerLimit + i);
            }
        }
        Integer random = new Random().nextInt(getNumStates());
        return valueToSide.get( random );
    }




}
