/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evergreen.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * Useful functions to do with entities
 * @author isXander
 */
public class EntityUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     *
     * @param ent the entity that we are getting pos from
     * @return block position of entity
     */
    public static BlockPos getBlockPosLoc(Entity ent) {
        return new BlockPos(ent.posX, ent.posY, ent.posZ);
    }

    /**
     * Calculates distance between player's eyes and entities hitbox
     * This has been taken from actual MC source and should NEVER give false positives.
     *
     * Very high precision means any injectable cheat that has not updated for evergreen
     * will be detected easily with double precision
     *
     * @param entity entity we are getting distance from
     * @return distance between player and entity
     * @author isXander
     */
    public static double getReachDistanceFromEntity(Entity entity) {
        mc.mcProfiler.startSection("Calculate Reach Dist");

        // How far will ray travel before ending
        double maxSize = 20D;
        // Bounding box of entity
        AxisAlignedBB otherBB = entity.getEntityBoundingBox();
        // This is where people found out that F3+B is not accurate for hitboxes,
        // it makes hitboxes bigger by certain amount
        float collisionBorderSize = entity.getCollisionBorderSize();
        AxisAlignedBB otherHitbox = otherBB.expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
        // Not quite sure what the difference is between these two vectors
        // In actual code where this is taken from, partialTicks is always 1.0
        // So this won't decrease accuracy
        Vec3 eyePos = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 lookPos = mc.thePlayer.getLook(1.0F);
        // Get vector for raycast
        Vec3 adjustedPos = eyePos.addVector(lookPos.xCoord * maxSize, lookPos.yCoord * maxSize, lookPos.zCoord * maxSize);
        MovingObjectPosition movingObjectPosition = otherHitbox.calculateIntercept(eyePos, adjustedPos);
        Vec3 otherEntityVec;
        // This will trigger if hit distance is more than maxSize
        if (movingObjectPosition == null)
            return -1;
        otherEntityVec = movingObjectPosition.hitVec;
        // finally calculate distance between both vectors
        double dist = eyePos.distanceTo(otherEntityVec);

        mc.mcProfiler.endSection();
        return dist;
    }

}
