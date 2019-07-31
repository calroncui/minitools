package com.cuiyf.minitools.exception;

public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {

	public static String getRootExceptionMessage(final Throwable throwable,String defaultMessage){		
		if( throwable == null ){
			return null;
		}		
		Throwable[] throwableArray = getThrowables(throwable);		
		Throwable t = throwableArray[throwableArray.length-1];		
		if( t.getMessage() == null ){
			return defaultMessage;
		}else{
			return t.getMessage();
		}		
	}
	
	public static String getRootExceptionMessage(final Throwable throwable){		
		return getRootExceptionMessage(throwable,"");
	}
	
}
