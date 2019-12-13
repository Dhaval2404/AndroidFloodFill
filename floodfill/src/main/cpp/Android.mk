# Ref: https://developer.android.com/ndk/guides/android_mk

# location of the source files in the development tree.
# Here, the macro function my-dir, provided by the build system, returns the path of the current directory
LOCAL_PATH := $(call my-dir)

# Clear LOCAL_XXX variables
include $(CLEAR_VARS)

LOCAL_SRC_FILES := floodfill.cpp
LOCAL_MODULE    := floodfill
LOCAL_LDLIBS    := -lm -llog -ljnigraphics

# This line helps the system tie everything together:
include $(BUILD_SHARED_LIBRARY)