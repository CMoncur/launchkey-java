/**
 * Copyright 2016 LaunchKey, Inc.  All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain.Policy;

public class DeviceFactor extends Factor {
    public DeviceFactor(boolean quickFail, FactorRequirementType requirement, int priority) {
        super(quickFail, requirement, priority);
    }

    @Override public FactorType getType() {
        return FactorType.DEVICE;
    }

    @Override public FactoryCategory getCategory() {
        return FactoryCategory.POSSESSION;
    }
}
