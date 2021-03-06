package atlantis.production.strategies;

import atlantis.AtlantisConfig;
import atlantis.buildings.managers.AtlantisBaseManager;
import atlantis.workers.AtlantisWorkerCommander;
import atlantis.workers.AtlantisWorkerManager;
import atlantis.wrappers.SelectUnits;
import java.util.ArrayList;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;

public class ZergProductionStrategy extends AtlantisProductionStrategy {

    @Override
    protected String getFilename() {
        return "ZergDefault.csv";
    }

    @Override
    public void produceWorker() {
        _produceUnit(AtlantisConfig.WORKER);
    }

    @Override
    public void produceInfantry(UnitType infantryType) {
        _produceUnit(infantryType);
    }

    @Override
    public ArrayList<UnitType> produceWhenNoProductionOrders() {
        ArrayList<UnitType> units = new ArrayList<>();
        if (AtlantisWorkerCommander.shouldTrainWorkers()) {
            units.add(UnitTypes.Zerg_Drone);
        }
        else {
            units.add(UnitTypes.Zerg_Zergling);
        }
        return units;
    }

    // =========================================================
    /**
     * Produce zerg unit from free larva. Will do nothing if no free larva is available.
     */
    public void produceZergUnit(UnitType unitType) {
        _produceUnit(unitType);
    }

    // =========================================================
    protected void _produceUnit(UnitType unitType) {
        for (Unit base : SelectUnits.ourBases().list()) {
            for (Unit unit : base.getLarva()) {
//                System.out.println(unit + " into " + unitType);
                base.train(unitType);
                return;
            }
        }
    }

    // =========================================================
    // Auxiliary
    private Unit getFreeLarva() {
        return SelectUnits.our().ofType(UnitTypes.Zerg_Larva).first();
    }

}
