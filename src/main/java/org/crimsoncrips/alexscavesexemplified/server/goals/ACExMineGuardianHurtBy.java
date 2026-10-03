//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.crimsoncrips.alexscavesexemplified.server.goals;

import org.crimsoncrips.alexscavesexemplified.misc.ACExUtils;
import org.crimsoncrips.alexscavesexemplified.server.ACExAttachments;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class ACExMineGuardianHurtBy extends HurtByTargetGoal {

    MineGuardianEntity mineGuardian;

    public ACExMineGuardianHurtBy(MineGuardianEntity pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
        mineGuardian = pMob;
    }

    @Override
    public void start() {
        if (this.mob.getLastHurtByMob() instanceof Player player && !Objects.equals(player.getUUID().toString(), mineGuardian.getData(ACExAttachments.MINE_GUARDIAN_OWNER))) {
            this.mob.setTarget(this.mob.getLastHurtByMob());
        }
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = ACExUtils.seconds(15.0F);
        if (this.alertSameType) {
            this.alertOthers();
        }
    }
}
