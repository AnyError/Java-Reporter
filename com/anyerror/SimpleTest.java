package com.anyerror;

import java.util.logging.*;


/**
 * Created by IntelliJ IDEA.
 * User: ciddennis
 * Date: Oct 24, 2010
 * Time: 2:15:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTest {
    public static void main(String[] args) {
        AnyError.project_key = "4ce5e1ff1c27315abe000001";

        Logger log = Logger.getAnonymousLogger();
        log.addHandler(new JdkLogHandler());
        log.setLevel(Level.FINEST);

        org.apache.log4j.Logger log2 = org.apache.log4j.Logger.getRootLogger();
        log2.addAppender(new Log4JAppender());
        
        for (int i = 0; i < 10; i++) {
            log.log(Level.SEVERE, "This is a server error");
            log.log(Level.SEVERE, "This is a server error with some randon stuff " + System.currentTimeMillis());
            log2.error("This is a log4j message");

            try {
                String b = null;
                b.charAt(1);
            }
            catch (Throwable t) {

                AnyError.notify(t);
            }
        }
    }
}
