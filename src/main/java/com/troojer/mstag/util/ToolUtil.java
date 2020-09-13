package com.troojer.mstag.util;

import static java.util.ResourceBundle.getBundle;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class ToolUtil {
    public static String getMessage(String messageName) {
        return getBundle("messages", getLocale()).getString(messageName);
    }
}
