/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zeppelin.security;

import static org.junit.Assert.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.utils.SecurityUtils;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.InetAddress;


public class SecurityUtilsTest {

  @Test
  public void isInvalid() throws URISyntaxException, UnknownHostException {
    assertFalse(SecurityUtils.isValidOrigin("http://127.0.1.1", ZeppelinConfiguration.create()));
  }

  @Test
  public void isInvalidFromConfig() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertFalse(SecurityUtils.isValidOrigin("http://otherinvalidhost.com",
          new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site.xml"))));
  }

  @Test
  public void isLocalhost() throws URISyntaxException, UnknownHostException {
    assertTrue(SecurityUtils.isValidOrigin("http://localhost", ZeppelinConfiguration.create()));
  }

  @Test
  public void isLocalMachine() throws URISyntaxException, UnknownHostException {
    String origin = "http://" + InetAddress.getLocalHost().getHostName();
    assertTrue("Origin " + origin + " is not allowed. Please check your hostname.",
               SecurityUtils.isValidOrigin(origin, ZeppelinConfiguration.create()));
  }

  @Test
  public void isValidFromConfig() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertTrue(SecurityUtils.isValidOrigin("http://otherhost.com",
           new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site.xml"))));
  }

  @Test
  public void isValidFromStar() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertTrue(SecurityUtils.isValidOrigin("http://anyhost.com",
           new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site-star.xml"))));
  }

  @Test
  public void nullOrigin() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertFalse(SecurityUtils.isValidOrigin(null,
          new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site.xml"))));
  }

  @Test
  public void emptyOrigin() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertFalse(SecurityUtils.isValidOrigin("",
          new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site.xml"))));
  }

  @Test
  public void notAURIOrigin() throws URISyntaxException, UnknownHostException, ConfigurationException {
    assertFalse(SecurityUtils.isValidOrigin("test123",
          new ZeppelinConfiguration(this.getClass().getResource("/zeppelin-site.xml"))));
  }
}
