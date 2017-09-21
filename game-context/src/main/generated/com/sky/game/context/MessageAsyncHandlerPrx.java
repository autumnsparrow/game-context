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

public interface MessageAsyncHandlerPrx extends Ice.ObjectPrx
{
    public void onRecieve(Message msg, String extra)
        throws MessageException;

    public void onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx)
        throws MessageException;

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra);

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, Ice.Callback __cb);

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, Callback_MessageAsyncHandler_onRecieve __cb);

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, Callback_MessageAsyncHandler_onRecieve __cb);

    public void end_onRecieve(Ice.AsyncResult __result)
        throws MessageException;
}