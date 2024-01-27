package com.oglimmer.kniffel.web;

// If you want to use any other class you have to import it first.
// With * at the end, you import all classes in this package

import com.oglimmer.kniffel.web.dto.ServerStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

// this annotation enables this class for http REST processing
@RestController
// this is the base URL for all endpoint methods in this class
@RequestMapping("/api/v1/server")
@CrossOrigin // this will allow *
// @CrossOrigin(origins = "http://localhost")
public class ServerStatusController {

    // method = POST
    // url = /api/v1/server/thread-dump
    @PostMapping("/thread-dump")
    public void threadDump() {
        // this code writes the StackTrace of all threads to stdout
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMXBean.dumpAllThreads(true, true)) {
            System.out.println(threadInfo);
        }
    }

    // GET because this is pure data retrieval
    @GetMapping("/status")
    public ServerStatus getServerStatus() {
        // in Java you have to create all objects with: new <Class name>()
        ServerStatus serverStatus = new ServerStatus();
        // At this point we have a new object of class ServerStatus and we hold a
        // reference in the variable serverStatus.
        // This object lives on the Heap and is memory managed by Java (a Garbage Collector
        // will delete it if no references are pointing to it anymore)
        serverStatus.setServerTime(System.currentTimeMillis());
        serverStatus.setServerVersion("1.0.0");
        serverStatus.setServerName("Kniffel Server");
        serverStatus.setServerStatus("OK");
        return serverStatus;
    }
}