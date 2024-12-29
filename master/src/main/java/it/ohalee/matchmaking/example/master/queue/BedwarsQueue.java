package it.ohalee.matchmaking.example.master.queue;

import it.ohalee.ultimatematchmaking.api.MapSupplier;
import it.ohalee.ultimatematchmaking.api.QueueStealer;
import it.ohalee.matchmaking.example.master.player.BedwarsParticipator;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.match.SharedMatchStatus;
import it.ohalee.ultimatematchmaking.queue.QueueStatus;
import it.ohalee.ultimatematchmaking.queue.StandardQueue;
import it.ohalee.ultimatematchmaking.util.Basement;
import it.ohalee.ultimatematchmaking.util.StaticTask;

import java.util.UUID;

public class BedwarsQueue extends StandardQueue<BedwarsType, SharedBedwarsMatch, BedwarsParticipator> {

    public BedwarsQueue(String mode, BedwarsType queueType, MapSupplier supplier) {
        super(mode, queueType, supplier);
    }

    @Override
    public SharedBedwarsMatch seekMatch(int weight) {
        if (tunnels.isEmpty()) return createMatch();
        SharedBedwarsMatch best = null;
        for (SharedBedwarsMatch match : tunnels.values()) {
            if (match.getStatus() != SharedMatchStatus.OPEN || !match.canCarry(weight)) continue;

            best = match;
        }
        return best == null ? createMatch() : best;
    }

    @Override
    public SharedBedwarsMatch summonMatch() {
        return new SharedBedwarsMatch(UUID.randomUUID().toString().substring(0, 4) + mode + UUID.randomUUID().toString().substring(0, 3), queueType);
    }

    @Override
    public void task() {
        status = QueueStatus.TASK;
        StaticTask.runBukkitTaskTimer(summonStealer(), 2L, 2L, true);
    }

    @Override
    public QueueStealer<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> summonStealer() {
        return new BedwarsQueueStealer(
                Basement.rclient().getLock("lock_" + queueType.name()),
                this
        );
    }

}
