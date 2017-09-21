#ifndef MESSAGE_ICE_
#define MESSAGE_ICE_
module com {

	module sky {
		
		module game {
		
			module context {
			
				//
				// message bean
				//
				struct Message{
					string transcode;
					string content;
					string token;
				};
				
				//
				// Route Header
				//
				dictionary<string,string> RouteHeader;
				
				
				
				
				//
				// message exception 
				//
				exception MessageException{
					int state;
					string message;
				};
				
				//
				// message handler.
				// sync invoke
				//
				interface MessageHandler{
				
					Message invoke(Message msg) throws MessageException;
					
				};
				
				// message handler 
				// async invoke
				interface MessageAsyncHandler{
					
					// server side & client side
					void onRecieve(Message msg,string extra) throws MessageException;
					
				
				};
				
				
				// interface for internal system remote invoke
				
				// define a message bean for internal
				struct MessageInternalBean{
					string ns; // namesapce, defined the beans.
					string operation; // operation.
					string parameter;// in and out parameter
				
				};
				
				//internal invoke.
				interface MessageInternalHandler{
					MessageInternalBean invoke(MessageInternalBean param) throws MessageException;
				};
			
			
				// interface async message 
				interface MessageRouter{
					
					// one -way invoke. void return without exception.
					void route(Message message,RouteHeader);
					
				};
			
			};
		
		
		};
		
	
	};




};


#endif
