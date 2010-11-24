package com.anyerror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.*;

public class JdkLogHandler extends Handler
{
    @Override
    public void publish(LogRecord logRecord) {
        System.out.println("In our handner");
        HashMap map = new HashMap();

        map.put("message",logRecord.getMessage());
        map.put("sourceMethod",logRecord.getSourceMethodName());
        map.put("level",logRecord.getLevel().getName());
        map.put("parameters",paramsToSimpleArray(logRecord.getParameters()));
        map.put("sourceClassName",logRecord.getSourceClassName());
        map.put("threadId",logRecord.getThreadID());
        map.put("loggerName",logRecord.getLoggerName());

        if(logRecord.getThrown() == null) {
            AnyError.notify(logRecord.getMessage(),map,logRecord.getThrown());
        }
        else{
            AnyError.notify(logRecord.getMessage(),map,null);
        }

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    private ArrayList paramsToSimpleArray(Object[] in_params ) {
        if(in_params == null)
            return new ArrayList();

        ArrayList params = new ArrayList();
        for(int i = 0;i< in_params.length;i++)
        {
            params.add(in_params[i].toString());
        }
        return params;
    }
}