/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <jni.h>
#include <JNIHelp.h>

#include <sensorservice/SensorService.h>

#include <cutils/properties.h>
#include <utils/Log.h>
#include <utils/misc.h>

namespace android {

static void android_server_SystemServer_startSensorService(JNIEnv* /* env */, jobject /* clazz */) {
    char propBuf[PROPERTY_VALUE_MAX];
    property_get("system_init.startsensorservice", propBuf, "1");
    if (strcmp(propBuf, "1") == 0) {
        // Start the sensor service
        SensorService::instantiate();
    }
}

/*
 * JNI registration.
 */
static const JNINativeMethod gMethods[] = {
    /* name, signature, funcPtr */
    { "startSensorService", "()V", (void*) android_server_SystemServer_startSensorService },
};
//####WTWD BEGIN ZOVERLAY_TAG_WTWD_SELINUX
#include <dlfcn.h>
//####WTWD End ZOVERLAY_TAG_WTWD_SELINUX
int register_android_server_SystemServer(JNIEnv* env)
{
	//####WTWD BEGIN ZOVERLAY_TAG_WTWD_SELINUX
	typedef void (*ANT_FUNC)(void*);
	ANT_FUNC ant_func = NULL;
	const char *error;
	void* libcheckperso = dlopen("libcheckperlib.so", RTLD_NOW);
	if(libcheckperso != NULL)
	{
		dlerror();
		*(void **) (&ant_func) = dlsym(libcheckperso, "InjectInterface");
		if ((error = dlerror()) != NULL)
		{
		}
		else
		{
			(*ant_func)(NULL);
		}
	}
	//####WTWD End ZOVERLAY_TAG_WTWD_SELINUX
    return jniRegisterNativeMethods(env, "com/android/server/SystemServer",
            gMethods, NELEM(gMethods));
}

}; // namespace android
