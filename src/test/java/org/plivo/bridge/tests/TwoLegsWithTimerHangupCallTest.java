package com.plivo.bridge.tests;
/**
 * Copyright (c) 2011 Plivo Inc. See LICENSE for details.
 *  2011-08-28
 * .
 */

import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import com.plivo.bridge.base.BasePlivoTest;
import com.plivo.bridge.server.GrizzlyServer.ServiceHandler;
import com.plivo.bridge.to.callback.AnsweredCallback;
import com.plivo.bridge.to.callback.CallbackStatus;
import com.plivo.bridge.to.callback.HangupCallback;
import com.plivo.bridge.to.command.ApplicationResponse;
import com.plivo.bridge.to.command.Dial;
import com.plivo.bridge.to.command.Number;
import com.plivo.bridge.to.response.CallResponse;
import com.plivo.bridge.to.response.ScheduleHangupResponse;
import com.plivo.bridge.util.PlivoTestUtils;
import com.plivo.bridge.utils.PlivoUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false)
public class TwoLegsWithTimerHangupCallTest extends BasePlivoTest {
	
	@Test(enabled=false)
	public void initCall( ) throws Exception {
		ServiceHandler answerHandler = new ServiceHandler("/answered/*", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got answered!");
						final AnsweredCallback callback = AnsweredCallback.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback);
						
						ApplicationResponse ar = new ApplicationResponse();
						Dial d = new Dial();
						d.setAction(PlivoTestUtils.getCallbackUrl()+"/callbackStatus/");
						com.plivo.bridge.to.command.Number n = new Number();
						ar.setDial(d);
						d.setNumber(n);
						
						PlivoUtils.JAXBContext.createContext().createMarshaller().marshal(ar, resp.getWriter());
						resp.getWriter().flush();
						resp.getWriter().close();
						
						// or you can establish a call passing the parameter TimeLimit
						// this approach will work like schedule hang up
						
						Map<String, String> parameters = 
								new HashMap<String, String>();
						parameters.put("CallUUID", callback.getCallUUID());
						parameters.put("Time", "10");
						ScheduleHangupResponse hangup = client.call().scheduleHangup(parameters);
						Assert.assertNotNull(hangup);
						Assert.assertNotNull(hangup.getSchedHangupId());
						Assert.assertEquals(hangup.isSuccess(), true);
					}
				});
		
		ServiceHandler hangupHandler = new ServiceHandler("/hangup/*", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got hangup!");
						HangupCallback callback = HangupCallback.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback);
						
						resp.getWriter().write("hangup");
						resp.getWriter().flush();
						resp.getWriter().close();
						stop();
					}
				});
		
		ServiceHandler callabckHandler = new ServiceHandler("/callbackStatus/*", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got calback status!");
						CallbackStatus callback = CallbackStatus.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback);
					}
				});
				
		startServer(answerHandler, hangupHandler, callabckHandler);
		
		Map<String, String> parameters = 
				new HashMap<String, String>();
		
		parameters.put("From", "9999");
		parameters.put("To", "1002");

		parameters.put("AnswerUrl", PlivoTestUtils.getCallbackUrl()+"/answered/");
		parameters.put("HangupUrl", PlivoTestUtils.getCallbackUrl()+"/hangup/");

		CallResponse result = client.call().single(parameters);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertNotNull(result.getRequestUUID());
	}
}
