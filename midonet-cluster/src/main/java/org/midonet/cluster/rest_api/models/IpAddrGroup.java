/*
 * Copyright 2014 Midokura SARL
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
import java.util.UUID;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlRootElement;

import org.midonet.cluster.data.ZoomClass;
import org.midonet.cluster.data.ZoomField;
import org.midonet.cluster.models.Topology;
import org.midonet.cluster.rest_api.ResourceUris;
import org.midonet.cluster.util.UUIDUtil;


/**
 * Class representing a IP address group.
 */
@XmlRootElement
@ZoomClass(clazz = Topology.IPAddrGroup.class)
public class IpAddrGroup extends UriResource {

    public static final int MIN_IP_ADDR_GROUP_NAME_LEN = 1;
    public static final int MAX_IP_ADDR_GROUP_NAME_LEN = 255;

    @ZoomField(name = "id", converter = UUIDUtil.Converter.class)
    public UUID id;

    @NotNull
    @Size(min = MIN_IP_ADDR_GROUP_NAME_LEN, max = MAX_IP_ADDR_GROUP_NAME_LEN)
    @ZoomField(name = "name")
    public String name;

    public IpAddrGroup() {
    }

    public IpAddrGroup(org.midonet.cluster.data.IpAddrGroup data) {
        this(data.getId(), data.getName());
    }

    public IpAddrGroup(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public URI getUri() {
        return absoluteUri(ResourceUris.IP_ADDR_GROUPS, id);
    }

    @SuppressWarnings("unused") // used by serializers
    public URI getAddrs() {
        return UriBuilder.fromUri(relativeUri(ResourceUris.IP_ADDRS))
                         .build();
    }

    public org.midonet.cluster.data.IpAddrGroup toData() {

        return new org.midonet.cluster.data.IpAddrGroup()
                .setId(this.id)
                .setName(this.name);
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name;
    }

    /**
     * Interface used for a Validation group. This group gets triggered after
     * the default validations.
     */
    public interface IpAddrGroupExtended {
    }

    /**
     * Interface used for validating a IP addr group on creates.
     */
    public interface IpAddrGroupCreateGroup {
    }

    /**
     * Interface that defines the ordering of validation groups for IP addr
     * group create.
     */
    @GroupSequence({ Default.class, IpAddrGroupCreateGroup.class,
                     IpAddrGroupExtended.class })
    public interface IpAddrGroupCreateGroupSequence {
    }

    @Override
    public void create() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}

