/*
 * Copyright 2015 Midokura SARL
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

package org.midonet.cluster.rest_api.models;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import org.midonet.cluster.data.ZoomClass;
import org.midonet.cluster.data.ZoomField;
import org.midonet.cluster.models.Topology;
import org.midonet.cluster.rest_api.ResourceUris;
import org.midonet.cluster.util.UUIDUtil;

@ZoomClass(clazz = Topology.ServiceChain.class)
public class ServiceChain extends UriResource {

    @ZoomField(name = "id", converter = UUIDUtil.Converter.class)
    public UUID id;

    @ZoomField(name = "name")
    public String name;

    @ZoomField(name = "port", converter = UUIDUtil.Converter.class)
    @NotNull
    public UUID port;

    @ZoomField(name = "classifiers")
    public List<Condition> classifiers;

    public ServiceChain() {}

    @Override
    public URI getUri() {
        return absoluteUri(ResourceUris.SERVICE_CHAINS, id);
    }

    @JsonIgnore
    @Override
    public void create() {
        if (null == id) {
            id = UUID.randomUUID();
        }
    }

    @JsonIgnore
    public void update(ServiceChain from) {
        this.id = from.id;
        this.port = from.port;
    }
}
