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

public final class MessageAsyncHandlerPrxHelper extends Ice.ObjectPrxHelperBase implements MessageAsyncHandlerPrx
{
    private static final String __onRecieve_name = "onRecieve";

    public void onRecieve(Message msg, String extra)
        throws MessageException
    {
        onRecieve(msg, extra, null, false);
    }

    public void onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx)
        throws MessageException
    {
        onRecieve(msg, extra, __ctx, true);
    }

    private void onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws MessageException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        final Ice.Instrumentation.InvocationObserver __observer = IceInternal.ObserverHelper.get(this, "onRecieve", __ctx);
        int __cnt = 0;
        try
        {
            while(true)
            {
                Ice._ObjectDel __delBase = null;
                try
                {
                    __checkTwowayOnly("onRecieve");
                    __delBase = __getDelegate(false);
                    _MessageAsyncHandlerDel __del = (_MessageAsyncHandlerDel)__delBase;
                    __del.onRecieve(msg, extra, __ctx, __observer);
                    return;
                }
                catch(IceInternal.LocalExceptionWrapper __ex)
                {
                    __handleExceptionWrapper(__delBase, __ex, __observer);
                }
                catch(Ice.LocalException __ex)
                {
                    __cnt = __handleException(__delBase, __ex, null, __cnt, __observer);
                }
            }
        }
        finally
        {
            if(__observer != null)
            {
                __observer.detach();
            }
        }
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra)
    {
        return begin_onRecieve(msg, extra, null, false, null);
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx)
    {
        return begin_onRecieve(msg, extra, __ctx, true, null);
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, Ice.Callback __cb)
    {
        return begin_onRecieve(msg, extra, null, false, __cb);
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_onRecieve(msg, extra, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, Callback_MessageAsyncHandler_onRecieve __cb)
    {
        return begin_onRecieve(msg, extra, null, false, __cb);
    }

    public Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, Callback_MessageAsyncHandler_onRecieve __cb)
    {
        return begin_onRecieve(msg, extra, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_onRecieve(Message msg, String extra, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__onRecieve_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __onRecieve_name, __cb);
        try
        {
            __result.__prepare(__onRecieve_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__startWriteParams(Ice.FormatType.DefaultFormat);
            msg.__write(__os);
            __os.writeString(extra);
            __result.__endWriteParams();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_onRecieve(Ice.AsyncResult __result)
        throws MessageException
    {
        Ice.AsyncResult.__check(__result, this, __onRecieve_name);
        boolean __ok = __result.__wait();
        try
        {
            if(!__ok)
            {
                try
                {
                    __result.__throwUserException();
                }
                catch(MessageException __ex)
                {
                    throw __ex;
                }
                catch(Ice.UserException __ex)
                {
                    throw new Ice.UnknownUserException(__ex.ice_name(), __ex);
                }
            }
            __result.__readEmptyParams();
        }
        catch(Ice.LocalException ex)
        {
            Ice.Instrumentation.InvocationObserver __obsv = __result.__getObserver();
            if(__obsv != null)
            {
                __obsv.failed(ex.ice_name());
            }
            throw ex;
        }
    }

    public static MessageAsyncHandlerPrx checkedCast(Ice.ObjectPrx __obj)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof MessageAsyncHandlerPrx)
            {
                __d = (MessageAsyncHandlerPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId()))
                {
                    MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static MessageAsyncHandlerPrx checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof MessageAsyncHandlerPrx)
            {
                __d = (MessageAsyncHandlerPrx)__obj;
            }
            else
            {
                if(__obj.ice_isA(ice_staticId(), __ctx))
                {
                    MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static MessageAsyncHandlerPrx checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId()))
                {
                    MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static MessageAsyncHandlerPrx checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA(ice_staticId(), __ctx))
                {
                    MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static MessageAsyncHandlerPrx uncheckedCast(Ice.ObjectPrx __obj)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            if(__obj instanceof MessageAsyncHandlerPrx)
            {
                __d = (MessageAsyncHandlerPrx)__obj;
            }
            else
            {
                MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static MessageAsyncHandlerPrx uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        MessageAsyncHandlerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            MessageAsyncHandlerPrxHelper __h = new MessageAsyncHandlerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::com::sky::game::context::MessageAsyncHandler"
    };

    public static String ice_staticId()
    {
        return __ids[1];
    }

    protected Ice._ObjectDelM __createDelegateM()
    {
        return new _MessageAsyncHandlerDelM();
    }

    protected Ice._ObjectDelD __createDelegateD()
    {
        return new _MessageAsyncHandlerDelD();
    }

    public static void __write(IceInternal.BasicStream __os, MessageAsyncHandlerPrx v)
    {
        __os.writeProxy(v);
    }

    public static MessageAsyncHandlerPrx __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            MessageAsyncHandlerPrxHelper result = new MessageAsyncHandlerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }

    public static final long serialVersionUID = 0L;
}
