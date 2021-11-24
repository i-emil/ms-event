package com.troojer.msevent.model.enm;

import lombok.Getter;

public enum Duration {
    H1(1), H3(3), H5(5), H12(12), D1(24);

    @Getter
    private final int hours;

    Duration(int hours) {
        this.hours = hours;
    }
}
