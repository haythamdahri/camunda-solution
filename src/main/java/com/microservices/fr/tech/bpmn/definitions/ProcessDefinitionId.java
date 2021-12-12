package com.microservices.fr.tech.bpmn.definitions;

public enum ProcessDefinitionId {

    CREATE_PAYMENT("payment-process"),
    CANCEL_PAYMENT("cancel-payment");

    private final String name;

    ProcessDefinitionId(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
