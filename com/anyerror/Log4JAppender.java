package com.anyerror;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ciddennis
 * Date: Oct 24, 2010
 * Time: 6:10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Log4JAppender implements Appender {
    public void addFilter(Filter filter) {

    }

    public Filter getFilter() {
        return null;
    }

    public void clearFilters() {

    }

    public void close() {

    }

    public void doAppend(LoggingEvent loggingEvent) {
        LocationInfo li = loggingEvent.getLocationInformation();

        HashMap map = new HashMap();

        map.put("message", loggingEvent.getMessage());
        map.put("sourceMethod", li.getMethodName());
        map.put("level", loggingEvent.getLevel().toString());
        map.put("parameters", loggingEvent.getProperties());
        map.put("sourceClassName", li.getClassName());
        map.put("lineNumber", li.getLineNumber());
        map.put("fileName", li.getFileName());
        map.put("threadName", loggingEvent.getThreadName());


        
        AnyError.notify(loggingEvent.getMessage().toString(),
                map,
                (loggingEvent.getThrowableInformation() == null ?
                        null : loggingEvent.getThrowableInformation().getThrowable())
        );


    }


    public String getName() {
        return null;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {

    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public void setLayout(Layout layout) {

    }

    public Layout getLayout() {
        return null;
    }

    public void setName(String s) {

    }

    public boolean requiresLayout() {
        return false;
    }
}
