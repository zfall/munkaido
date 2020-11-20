package hu.zfall.cleancode.munkaido.boundaries;

import hu.zfall.cleancode.munkaido.boundaries.dto.InfoResponse;

public interface StartEndWorkRegistration {

    InfoResponse startWork(final String username);

    InfoResponse endWork(final String username);

    InfoResponse startLanch(final String username);

    InfoResponse endLanch(final String username);

}
