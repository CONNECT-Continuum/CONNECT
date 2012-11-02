/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.event.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author akong
 * 
 */
public class ErrorExtractorTest {

    @Test
    public void extract() {

        RegistryError registryError1 = new RegistryError();
        registryError1.setErrorCode("errorCode1");

        RegistryError registryError2 = new RegistryError();
        registryError2.setErrorCode("errorCode2");

        RegistryError registryErrorEmpty = new RegistryError();

        List<RegistryError> registryErrorList = new ArrayList<RegistryError>();
        registryErrorList.add(registryError1);
        registryErrorList.add(registryError2);
        registryErrorList.add(registryErrorEmpty);

        List<String> errorCodeList = Lists.transform(registryErrorList, new ErrorExtractor());

        assertEquals(3, errorCodeList.size());
        assertEquals("errorCode1", errorCodeList.get(0));
        assertEquals("errorCode2", errorCodeList.get(1));
        assertEquals(null, errorCodeList.get(2));
    }

}
