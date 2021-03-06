// **********************************************************************
//
// Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.5.0
//
// <auto-generated>
//
// Generated from file `message.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.sky.game.context;

public class MessageInternalBean implements java.lang.Cloneable, java.io.Serializable
{
    public String ns;

    public String operation;

    public String parameter;
    
    
    
    public MessageInternalBean()
    {
    }

    public MessageInternalBean(String ns, String operation, String parameter)
    {
        this.ns = ns;
        this.operation = operation;
        this.parameter = parameter;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        MessageInternalBean _r = null;
        if(rhs instanceof MessageInternalBean)
        {
            _r = (MessageInternalBean)rhs;
        }

        if(_r != null)
        {
            if(ns != _r.ns)
            {
                if(ns == null || _r.ns == null || !ns.equals(_r.ns))
                {
                    return false;
                }
            }
            if(operation != _r.operation)
            {
                if(operation == null || _r.operation == null || !operation.equals(_r.operation))
                {
                    return false;
                }
            }
            if(parameter != _r.parameter)
            {
                if(parameter == null || _r.parameter == null || !parameter.equals(_r.parameter))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 5381;
        __h = IceInternal.HashUtil.hashAdd(__h, "::com::sky::game::context::MessageInternalBean");
        __h = IceInternal.HashUtil.hashAdd(__h, ns);
        __h = IceInternal.HashUtil.hashAdd(__h, operation);
        __h = IceInternal.HashUtil.hashAdd(__h, parameter);
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeString(ns);
        __os.writeString(operation);
        __os.writeString(parameter);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        ns = __is.readString();
        operation = __is.readString();
        parameter = __is.readString();
    }

    public static final long serialVersionUID = 2975128718146249112L;
}
