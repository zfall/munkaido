package hu.zfall.cleancode.munkaido.domain;

public enum WorkTimeItemSpecial {
    LUNCH("L");

    private String value;

    WorkTimeItemSpecial(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WorkTimeItemSpecial getByValue(final String value) {
        for (final WorkTimeItemSpecial special : WorkTimeItemSpecial.values()) {
            if (special.getValue().equals(value)) {
                return special;
            }
        }
        return null;
    }
}
