package atlantis.production.strategies;

import atlantis.AtlantisConfig;
import atlantis.wrappers.SelectUnits;
import com.sun.javafx.css.SizeUnits;
import java.util.ArrayList;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class TerranProductionStrategy extends AtlantisProductionStrategy {

    @Override
    protected String getFilename() {
        return "TerranDefault.csv";
    }

    @Override
    public void produceWorker() {
        Unit building = SelectUnits.ourOneIdle(AtlantisConfig.BASE);
        if (building != null) {
            building.train(AtlantisConfig.WORKER);
        }
    }

    @Override
    public void produceInfantry(UnitType infantryType) {
        Unit building = SelectUnits.ourOneIdle(AtlantisConfig.BARRACKS);
        if (building != null) {
            building.train(infantryType);
        }
    }

    @Override
    public ArrayList<UnitType> produceWhenNoProductionOrders() {
        ArrayList<UnitType> units = new ArrayList<>();
        
        int marines = SelectUnits.our().countUnitsOfType(UnitType.UnitTypes.Terran_Marine);
        int medics = SelectUnits.our().countUnitsOfType(UnitType.UnitTypes.Terran_Medic);
        
        if ((double) marines / medics < 3) {
            units.add(UnitType.UnitTypes.Terran_Marine);
        }
        else {
            units.add(UnitType.UnitTypes.Terran_Medic);
        }
        return units;
    }

}
