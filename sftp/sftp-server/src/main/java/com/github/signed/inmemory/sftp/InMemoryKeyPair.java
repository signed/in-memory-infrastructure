/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.signed.inmemory.sftp;

import java.security.KeyPair;
import java.security.interfaces.DSAKey;
import java.security.interfaces.RSAKey;

import org.apache.sshd.common.KeyPairProvider;

public class InMemoryKeyPair implements KeyPairProvider {

    public static KeyPairProvider GenerateNewKeyPair(){
        return new InMemoryKeyPair(new KeyPairGenerator().dsa().generateKeyPair());
    }

    private final KeyPair keyPair;

    public InMemoryKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public KeyPair loadKey(String type) {
        assert type != null;
        if( type.equals(getKeyType())){
            return keyPair;
        }
        return null;
    }

    public String getKeyTypes() {
        return  getKeyType();
    }

    private String getKeyType() {
        Object key = keyPair.getPrivate() != null ? keyPair.getPrivate() : keyPair.getPublic();
        if (key instanceof DSAKey) {
            return SSH_DSS;
        } else if (key instanceof RSAKey) {
            return SSH_RSA;
        }
        return null;
    }
}
