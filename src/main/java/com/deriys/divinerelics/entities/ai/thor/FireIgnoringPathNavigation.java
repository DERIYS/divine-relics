package com.deriys.divinerelics.entities.ai.thor;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

public class FireIgnoringPathNavigation extends GroundPathNavigation {
    public FireIgnoringPathNavigation(Mob p_26448_, Level p_26449_) {
        super(p_26448_, p_26449_);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new FireIgnoringWalkNodeEvaluator();
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
}
