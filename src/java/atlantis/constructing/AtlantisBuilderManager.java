package atlantis.constructing;

import atlantis.AtlantisGame;
import atlantis.constructing.position.AbstractPositionFinder;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class AtlantisBuilderManager {

    public static void update(Unit builder) {
        if (builder == null) {
            System.err.println("builder null in ABM.update()");
            return;
        }

        // Don't disturb builder that are already constructing
        if (builder.isConstructing() || builder.isMorphing()) {
            return;
        }

        handleConstruction(builder);
    }

    // =========================================================
    
    private static void handleConstruction(Unit builder) {
        ConstructionOrder constructionOrder = AtlantisConstructingManager.getConstructionOrderFor(builder);
        if (constructionOrder != null) {

            // Construction HASN'T STARTED YET, we're probably not even at the
            // required place
            if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_NOT_STARTED) {
                travelToConstruct(builder, constructionOrder);
            } // Construction is IN PROGRESS
            else if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_IN_PROGRESS) {
                // Do nothing
            } // Construction has FINISHED
            else if (constructionOrder.getStatus() == ConstructionOrderStatus.CONSTRUCTION_FINISHED) {
                // Do nothing
            }
        } else {
            System.err.println("constructionOrder null for " + builder);
        }
    }

    private static void travelToConstruct(Unit builder, ConstructionOrder constructionOrder) {
        Position buildPosition = constructionOrder.getPositionToBuild();
        UnitType buildingType = constructionOrder.getBuildingType();

        if (builder == null) {
            throw new RuntimeException("Builder empty");
        }
        if (buildPosition == null) {
//            throw new RuntimeException("buildPosition empty");
            System.err.println("buildPosition is null (travelToConstruct " + buildingType + ")");
            constructionOrder.cancel();
            return;
        }

        // Move builder to the build position
        buildPosition = buildPosition.translated(buildingType.getTileWidth() * 16, buildingType.getTileHeight() * 16);
        if (!builder.isMoving() && !builder.isConstructing() && builder.distanceTo(buildPosition) > 0.15) {
            builder.move(buildPosition, false);
        } // Unit is already at the build position, issue build order
        // If we can afford to construct this building exactly right now, issue build order which should
        // be immediate as unit is standing just right there
        else if (AtlantisGame.canAfford(buildingType.getMineralPrice(), buildingType.getGasPrice())) {
            if (!AbstractPositionFinder.canPhysicallyBuildHere(builder, buildingType,
                    buildPosition)) {
                buildPosition = constructionOrder.findNewBuildPosition();
            }

            if (buildPosition != null && !builder.isConstructing()) {
                builder.build(buildPosition, buildingType);
            }
        }

    }

}
