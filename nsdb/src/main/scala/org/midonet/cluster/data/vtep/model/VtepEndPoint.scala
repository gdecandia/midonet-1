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

package org.midonet.cluster.data.vtep.model

import org.midonet.packets.IPv4Addr

/** VxLAN end point management interface.  This represents the management
  * port and IP at which a VTEP's configuration database can be manipulated.
  */
case class VtepEndPoint(mgmtIp: IPv4Addr, mgmtPort: Int) {
    private val str: String = s"$mgmtIp:$mgmtPort"
    override def toString = str
}
