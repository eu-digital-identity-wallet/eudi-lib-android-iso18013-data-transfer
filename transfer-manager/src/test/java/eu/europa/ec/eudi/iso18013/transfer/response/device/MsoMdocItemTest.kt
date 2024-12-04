/*
 * Copyright (c) 2024 European Commission
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

package eu.europa.ec.eudi.iso18013.transfer.response.device

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class MsoMdocItemTest {

    @Test
    fun `test equals method`() {
        val msoMdocItem1 = MsoMdocItem(
            namespace = "nameSpace1",
            elementIdentifier = "elementIdentifier1",
        )

        val msoMdocItem2 = MsoMdocItem(
            namespace = "nameSpace1",
            elementIdentifier = "elementIdentifier1",
        )

        assertEquals(msoMdocItem1, msoMdocItem2)


        val msoMdocItem3 = MsoMdocItem(
            namespace = "nameSpace1",
            elementIdentifier = "elementIdentifier1",
        )

        val msoMdocItem4 = MsoMdocItem(
            namespace = "nameSpace2",
            elementIdentifier = "elementIdentifier1",
        )

        assertNotEquals(msoMdocItem3, msoMdocItem4)
    }
}