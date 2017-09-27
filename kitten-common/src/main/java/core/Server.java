package main.java.core;

import java.io.IOException;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:05
 */
public interface Server {

    void start() throws IOException;

    void stop() throws InterruptedException;

}
