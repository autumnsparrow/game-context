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

public final class MessageAsyncHandlerHolder extends Ice.ObjectHolderBase<MessageAsyncHandler>
{
    public
    MessageAsyncHandlerHolder()
    {
    }

    public
    MessageAsyncHandlerHolder(MessageAsyncHandler value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof MessageAsyncHandler)
        {
            value = (MessageAsyncHandler)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _MessageAsyncHandlerDisp.ice_staticId();
    }
}
