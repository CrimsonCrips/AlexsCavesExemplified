//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.crimsoncrips.alexscavesexemplified.misc.interfaces.MineGuardianXtra;

import java.util.Objects;

public class ACExMineGuardianHurtBy extends HurtByTargetGoal {

    MineGuardianEntity mineGuardian;

    public ACExMineGuardianHurtBy(MineGuardianEntity pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
        mineGuardian = pMob;
    }

    @Override
    public void start() {
        if (this.mob.getLastHurtByMob() instanceof Player player && !Objects.equals(player.getUUID().toString(), ((MineGuardianXtra)mineGuardian).alexsCavesExemplified$getOwner())) {
            this.mob.setTarget(this.mob.getLastHurtByMob());
        }
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        if (this.alertSameType) {
            this.alertOthers();
        }
    }
}
