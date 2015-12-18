LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

#LOCAL_JAVA_LIBRARIES := telephony-common voip-common telephony-msim
#LOCAL_STATIC_JAVA_LIBRARIES := com.android.phone.shared \
#        com.android.services.telephony.common \
#        libphonenumbergoogle \
#        guava \

LOCAL_SRC_FILES := $(call all-java-files-under, src)
#LOCAL_STATIC_JAVA_LIBRARIES := #httpclient #httpcore junit libthrift log4j servlet-api slf4j-api slf4j-api slf4j-log4j12 #commons-logging commons-codec
LOCAL_STATIC_JAVA_LIBRARIES := slf4j-log4j12 slf4j-api log4j javaxannot libthrift #servlet-api slf4j-log4j12 #slf4j-api log4j 

#LOCAL_SRC_FILES += \
#        src/com/android/phone/EventLogTags.logtags \
#        src/com/android/phone/INetworkQueryService.aidl \
#        src/com/android/phone/INetworkQueryServiceCallback.aidl \
#        src/org/codeaurora/ims/IImsService.aidl \
#        src/org/codeaurora/ims/IImsServiceListener.aidl \
#        src/org/codeaurora/btmultisim/IBluetoothDsdaService.aidl

LOCAL_PACKAGE_NAME := EyeService 

LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PROGUARD_FLAG_FILES := proguard.flags

include $(BUILD_PACKAGE)

# Build the test package
#include $(call all-makefiles-under,$(LOCAL_PATH))

#-------------- BEGIN JAVA LIBRARIES ------------
include $(CLEAR_VARS)
#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := #httpclient:libs/httpclient-4.4.1.jar #httpcore:libs/httpcore-4.4.1.jar junit:libs/junit-4.4.jar #libthrift:libs/libthrift-0.9.3.jar log4j:libs/log4j-1.2.14.jar servlet-api:libs/servlet-api-2.5.jar slf4j-api:libs/slf4j-api-1.5.8.jar slf4j-log4j12:libs/slf4j-log4j12-1.5.8.jar #commons-logging:libs/commons-logging-1.2.jar commons-codec:libs/commons-codec-1.9.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := libthrift:libs/libthrift-0.9.3.jar slf4j-api2:libs/slf4j-api-1.7.12.jar slf4j-log4j12:libs/slf4j-log4j12-1.5.8.jar slf4j-api:libs/slf4j-api-1.5.8.jar log4j:libs/log4j-1.2.14.jar servlet-api:libs/servlet-api-2.5.jar javaxannot:libs/javax.annotation-3.2-b06.jar
include $(BUILD_MULTI_PREBUILT)
