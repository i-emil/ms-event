package com.troojer.msevent.client;

import com.troojer.msevent.util.AccessCheckerUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserPlanConstantsClient {

    private final AccessCheckerUtil accessChecker;

    protected UserPlanConstantsClient(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    public static int getMaxPersonCount(String plan) {
        return switch (plan) {
            case "BASIC" -> 10;
            case "PREMIUM" -> 20;
            case "PREMIUM_PLUS" -> 30;
            case "TOUR_AGENCY" -> 30;
            case "EVENT_AGENCY" -> 50;
            case "TOUR_AGENCY_PLUS" -> 50;
            case "EVENT_AGENCY_PLUS" -> 100;
            case "UNLIMITED" -> Integer.MAX_VALUE;
            default -> 10;
        };
    }

    public static int getMaxEventDurationHour(String plan) {
        return switch (plan) {
            case "BASIC" -> 3;
            case "PREMIUM" -> 12;
            case "PREMIUM_PLUS" -> 24;
            case "TOUR_AGENCY" -> 120;
            case "EVENT_AGENCY" -> 24;
            case "TOUR_AGENCY_PLUS" -> 168;
            case "EVENT_AGENCY_PLUS" -> 72;
            case "UNLIMITED" -> Integer.MAX_VALUE;
            default -> 3;
        };
    }

    public static int getDaysBeforeStarting(String plan) {
        return switch (plan) {
            case "BASIC" -> 7;
            case "PREMIUM" -> 14;
            case "PREMIUM_PLUS" -> 30;
            case "TOUR_AGENCY" -> 60;
            case "EVENT_AGENCY" -> 60;
            case "TOUR_AGENCY_PLUS" -> 120;
            case "EVENT_AGENCY_PLUS" -> 120;
            case "UNLIMITED" -> Integer.MAX_VALUE;
            default -> 7;
        };
    }

    public Map<String, String> getConstantsMap(){
        String plan = accessChecker.getPlan();
        System.out.println("dddd"+plan);
        Map<String, String> map = new HashMap<>();
        map.put("maxPersonCount", Integer.toString(getMaxPersonCount(plan)));
        map.put("maxEventDurationHour", Integer.toString(getMaxEventDurationHour(plan)));
        map.put("daysBeforeStarting", Integer.toString(getMaxEventDurationHour(plan)));
        return map;
    }
}
