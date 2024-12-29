package it.ohalee.matchmaking.example.master.queue;

import it.ohalee.basementlib.api.bukkit.chat.Colorizer;
import it.ohalee.matchmaking.example.master.player.BedwarsParticipator;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.matchmaking.example.player.ParticipatorSection;
import it.ohalee.ultimatematchmaking.queue.StandardQueueStealer;
import org.redisson.api.RLock;

import java.util.Optional;

public class BedwarsQueueStealer extends StandardQueueStealer<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> {

    public BedwarsQueueStealer(RLock taskLock, BedwarsQueue queue) {
        super(taskLock, queue);
    }

    public static ParticipatorSection getSectionFromPlayer(BedwarsParticipator bedwarsPlayer) {
        ParticipatorSection section = new ParticipatorSection(bedwarsPlayer);
        // If you want to add party support...

        /*
        Optional<Party> optionalParty = somePartyManager.getParty(bedwarsPlayer.getPlayerName());
        if (optionalParty.isEmpty() || optionalParty.get().getMembers().size() == 1 || !optionalParty.get().getLeader().equals(bedwarsPlayer.getPlayerName())) {
            section.addParticipator(bedwarsPlayer.getPlayerName());
            return section;
        }
        optionalParty.get().getMembers().forEach(component -> {
            section.addParticipator(component);
        });
         */
        return section;
    }

    @Override
    public void run() {

        if (!taskLock.tryLock()) return;
        try {
            if (queue.getRancher().getRunningServers() == 0)
                return;
            if (countNull == MAX_COUNT_NULL) {
                queue.idle();
                this.cancel();
                return;
            }
            Optional<BedwarsParticipator> playerOptional = queue.stealPlayer();
            if (!playerOptional.isPresent()) {
                countNull++;
                return;
            }

            BedwarsParticipator player = playerOptional.get();
            ParticipatorSection section = getSectionFromPlayer(player);

            if (section.size() > queue.getType().teamSize()) {
                player.getPlayer().sendMessage(Colorizer.colorize("&cIn your party there are too many players!"));
                player.removeFromQueue();
                return;
            }

            SharedBedwarsMatch match = queue.seekMatch(section.size());
            if (match == null) {
                if (player.lastUpdate() + 10000 < System.currentTimeMillis()) {
                    player.removeFromQueue();
                    if (player.getPlayer() != null)
                        player.getPlayer().sendMessage("§c› You have been removed from the queue!");
                    return;
                }
                queue.addPlayer(player, true);
                return;
            }

            match.join(section);
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            taskLock.unlock();
        }
    }

}
