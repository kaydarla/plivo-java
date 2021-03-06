package com.plivo.bridge.tests;

/**
 * Copyright (c) 2011 Plivo Inc. See LICENSE for details.
 *  2012-03-14
 * .
 */

import java.util.HashMap;
import java.util.Map;

import com.plivo.bridge.client.PlivoClient;
import com.plivo.bridge.exception.PlivoClientException;
import com.plivo.bridge.to.response.BulkCallResponse;
import com.plivo.bridge.to.response.CallResponse;
import com.plivo.bridge.to.response.CancelScheduleHangupResponse;
import com.plivo.bridge.to.response.CancelSchedulePlayResponse;
import com.plivo.bridge.to.response.GroupCallResponse;
import com.plivo.bridge.to.response.HangupAllCallResponse;
import com.plivo.bridge.to.response.HangupCallResponse;
import com.plivo.bridge.to.response.PlayResponse;
import com.plivo.bridge.to.response.PlayStopResponse;
import com.plivo.bridge.to.response.RecordStartResponse;
import com.plivo.bridge.to.response.RecordStopResponse;
import com.plivo.bridge.to.response.ScheduleHangupResponse;
import com.plivo.bridge.to.response.SchedulePlayResponse;
import com.plivo.bridge.to.response.TransfCallResponse;
import com.plivo.bridge.to.response.conference.ConferenceDeafResponse;
import com.plivo.bridge.to.response.conference.ConferenceHangupResponse;
import com.plivo.bridge.to.response.conference.ConferenceKickResponse;
import com.plivo.bridge.to.response.conference.ConferenceListResponse;
import com.plivo.bridge.to.response.conference.ConferenceMuteResponse;
import com.plivo.bridge.to.response.conference.ConferencePlayResponse;
import com.plivo.bridge.to.response.conference.ConferenceRecordStartResponse;
import com.plivo.bridge.to.response.conference.ConferenceRecordStopResponse;
import com.plivo.bridge.to.response.conference.ConferenceSpeakResponse;
import com.plivo.bridge.to.response.conference.ConferenceUndeafResponse;
import com.plivo.bridge.to.response.conference.ConferenceUnmuteResponse;
import com.plivo.bridge.util.PlivoTestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled=false)
public class AllTest {
	PlivoClient client;

	public AllTest() {
		client = PlivoClient.create(PlivoTestUtils.getAccountId(),
				PlivoTestUtils.getAuthToken(), true);
	}

	@Test(enabled=false)
	public void makeCall() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/making-an-outbound-call/
		 */

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("From", "9999");
		parameters.put("To", "1003");

		parameters.put("AnswerUrl", "http://localhost/answer.html");


		CallResponse result = client.call().single(parameters);

		Assert.assertNotNull(result);
		Assert.assertTrue(result.isSuccess());
		Thread.sleep(10000);
	}

	@Test(enabled=false)
	public void makeBulkCall() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/making-bulk-outbound-calls/
		 */

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("From", "1001");
		parameters.put("To", "1002>1003");

		parameters.put("Delimiter", ">");
		parameters.put("Gateways", "user/>user/");
		parameters.put("GatewayCodecs", "PCMA,PCMU'>'PCMA,PCMU");
		parameters.put("GatewayTimeouts", "60>30");
		parameters.put("GatewayRetries", "2>1");
		parameters.put("OriginateDialString", "bridge_early_media=true,hangup_after_bridge=true");
		parameters.put("AnswerUrl", "http://localhost:5151/answered/");
		parameters.put("HangUpUrl", "http://localhost:5151/hangup/");
		parameters.put("RingUrl", "http://localhost:5151/ringing/");

		BulkCallResponse result = client.call().bulk(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void makeGroupCall() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/making-an-outbound-group-call/
		 */

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("From", "1001");
		parameters.put("To", "1002>1003");

		parameters.put("Delimiter", ">");
		parameters.put("Gateways", "user/>user/");
		parameters.put("AnswerUrl", "http://localhost:5151/answered/");
		parameters.put("HangUpUrl", "http://localhost:5151/hangup/");
		parameters.put("RingUrl", "http://localhost:5151/ringing/");

		GroupCallResponse result = client.call().group(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void transf() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/transfer-a-call/
		 */
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("URL", "http://localhost:5151/transfered/");
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");

		TransfCallResponse result = client.call().transfer(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void hangUp() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/hangup-a-call/
		 */

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		HangupCallResponse result = client.call().hangUp(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void hangUpAll() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/hangup-all-calls/ 
		 */
		Map<String, String> parameters = new HashMap<String, String>();
		HangupAllCallResponse result = client.call().hangUpAll(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void scheduleHangUp() throws Exception {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/schedule-hangup-for-a-call/ 
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		parameters.put("Time", "10000");
		ScheduleHangupResponse result = client.call().scheduleHangup(parameters);

		System.out.println(result);
	}

	@Test(enabled=false)
	public void cancelScheduleHangUp() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/cancel-a-scheduled-hangup/ 
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("SchedHangupId", "1");
		CancelScheduleHangupResponse result;
		
		try {
			result = client.call().cancelScheduledHangup(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void recordStart() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/start-recording-a-call/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		parameters.put("FileFormat", "mp3");
		parameters.put("FilePath", "/Users/plivo/temp/data/");
		parameters.put("TimeLimit", "60");
		RecordStartResponse result;
		
		try {
			result = client.call().recordStart(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void recordStop() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/1142-2/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		parameters.put("RecordFile", "/Users/plivo/temp/data/fileName.mp3");
		RecordStopResponse result;
		
		try {
			result = client.call().recordStop(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void play() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/play-something-to-a-call/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		parameters.put("Sounds", "hello.mp3,listenCarefully.mp3,bye.mp3");
		parameters.put("Legs", "aleg");
		PlayResponse result;
		
		try {
			result = client.call().play(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void playStop() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/stop-play/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		PlayStopResponse result;
		
		try {
			result = client.call().playStop(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void schedulePlay() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/schedule-play-something-on-a-call/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("CallUUID", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		parameters.put("Sounds", "hello.mp3,listenCarefully.mp3,bye.mp3");
		parameters.put("Legs", "aleg");
		parameters.put("Time", "20");
		
		SchedulePlayResponse result;
		
		try {
			result = client.call().schedulePlay(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void cancelSchedulePlay() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/call/cancel-a-scheduled-play/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("SchedPlayId", "edaa59e1-79e0-41de-b016-f7a7570f6e9c");
		
		CancelSchedulePlayResponse result;
		
		try {
			result = client.call().cancelSchedulePlay(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceMute() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/mute-a-member-or-all-members/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceMuteResponse result;
		
		try {
			result = client.conference().mute(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceUnmute() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/unmute-a-member-or-all-members/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceUnmuteResponse result;
		
		try {
			result = client.conference().unmute(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceKick() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/kick-a-member-or-all-members/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceKickResponse result;
		
		try {
			result = client.conference().kick(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceHangup() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/1170-2/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceHangupResponse result;
		
		try {
			result = client.conference().hangup(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceDeaf() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/make-a-member-or-all-members-deaf/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceDeafResponse result;
		
		try {
			result = client.conference().deaf(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceUndeaf() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/make-a-member-or-all-members-undeaf/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("ConferenceMemberID", "all");
		
		ConferenceUndeafResponse result;
		
		try {
			result = client.conference().undeaf(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceRecordStart() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/start-recording-a-conference/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("FileFormat", "mp3");
		parameters.put("FilePath", "/Users/plivo/temp/data/");
		
		ConferenceRecordStartResponse result;
		
		try {
			result = client.conference().recordStart(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceRecordStop() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/stop-recording-a-conference/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("RecordFile", "conference.mp3");
		
		ConferenceRecordStopResponse result;
		
		try {
			result = client.conference().recordStop(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferencePlay() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/play-sound-into-a-conference/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("FilePath", "/Users/plivo/sounds/me.mp3");
		parameters.put("MemberID", "2xep3");
		
		ConferencePlayResponse result;
		
		try {
			result = client.conference().play(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceSpeak() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/say-speech-into-a-conference/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("Text", "Hello World");
		parameters.put("MemberID", "2xep3");
		
		ConferenceSpeakResponse result;
		
		try {
			result = client.conference().speak(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceListMembers() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/list-members-in-a-conference/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("ConferenceName", "test-conference");
		parameters.put("MemberFilter", "");
		parameters.put("CallUUIDFilter", "");
		parameters.put("MutedFilter", "false");
		parameters.put("DeafFilter", "false");
		
		ConferenceListResponse result;
		
		try {
			result = client.conference().listMembers(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
	
	@Test(enabled=false)
	public void conferenceList() {
		/*
		 * Check documentation at http://www.plivo.org/docs/restapis/conference/list-all-conferences-and-members/
		 */
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("MemberFilter", "");
		parameters.put("CallUUIDFilter", "");
		parameters.put("MutedFilter", "false");
		parameters.put("DeafFilter", "false");
		
		ConferenceListResponse result;
		
		try {
			result = client.conference().list(parameters);
			System.out.println(result);
		} catch (PlivoClientException e) {
			System.out.println(e.getHttpMessage());
			System.out.println(e.getHttpStatusCode());
		}
	}
}
