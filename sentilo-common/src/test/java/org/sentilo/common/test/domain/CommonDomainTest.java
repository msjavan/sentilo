/*
 * Sentilo
 * 
 * Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de Barcelona.
 * 
 * This program is licensed and may be used, modified and redistributed under the terms of the
 * European Public License (EUPL), either version 1.1 or (at your option) any later version as soon
 * as they are approved by the European Commission.
 * 
 * Alternatively, you may redistribute and/or modify this program under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * 
 * See the licenses for the specific language governing permissions, limitations and more details.
 * 
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along with this program;
 * if not, you may find them at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl http://www.gnu.org/licenses/ and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.common.test.domain;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sentilo.common.domain.AlertOwner;
import org.sentilo.common.domain.AuthorizedProvider;
import org.sentilo.common.domain.CatalogAlert;
import org.sentilo.common.domain.CatalogAlertInputMessage;
import org.sentilo.common.domain.CatalogAlertResponseMessage;
import org.sentilo.common.domain.CatalogComponent;
import org.sentilo.common.domain.CatalogDeleteInputMessage;
import org.sentilo.common.domain.CatalogInputMessage;
import org.sentilo.common.domain.CatalogProvider;
import org.sentilo.common.domain.CatalogResponseMessage;
import org.sentilo.common.domain.CatalogSensor;
import org.sentilo.common.domain.EventMessage;
import org.sentilo.common.domain.OrderMessage;
import org.sentilo.common.domain.QueryFilterParams;
import org.sentilo.common.domain.SensorOrdersMessage;
import org.sentilo.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.DataBinder;


public class CommonDomainTest {

  @Test
  public void alertOwner() throws Exception {
    final Object[] values = {"mockAlert","mockProvider"};
    final String[] attributes = {"alertId","ownerEntityId"};

    buildAndCompareObjects(AlertOwner.class, attributes, values);
  }

  @Test
  public void authorizedProvider() throws Exception {
    final Object[] values = {"mockProvider", "READ", Collections.<CatalogSensor>emptyList()};
    final String[] attributes = {"provider", "permission", "sensors"};

    buildAndCompareObjects(AuthorizedProvider.class, attributes, values);
  }

  @Test
  public void catalogAlert() throws Exception {
    final Object[] values =
        {"mock id", "mock name", "mock description", "mockEntity", "mockType", "mockTrigger", "mockExpression", "mockComponent", "mockSensor"};
    final String[] attributes = {"id", "name", "description", "entity", "type", "trigger", "expression", "component", "sensor"};

    bindAndValidateMutableObject(CatalogAlert.class, attributes, values);

  }

  @Test
  public void catalogAlertInputMessage() throws Exception {
    final Object[] values = {Collections.<CatalogAlert>emptyList(), new String[1], "mockEntity", Collections.<String, String>emptyMap()};
    final String[] attributes = {"alerts", "alertsIds", "entityId", "parameters"};
    final Object[] constructorValues = {"mockEntity", Collections.<String, String>emptyMap()};

    buildAndCompareObjects(CatalogAlertInputMessage.class, attributes, values, constructorValues);
  }

  @Test
  public void catalogAlertResponseMessage() throws Exception {
    final Object[] values =
        {Collections.<CatalogAlert>emptyList(), Collections.<AlertOwner>emptyList(), "mockCode", "mockMessage", Collections.<String>emptyList()};
    final String[] attributes = {"alerts", "owners", "code", "errorMessage", "errorDetails"};

    final Object[] constructorValues = {"mockCode", "mockMessage", Collections.<String>emptyList()};
    final Object[] constructorValues2 = {"mockCode", "mockMessage"};
    final Object[] constructorValues3 = {"mockCode"};

    // Validate the 3 constructors of CatalogAlertResponseMessage
    buildAndCompareObjects(CatalogAlertResponseMessage.class, attributes, values, constructorValues);
    buildAndCompareObjects(CatalogAlertResponseMessage.class, attributes, values, constructorValues2);
    buildAndCompareObjects(CatalogAlertResponseMessage.class, attributes, values, constructorValues3);
  }

  @Test
  public void catalogComponent() throws Exception {
    final Object[] values = {"mock id", "mock location", "mock type", "mock desc", true};
    final String[] attributes = {"component", "location", "componentType", "componentDesc", "componentPublicAccess"};

    bindAndValidateMutableObject(CatalogComponent.class, attributes, values);
  }

  @Test
  public void catalogDeleteInputMessage() throws Exception {
    // It is a singular case, where setters / getters are not homogeneous
    CatalogDeleteInputMessage message = new CatalogDeleteInputMessage();
    message.setSensors(new String[1]);
    message.setComponents(new String[1]);

    Assert.assertArrayEquals(message.getSensorsIds(), new String[1]);
    Assert.assertArrayEquals(message.getComponentsIds(), new String[1]);
  }

  @Test
  public void catalogInputMessage() throws Exception {
    final Object[] values =
        {"mock provider", "mock entity", "mock body", Collections.<String, String>emptyMap(), Collections.<CatalogSensor>emptyList(),
            Collections.<CatalogComponent>emptyList()};
    final String[] attributes = {"providerId", "entityId", "body", "parameters", "sensors", "components"};
    final Object[] constructorValues = {"mock entity", Collections.<String, String>emptyMap()};

    buildAndCompareObjects(CatalogInputMessage.class, attributes, values, constructorValues);
  }

  @Test
  public void catalogProvider() throws Exception {
    final Object[] values = {"mock provider"};
    final String[] attributes = {"provider"};

    bindAndValidateMutableObject(CatalogProvider.class, attributes, values);
  }

  @Test
  public void catalogResponseMessage() throws Exception {
    final Object[] values = {Collections.<AuthorizedProvider>emptyList(), "mockCode", "mockMessage", Collections.<String>emptyList()};
    final String[] attributes = {"providers", "code", "errorMessage", "errorDetails"};

    final Object[] constructorValues = {"mockCode", "mockMessage", Collections.<String>emptyList()};
    final Object[] constructorValues2 = {"mockCode", "mockMessage"};
    final Object[] constructorValues3 = {"mockCode"};
    final Object[] constructorValues4 = {Collections.<AuthorizedProvider>emptyList()};

    // Validate the 3 constructors of CatalogAlertResponseMessage
    buildAndCompareObjects(CatalogResponseMessage.class, attributes, values, constructorValues);
    buildAndCompareObjects(CatalogResponseMessage.class, attributes, values, constructorValues2);
    buildAndCompareObjects(CatalogResponseMessage.class, attributes, values, constructorValues3);
    buildAndCompareObjects(CatalogResponseMessage.class, attributes, values, constructorValues4);
  }

  @Test
  public void catalogSensor() throws Exception {
    final Object[] values =
        {"mock sensor", "mock provider", "mock desc", "mock data type", "mock location", "mock type", "mock unit", "mock tz", true, "mock comp",
            "mock comp type", "mock comp desc", false, Collections.<String, String>emptyMap()};
    final String[] attributes =
        {"sensor", "provider", "description", "dataType", "location", "type", "unit", "timeZone", "publicAccess", "component", "componentType",
            "componentDesc", "componentPublicAccess", "additionalInfo"};

    bindAndValidateMutableObject(CatalogSensor.class, attributes, values);
  }

  @Test
  public void eventMessage() throws Exception {
    final Object[] values =
        {"mock msg", "mock ts", "mock topic", "mock type", "mock sensor", "mock provider", "mock location", "mock senser", "mock alert"};
    final String[] attributes = {"message", "timestamp", "topic", "type", "sensor", "provider", "location", "sender", "alert"};

    bindAndValidateMutableObject(EventMessage.class, attributes, values);
  }

  @Test
  public void orderMessage() throws Exception {
    final Long currentTs = System.currentTimeMillis();
    final Object[] values = {"mock order", "mock sender", DateUtils.toStringTimestamp(new Date(currentTs))};
    final String[] attributes = {"order", "sender", "timestamp"};
    final Object[] constructorValues = {"mock order"};
    final Object[] constructorValues2 = {"mock order", "mock sender"};
    final Object[] constructorValues3 = {"mock order", "mock sender", currentTs};

    final Object[] valuesWithNullTs = {"mock order", "mock sender", null};
    final Object[] constructorValuesWithNullTs = {"mock order", "mock sender", null};

    bindAndValidateMutableObject(OrderMessage.class, attributes, values);
    buildAndCompareObjects(OrderMessage.class, attributes, values, constructorValues);
    buildAndCompareObjects(OrderMessage.class, attributes, values, constructorValues2);
    buildAndCompareObjects(OrderMessage.class, attributes, values, constructorValues3);
    buildAndCompareObjects(OrderMessage.class, attributes, valuesWithNullTs, constructorValuesWithNullTs);


  }

  @Test
  public void queryFilterParams() {
    final Date currentDate = new Date();
    final Long currentTs = currentDate.getTime();
    final String[] attributes = {"from", "to", "limit"};
    final Object[] values = {null, null, 10};
    final Object[] values2 = {currentDate, currentDate, 10};
    final Object[] values3 = {currentDate, currentDate, null};

    assertFieldValues(new QueryFilterParams(10), attributes, values);
    assertFieldValues(new QueryFilterParams(currentTs, currentTs, 10), attributes, values2);
    assertFieldValues(new QueryFilterParams(currentDate, currentDate), attributes, values3);
  }

  @Test
  public void sensorOrdersMessage() throws Exception {
    final Object[] values = {"mock sensor", Collections.<OrderMessage>emptyList()};
    final String[] attributes = {"sensor", "orders"};

    bindAndValidateMutableObject(SensorOrdersMessage.class, attributes, values);
  }

  // Utility methods

  private <T> void buildAndCompareObjects(Class<T> clazz, String[] attributes, Object[] values) throws Exception {
    buildAndCompareObjects(clazz, attributes, values, values);
  }

  private <T> void buildAndCompareObjects(Class<T> clazz, String[] attributes, Object[] values, Object[] constructorValues) throws Exception {
    T message = clazz.newInstance();
    bindProperties(message, attributes, values);

    T message2 = buildInstance(clazz, constructorValues);
    if (!constructorValues.equals(values)) {
      bindProperties(message2, attributes, values);
    }

    assertAreEquals(message, message2);
  }

  private <T> void bindAndValidateMutableObject(Class<T> clazz, String[] attributes, Object[] values) throws Exception {
    T target = clazz.newInstance();

    bindProperties(target, attributes, values);

    assertFieldValues(target, attributes, values);
  }

  private void assertFieldValues(Object target, String[] attributes, Object[] values) {
    for (int i = 0; i < attributes.length; i++) {
      Assert.assertEquals("Error validating attribute " + attributes[i], values[i], ReflectionTestUtils.getField(target, attributes[i]));
    }
  }

  private void bindProperties(Object target, String[] attributes, Object[] values) {
    DataBinder binder = new DataBinder(target);
    binder.bind(buildMutablePropertyValues(attributes, values));
  }

  private MutablePropertyValues buildMutablePropertyValues(String[] attributes, Object[] values) {
    assert attributes != null && values != null && attributes.length == values.length;
    Map<String, Object> properties = new HashMap<String, Object>();

    for (int i = 0; i < attributes.length; i++) {
      properties.put(attributes[i], values[i]);
    }

    return new MutablePropertyValues(properties);
  }

  @SuppressWarnings("unchecked")
  private <T> T buildInstance(Class<T> clazz, Object[] params) throws Exception {
    T newInstance = null;
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      try{
        newInstance = (T)constructor.newInstance(params);
        break;
      } catch (Exception e) {
      }
    }

    if (newInstance != null) {
      return newInstance;
    } else {
      String template = "Class %s has not constructor with %d params";
      throw new IllegalArgumentException(String.format(template, clazz.getName(), params.length));

    }
  }

  private void assertAreEquals(Object source, Object target) throws Exception {
    Assert.assertEquals(source.getClass(), target.getClass());

    PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());

    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      Method readMethod = propertyDescriptor.getReadMethod();
      if (!readMethod.getName().equals("getClass")) {
        Assert.assertEquals(readMethod.invoke(source), readMethod.invoke(target));
      }
    }

  }

}
