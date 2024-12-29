package it.ohalee.matchmaking.example.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ParticipatorSection {

    @Getter
    private final Object leader;

    private final Set<String> participatorsSection = new HashSet<>();

    public int size() {
        return participatorsSection.size();
    }

    public void addParticipator(String name) {
        participatorsSection.add(name);
    }

    public Set<String> getParticipators() {
        return Collections.unmodifiableSet(participatorsSection);
    }

}