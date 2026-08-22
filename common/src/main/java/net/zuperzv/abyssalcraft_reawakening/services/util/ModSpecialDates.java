package net.zuperzv.abyssalcraft_reawakening.services.util;

import java.time.Month;
import java.time.MonthDay;
import java.time.ZonedDateTime;
import java.util.List;

public class ModSpecialDates {
    public static final MonthDay HAPPY_BITHDAY_SHINOOW;
    public static final MonthDay HAPPY_BITHDAY_ZUPERZ;
    public static final MonthDay RIP_SHINOOWS_GRANDFATHER;

    public static MonthDay dayNow() {
        return MonthDay.from(ZonedDateTime.now());
    }

    public static boolean isSZuperZsBithday() {
        return HAPPY_BITHDAY_ZUPERZ.equals(dayNow());
    }
    public static boolean isShinoowsBithday() {
        return HAPPY_BITHDAY_SHINOOW.equals(dayNow());
    }

    //TODO Find out if this is a good or bad thing to do.
    // Hmm i don't think so but i am letting i be for now though. IDK
    public static boolean isRIPShinoowsGrandfather() {
        return RIP_SHINOOWS_GRANDFATHER.equals(dayNow());
    }
    static {
        HAPPY_BITHDAY_ZUPERZ = MonthDay.of(Month.MAY, 10);
        HAPPY_BITHDAY_SHINOOW = MonthDay.of(Month.FEBRUARY, 7);
        RIP_SHINOOWS_GRANDFATHER = MonthDay.of(Month.NOVEMBER, 9);
    }
}
