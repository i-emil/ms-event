package com.troojer.msevent.model;

abstract public class UserPlanConstants {

    public static int getMaxPersonCount(String plan) {
        return switch (plan) {
            case "BASIC" -> 10;
            case "PREMIUM" -> 20;
            case "PREMIUM_PLUS"-> 30;
            case "TOUR_AGENCY" -> 30;
            case "EVENT_AGENCY" -> 50;
            case "TOUR_AGENCY_PLUS" -> 50;
            case "EVENT_AGENCY_PLUS" -> 100;
            case "UNLIMITED" -> Integer.MAX_VALUE;
            default -> 10;
        };
    }

    public static int getMaxEventDuration(String plan){
        return switch (plan) {
            case "BASIC" -> 3;
            case "PREMIUM" -> 12;
            case "PREMIUM_PLUS" -> 24;
            case "TOUR_AGENCY" -> 120;
            case "EVENT_AGENCY" -> 24;
            case "TOUR_AGENCY_PLUS" -> 50;
            case "EVENT_AGENCY_PLUS" -> 100;
            case "UNLIMITED" -> Integer.MAX_VALUE;
            default -> 10;
        };
    }

    public static int getDaysBeforeStarting(String plan){
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
}

//    BASIC,
//    PREMIUM,
//    PREMIUM_PLUS,
//    TOUR_AGENCY,
//    EVENT_AGENCY,
//    TOUR_AGENCY_PLUS,
//    EVENT_AGENCY_PLUS,
//    UNLIMITED
