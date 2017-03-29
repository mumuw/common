package com.lin.common.net;

import java.io.Serializable;

/**
 * Created by linweilin on 2016/11/8.
 */

public class RequestParameter implements Serializable,Comparable<Object>{

    private static final long serialVersionUID = 1274906854152052510L;

    private String name;

    private String value;

    public RequestParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Object another) {
        int compared;
        final RequestParameter parameter = (RequestParameter)another;
        compared = this.name.compareTo(parameter.name);
        if (compared == 0){
            compared = this.value.compareTo(parameter.value);
        }
        return compared;
    }

    @Override
    public boolean equals(Object o) {
        if ( null == o){
            return false;
        }
        if (this == o){
            return true;
        }
        if (o instanceof RequestParameter){
            final RequestParameter requestParameter = (RequestParameter) o;
            return requestParameter.name.equals(this.name) && requestParameter.value.equals(this.value);
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
