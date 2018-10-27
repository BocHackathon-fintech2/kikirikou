package io.kikirikou.modules.jetty.managers.startup;


import io.kikirikou.modules.jetty.managers.decl.JettyManager;

public class StartJetty implements Runnable {

    private final JettyManager jettyManager;

    public StartJetty(JettyManager jettyManager) {
        this.jettyManager = jettyManager;
    }

    @Override
    public void run() {
        jettyManager.startup();
    }
}
