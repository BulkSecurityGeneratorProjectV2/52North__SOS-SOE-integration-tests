/**
 * Copyright (C) 2014 - 2014
 * by 52°North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: http://52north.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.sos.soe;

import java.io.IOException;

import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.swe.x20.DataArrayDocument;

import org.apache.http.client.ClientProtocolException;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetObservationTest extends AbstractValidationTest {
	
	private static final Logger logger = LoggerFactory.getLogger(GetObservationTest.class);
	
	@Test
	public void validateGetObservation() throws ClientProtocolException, IllegalStateException, IOException, XmlException {
		String url = HttpUtil.resolveServiceURL().concat("GetObservation?service=SOS&version=2.0.0&request=GetObservation&offering=&observedProperty=http%3A%2F%2Fdd.eionet.europa.eu%2Fvocabulary%2Faq%2Fpollutant%2F1&procedure=http%3A%2F%2Fcdr.eionet.europa.eu%2Fpl%2Feu%2Faqd%2Fd%2Fenvuvt6ea%2FPL_REPORT_D_FOR_2014v2.xml%23SPO_P_PL0518A_1_001&featureOfInterest=&namespaces=&spatialFilter=&temporalFilter=om:phenomenonTime,2013-12-03T14:00:00Z/2014-03-04T14:00:00Z&aggregationType=&responseFormat=&f=xml");
		
		XmlObject xo = HttpUtil.executeGet(url);
		
		Assert.assertTrue("Not a GetObservationResponse: "+xo.getClass(), xo instanceof GetObservationResponseDocument);
		
		validateXml(xo);
		
		GetObservationResponseDocument obs = (GetObservationResponseDocument) xo;
		
		Assert.assertNotNull("Got no ObservationData!", obs.getGetObservationResponse().getObservationDataArray(0));
		Assert.assertNotNull("Got no Result!", obs.getGetObservationResponse().getObservationDataArray(0).getOMObservation().getResult());
		
		DataArrayDocument dad = DataArrayDocument.Factory.parse(obs.getGetObservationResponse().getObservationDataArray()[0].getOMObservation().getResult().xmlText());
		
		logger.info("Got a DataArray result: "+ dad.getDataArray1().getDomNode().getLocalName());
		
		validateXml(dad);
		
		Assert.assertTrue("No values!", dad.getDataArray1().getElementCount().getCount().getValue().intValue() > 0);
		
		logger.info("Value count: "+dad.getDataArray1().getElementCount().getCount().getValue().intValue());
	}

}
