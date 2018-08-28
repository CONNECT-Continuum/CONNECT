/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.adminguimanagement.AdminGUIManagementPortType;
import gov.hhs.fha.nhinc.common.adminguimanagement.AdminGUIRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.DashboardStatusMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.EventLogMessageType;

public class DashboardStatusService implements AdminGUIManagementPortType {

    @Override
    public DashboardStatusMessageType dashboardStatus(AdminGUIRequestMessageType req) {

        DashboardStatusMessageType resp = new DashboardStatusMessageType();
        resp.setMemory("100 GB");
        resp.setOS("WINDURRS 10");
        resp.setServer("Wildfly 8.2.1.Final");
        resp.setVersion("Java 1.8");

        EventLogMessageType event = new EventLogMessageType();
        event.setEvent("Patient Discovery");
        event.setInbound(100);
        event.setOutbound(120);

        resp.getEvent().add(event);
        return null;
    }

}
