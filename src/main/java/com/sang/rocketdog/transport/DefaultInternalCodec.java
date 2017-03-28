package com.sang.rocketdog.transport;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;

import com.sang.rocketdog.transport.httpmsg.HttpMessage;

public class DefaultInternalCodec implements InternalCodec{

	private static final Integer maxInitialLineLength  = 1024;
	
	private DefaultInternalCodec(){
		
	}
	 static final  DefaultInternalCodec instance = new DefaultInternalCodec();
	
	public static DefaultInternalCodec getInstance(){
		return instance;
	}
	public void encode(Channel channel, ChannelBuffer buffer, Object message)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Object decode(Channel channel, ChannelBuffer buffer)
			throws Exception {
		// TODO Auto-generated method stub
		skipControlCharacters(buffer);
		HttpMessage message = new HttpMessage();
		String[] initPartList = readInitLine(buffer,message);
		if(initPartList.length<3){
			return DecodeResult.NEED_MORE_INPUT; //need
		}
		setMethod(message,initPartList[0]);
		setRequestPath(message,initPartList[1]);
		setHttpVersion(message, initPartList[2]);
		return message;
	}
	
	private static void setMethod(HttpMessage message,String methodStr) throws BadRequestException{
		if("GET".equals(methodStr) || "POST".equals(methodStr)){
			message.setMethod(methodStr);
		}else{
			throw new BadRequestException("method in requestLine is not in (GET,POST),but a "+methodStr);
		}
	}
	
	private static void setRequestPath(HttpMessage message,String requestPath){
		message.setRequestPath(requestPath);
	}
	private static void setHttpVersion(HttpMessage message,String versionStr) throws BadRequestException{
		if("HTTP/1.1".equals(versionStr) || "HTTP/1.0".equals(versionStr) || "HTTP/0.9".equals(versionStr)){
			message.setVersion(versionStr);
		}else{
			throw new BadRequestException("httpVersion in requestLine is not in (HTTP/1.1,HTTP/1.0,HTTP/0.9),but a "+versionStr);
		}
	}
    private String[]  readInitLine(ChannelBuffer buffer, HttpMessage message) throws TooLongFrameException, NeedMoreDataException {
    	String lineStr = readLine(buffer);
    	int astart =  findNoWhitespace(lineStr, 0);
    	int aEnd = findWhitespace(lineStr, astart);
    	
    	int bstart = findNoWhitespace(lineStr, aEnd);
    	int bEnd = findWhitespace(lineStr, bstart);
    	
    	int cstart = findNoWhitespace(lineStr, bEnd);
    	int cEnd = findEndOfString(lineStr);	
    	
    	return new String[]{lineStr.substring(astart, aEnd),
    			lineStr.substring(bstart, bEnd),
    			lineStr.substring(cstart, cEnd)};
	}
	private String readLine(ChannelBuffer buffer) throws TooLongFrameException, NeedMoreDataException {
		// TODO Auto-generated method stub
		int lineLength = 0;
		StringBuffer sb = new StringBuffer();
		do{
			byte nextByte = buffer.readByte();
			if(nextByte==13){
				nextByte = buffer.readByte();
				if(nextByte==10){
					return sb.toString();
				}
			}else if(nextByte==10){
				return sb.toString();
			}
			if(lineLength>maxInitialLineLength){
                // TODO: Respond with Bad Request and discard the traffic
                //    or close the connection.
                //       No need to notify the upstream handlers - just log.
                //       If decoding a response, just throw an exception.
                throw new TooLongFrameException(
                        "An HTTP line is larger than " + maxInitialLineLength +
                        " bytes.");
			}
			lineLength++;
			sb.append((char)nextByte);  //TODO (char)
			
		}while(buffer.readable());
		throw new NeedMoreDataException();
		
	}
	private static void skipControlCharacters(ChannelBuffer buffer) {
        for (;;) {
            char c = (char) buffer.readUnsignedByte();
            if (!Character.isISOControl(c) &&
                !Character.isWhitespace(c)) {
                buffer.readerIndex(buffer.readerIndex() - 1);
                break;
            }
        }
    }
	
	
	private static int findWhitespace(String str,int offset){
		int result ;
		for (result=offset;result<str.length();result++) {
			if(Character.isWhitespace(str.charAt(result))){
				break;
			}
		}
		return result;
	}
	
	private static int findNoWhitespace(String str,int offset){
		int result ;
		for (result=offset;result<str.length();result++) {
			if(!Character.isWhitespace(str.charAt(result))){
				break;
			}
		}
		return result;
	}
	
	private static int findEndOfString(String str){
		int result ;
		
		for(result=str.length();result>0;result--){
			if(!Character.isWhitespace(str.charAt(result-1))){
				break;
			}
		}
		return result;
		
		
	}
}
