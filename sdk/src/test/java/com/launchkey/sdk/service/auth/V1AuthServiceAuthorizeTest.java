package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.transport.v1.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressWarnings("Duplicates")
public class V1AuthServiceAuthorizeTest extends V1AuthServiceTestBase{

    protected final AuthsResponse authsResponse = new AuthsResponse("auth request");

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        when(transport.auths(any(AuthsRequest.class))).thenReturn(authsResponse);
    }

    @Test
    public void testChecksPingResponseCache() throws Exception {
        service.authorize("username");
        verifyChecksPingResponseCache();
    }

    @Test
    public void testDoesNotCallPingWhenPingResponseCacheReturnsResponse() throws Exception {
        when(pingResponseCache.getPingResponse()).thenReturn(pingResponse);

        service.authorize("username");
        verifyDoesNotCallPingWhenPingResponseCacheReturnsResponse();
    }

    @Test
    public void testDoesCallPingWhenPingResponseCacheReturnsNull() throws Exception {
        service.authorize("username");
        verifyDoesCallPingWhenPingResponseCacheReturnsNull();
    }

    @Test
    public void testAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue() throws Exception {
        service.authorize("username");
        verifyAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue();
    }

    @Test
    public void testPassesAuthsRequestToTransportAuthsCall() throws Exception {
        service.authorize("username");
        verify(transport).auths(any(AuthsRequest.class));
    }

    @Test
    public void testPassesCorrectRocketIDInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testPassesCorrectRsaEncryptedSecretKeyInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Encrypted Value".getBytes()), argumentCaptor.getValue().getSecretKey());
    }

    @Test
    public void testUsesPemFromPingToCreatePublicKey() throws Exception {
        service.authorize("username");
        verifyUsesPemFromPingToCreatePublicKey();
    }

    @Test
    public void testRsaEncryptedWithCorrectPublicKeyToCreateSecretKeyInAuthsRequest() throws Exception {
        service.authorize("username");
        verifyRsaEncryptedWithCorrectPublicKeyToCreateSecretKey();
    }

    @Test
    public void testRsaEncryptedJsonWithCorrectDataToCreateSecretKeyInAuthsRequest() throws Exception {
        Date start = new Date();
        service.authorize("username");
        Date end = new Date();

        verifyRsaEncryptedJsonWithCorrectDataToCreateSecretKey(start, end);
    }

    @Test
    public void testPassesCorrectSignatureInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Signature".getBytes()), argumentCaptor.getValue().getSignature());
    }

    @Test
    public void testSignatureInAuthsRequestUsedEncryptedButNotEncodedSecretKeyValue() throws Exception {
        service.authorize("username");
        verifySignatureUsedEncryptedSecretKeyValue();
    }

    @Test
    public void testPassesZeroAsSessionInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(0, argumentCaptor.getValue().getSession());
    }

    @Test
    public void testPassesOneAsUserPushIdInAuthsRequest() throws Exception {
        service.authorize("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().getUserPushID());
    }

    @Test
    public void testPassesContextInAuthsRequest() throws Exception {
        String expected = "Expected Context";
        service.authorize("username", "Expected Context");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getContext());
    }

    @Test
    public void testPassesContextWithNoExceptionWhenContextIs400Chars() throws Exception {
        String expected = (new String(new char[400])).replace("\0", "x");
        service.authorize("username", expected);
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRaisesIllegalArgumentExceptionWhenContextGreaterThan400Chars() throws Exception {
        String expected = (new String(new char[401])).replace("\0", "x");
        service.authorize("username", expected);
    }
}