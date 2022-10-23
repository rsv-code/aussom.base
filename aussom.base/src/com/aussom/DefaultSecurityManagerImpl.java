package com.aussom;

public class DefaultSecurityManagerImpl extends SecurityManagerImpl {
    public DefaultSecurityManagerImpl() {

        /*
         * Aussomdoc actions. See com.aussom.stdlib.ALang.java.
         */
        this.props.put("aussomdoc.file.getJson", true);
        this.props.put("aussomdoc.class.getJson", true);
    }
}
