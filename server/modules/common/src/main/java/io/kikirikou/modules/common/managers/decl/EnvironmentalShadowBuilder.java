// Copyright 2007 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.kikirikou.modules.common.managers.decl;

public interface EnvironmentalShadowBuilder {
    /**
     * Returns a proxy that delegates all methods to an object obtained from {@link Environment#peekRequired(Class)}.
     * Note that at the time this method is invoked, the Environment service may still be virtual, and will often not
     * yet have been loaded with values, and that's OK, the resolution is deferred to the instant a method is invoked.
     *
     * @param <T>
     * @param serviceType the service type, which is used to obtained the delegate instance
     * @return a proxy to the service
     */
    <T> T build(Class<T> serviceType);
}
