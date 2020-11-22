package hu.zfall.cleancode.munkaido.boundaries;

import hu.zfall.cleancode.munkaido.boundaries.dto.InfoResponse;

public interface StartEndWorkRegistration {

    InfoResponse startWork(final String username);

    InfoResponse endWork(final String username);

    InfoResponse startLunch(final String username);

    InfoResponse endLunch(final String username);

}
