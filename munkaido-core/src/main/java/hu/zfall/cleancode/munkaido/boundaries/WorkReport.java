package hu.zfall.cleancode.munkaido.boundaries;

import hu.zfall.cleancode.munkaido.boundaries.dto.WorkTimeSummary;

public interface WorkReport {

    WorkTimeSummary generateSummary(final String username);
}
