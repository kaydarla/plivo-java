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
import com.plivo.bridge.to.callback.HangupCallback;
import com.plivo.bridge.to.command.ApplicationResponse;
import com.plivo.bridge.to.command.GetDigits;
import com.plivo.bridge.to.command.Play;
import com.plivo.bridge.to.response.CallResponse;
import com.plivo.bridge.util.PlivoTestUtils;
import com.plivo.bridge.utils.PlivoUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=true)
public class GetDigitsTest extends BasePlivoTest {
	
	@Test(enabled=false)
	public void getDigits( ) throws Exception {
		
		ServiceHandler digitsHandler = new ServiceHandler("/digits.html", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got digits!");
						resp.getWriter().write("Ok");
						resp.getWriter().flush();
						resp.getWriter().close();
					}
				});
		
		
		ServiceHandler ringHandler = new ServiceHandler("/ring.html", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got ringing!");
						resp.getWriter().write("Ok");
						resp.getWriter().flush();
						resp.getWriter().close();
					}
				});
		
		ServiceHandler answerHandler = new ServiceHandler("/answer.html", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got answered!");
						
						AnsweredCallback callback = AnsweredCallback.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback);
						Assert.assertNotNull(callback.getCallUUID());
						
						ApplicationResponse ar = new ApplicationResponse();
						GetDigits digits = new GetDigits();
						digits.setAction(PlivoTestUtils.getCallbackUrl()+"/digits.html");
						digits.setNumDigits(1);
						Play p = new Play();
						digits.setPlay(p);
						p.setUrl("http://translate.google.com/translate_tts?q=Input+your+choice");
						p.setLoop(1);
						digits.setPlayBeep(true);
						digits.setRetries(1);
						digits.setValidDigits("123456789");
						ar.setGetDigits(digits);
						
						PlivoUtils.JAXBContext.createContext().createMarshaller().marshal(ar, resp.getWriter());
					}
				});
		
		ServiceHandler hangupHandler = new ServiceHandler("/hangup.html", 
				new HttpHandler() {
					@Override
					public void service(Request req, Response resp) throws Exception {
						System.out.println("got hangup!");
						
						HangupCallback callback = HangupCallback.create(PlivoTestUtils.mapToSingleValue(req.getParameterMap()));
						System.out.println(callback);
						
						Assert.assertNotNull(callback);
						Assert.assertNotNull(callback.getCallUUID());
						
						resp.getWriter().write("Ok");
						resp.getWriter().flush();
						resp.getWriter().close();
						
						stop();
					}
				});
		
		startServer(ringHandler, answerHandler, hangupHandler, digitsHandler);
		
		Map<String, String> parameters = 
				new HashMap<String, String>();
		
		parameters.put("From", "01010101010101010");
		parameters.put("To", "010101010110");

		parameters.put("HangupUrl", PlivoTestUtils.getCallbackUrl()+"/hangup.html");
		parameters.put("RingUrl", PlivoTestUtils.getCallbackUrl()+"/ring.html");
		parameters.put("AnswerUrl", PlivoTestUtils.getCallbackUrl()+"/answer.html");
		
		CallResponse result = client.call().single(parameters);
		
		System.out.println(result);
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getRequestUUID());
		Assert.assertEquals(result.isSuccess(), true);
	}
}
