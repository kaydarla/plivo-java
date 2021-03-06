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
import com.plivo.bridge.to.command.Speak;
import com.plivo.bridge.to.response.CallResponse;
import com.plivo.bridge.util.PlivoTestUtils;
import com.plivo.bridge.utils.PlivoUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false)
public class TwoLegsCallTest extends BasePlivoTest {
	
	@Test(enabled=false)
	public void initCall( ) throws Exception {
		ServiceHandler ringHandler = new ServiceHandler("/ringing/*", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got ringing!");
						resp.getWriter().write("Ok");
						resp.getWriter().flush();
						resp.getWriter().close();
					}
				});
	
		ServiceHandler answerHandler = new ServiceHandler("/answered/*", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got answered!");
						AnsweredCallback callback = AnsweredCallback.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback.getCallUUID());
						
						ApplicationResponse ar = new ApplicationResponse();
						Speak s = new Speak();
						s.setText("Please, wait while we call to PC!");
						ar.setSpeak(s);
						Dial d = new Dial();
						d.setAction(PlivoTestUtils.getCallbackUrl()+"/callbackStatus/");
						com.plivo.bridge.to.command.Number n = new Number();
						
						n.setNumber("553499322261");
						ar.setDial(d);
						d.setNumber(n);
						
						PlivoUtils.JAXBContext.createContext().createMarshaller().marshal(ar, resp.getOutputStream());
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
						
						resp.getWriter().write("Ok");
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
						Assert.assertNotNull(callback.getHangupReason());
					}
				});
		
		
		startServer(answerHandler, ringHandler, hangupHandler, callabckHandler);
		
		Map<String, String> parameters = 
				new HashMap<String, String>();
		
		parameters.put("From", "1111");
		parameters.put("To", "2222");

		parameters.put("RingUrl", PlivoTestUtils.getCallbackUrl()+"/ringing/");
		parameters.put("HangupUrl", PlivoTestUtils.getCallbackUrl()+"/hangup/");
		parameters.put("AnswerUrl", PlivoTestUtils.getCallbackUrl()+"/answered/");
		
		CallResponse result = client.call().single(parameters);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(result.isSuccess(), true);
		Assert.assertNotNull(result.getRequestUUID());
	}
}
