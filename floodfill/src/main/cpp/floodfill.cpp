/**
 *
 * This file was modified by the author from the following original file:
 * https://raw.githubusercontent.com/lakmalz/ColouringImageFloodFill/master/app/src/main/cpp/jnibitmap.cpp
 */

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <queue>
#include <android/bitmap.h>

#define  LOG_TAG    "floodfill"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

std::vector<float> pixelsAll;

extern "C"
JNIEXPORT jfloatArray JNICALL Java_com_github_dhaval2404_floodfill_FloodFill_floodFill(JNIEnv *env,
                                                                                       jobject obj,
                                                                                       jobject bitmap,
                                                                                       jint x,
                                                                                       jint y,
                                                                                       jint fillColor,
                                                                                       jint targetColor,
                                                                                       jint tolerance);

bool isPixelValid(int currentColor, int oldColor, int *startColor, int tolerance);

void floodFill(JNIEnv *env,
               uint32_t x,
               uint32_t y,
               uint32_t fillColor,
               uint32_t targetColor,
               jobject bitmap,
               void *bitmapPixels,
               AndroidBitmapInfo *bitmapInfo,
               uint32_t tolerance);



//-----------------separator with method declaration----------------------------

//https://cppcodetips.wordpress.com/2014/02/25/returning-array-of-user-defined-objects-in-jni/
extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_github_dhaval2404_floodfill_FloodFill_floodFill(JNIEnv *env,
                                                         jobject obj,
                                                         jobject bitmap,
                                                         jint x,
                                                         jint y,
                                                         jint fillColor,
                                                         jint targetColor,
                                                         jint tolerance) {
    AndroidBitmapInfo bitmapInfo;
    LOGI("Floodfill start");

    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo)) < 0) {

        LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
        jfloatArray emptyArray = env->NewFloatArray(0);
        return emptyArray;
    }

    if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGE("Bitmap format is not RGBA_8888!");
        jfloatArray emptyArray = env->NewFloatArray(0);
        return emptyArray;
    }

    void *bitmapPixels;
    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &bitmapPixels)) < 0) {
        LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
        jfloatArray emptyArray = env->NewFloatArray(0);
        return emptyArray;
    }

    std::deque<uint32_t> pixelsAllX;
    std::deque<uint32_t> pixelsAllY;
    std::deque<uint32_t> queueFillColor;

    pixelsAllX.push_back(x);
    pixelsAllY.push_back(y);
    queueFillColor.push_back(fillColor);

    std::deque<uint32_t> pixelsLoopX = pixelsAllX;
    std::deque<uint32_t> pixelsLoopY = pixelsAllY;
    std::deque<uint32_t> queueLoopFillColor = queueFillColor;
    while (!pixelsLoopX.empty()) {
        int cx = pixelsLoopX.front();
        int cy = pixelsLoopY.front();
        uint32_t cFillColor = queueLoopFillColor.front();
        pixelsLoopX.pop_front();
        pixelsLoopY.pop_front();
        queueLoopFillColor.pop_front();

        floodFill(env, cx, cy, cFillColor, targetColor, bitmap, bitmapPixels, &bitmapInfo,
                  tolerance);
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    LOGE("Pixels:%d ", pixelsAll.size());

    pixelsAllX.clear();
    pixelsAllY.clear();
    queueFillColor.clear();

    uint32_t size = pixelsAll.size();
    jfloatArray result = env->NewFloatArray(size);
    float *data = pixelsAll.data();
    env->SetFloatArrayRegion(result, 0, size, data);
    pixelsAll.clear();

    LOGI("Floodfill finish");

    return result;
}

bool isPixelValid(int currentColor, int oldColor, int *startColor, int tolerance) {

    if (tolerance != 0) {
        int alpha = ((currentColor & 0xFF000000) >> 24);
        int red = ((currentColor & 0xFF0000) >> 16) * alpha / 255; // red
        int green = ((currentColor & 0x00FF00) >> 8) * alpha / 255; // Green
        int blue = (currentColor & 0x0000FF) * alpha / 255; // Blue

        return (red >= (startColor[0] - tolerance)
                && red <= (startColor[0] + tolerance)
                && green >= (startColor[1] - tolerance)
                && green <= (startColor[1] + tolerance)
                && blue >= (startColor[2] - tolerance)
                && blue <= (startColor[2] + tolerance));
    } else {
        return currentColor == oldColor;
    }
}

void floodFill(JNIEnv *env,
               uint32_t x,
               uint32_t y,
               uint32_t color,
               uint32_t targetColor,
               jobject bitmap,
               void *bitmapPixels,
               AndroidBitmapInfo *bitmapInfo,
               uint32_t tolerance) {

    // Used to hold the the start( touched ) color that we like to change/fill
    if (color == targetColor)
        return;

    int values[3] = {};

    if (x > bitmapInfo->width - 1)
        return;
    if (y > bitmapInfo->height - 1)
        return;
    if (x < 0)
        return;
    if (y < 0)
        return;

    uint32_t *pixels = (uint32_t *) bitmapPixels;

    uint32_t oldColor;

    int red = 0;
    int blue = 0;
    int green = 0;
    int alpha = 0;
    oldColor = pixels[y * bitmapInfo->width + x];

    // Get red,green and blue values of the old color we like to change
    alpha = (int) ((color & 0xFF000000) >> 24);

    values[0] = (int) ((oldColor & 0xFF0000) >> 16) * alpha / 255; // red
    values[1] = (int) ((oldColor & 0x00FF00) >> 8) * alpha / 255; // Green
    values[2] = (int) (oldColor & 0x0000FF) * alpha / 255; // Blue


    alpha = (int) ((color & 0xFF000000) >> 24);
    blue = (int) ((color & 0xFF0000) >> 16);
    green = (int) ((color & 0x00FF00) >> 8);
    red = (int) (color & 0x0000FF);
    blue = blue * alpha / 255;
    green = green * alpha / 255;
    red = red * alpha / 255;

    if (red < 200 && red == blue && blue == green) {
        return;
    }

    int tmp = 0;
    tmp = red;
    red = blue;
    blue = tmp;

    color = ((alpha << 24) & 0xFF000000) | ((blue << 16) & 0xFF0000) |
            ((green << 8) & 0x00FF00) |
            (red & 0x0000FF);

    //LOGD("edit1");
    std::queue<uint32_t> pixelsX;
    std::queue<uint32_t> pixelsY;

    uint32_t nx = 0;
    uint32_t ny = 0;
    uint32_t wx = 0;
    uint32_t wy = 0;
    uint32_t ex = 0;
    uint32_t ey = 0;

    pixelsX.push(x);
    pixelsY.push(y);

    while (!pixelsX.empty()) {

        nx = pixelsX.front();
        ny = pixelsY.front();
        pixelsX.pop();
        pixelsY.pop();

        if (pixels[ny * bitmapInfo->width + nx] == color)
            continue;

        wx = nx;
        wy = ny;
        ex = wx + 1;
        ey = wy;

        while (wx > 0 &&
               isPixelValid(pixels[wy * bitmapInfo->width + wx], oldColor, values, tolerance)) {
            pixels[wy * bitmapInfo->width + wx] = color;

            pixelsAll.push_back(wx);
            pixelsAll.push_back(wy);

            if (wy > 0 && isPixelValid(pixels[(wy - 1) * bitmapInfo->width + wx], oldColor, values,
                                       tolerance)) {
                pixelsX.push(wx);
                pixelsY.push(wy - 1);
            }
            if (wy < bitmapInfo->height - 1 &&
                isPixelValid(pixels[(wy + 1) * bitmapInfo->width + wx], oldColor, values,
                             tolerance)) {
                pixelsX.push(wx);
                pixelsY.push(wy + 1);
            }
            wx--;
        }

        while (ex < bitmapInfo->width - 1 &&
               isPixelValid(pixels[ey * bitmapInfo->width + ex], oldColor, values, tolerance)) {
            pixels[ey * bitmapInfo->width + ex] = color;

            pixelsAll.push_back(ex);
            pixelsAll.push_back(ey);

            if (ey > 0 && isPixelValid(pixels[(ey - 1) * bitmapInfo->width + ex], oldColor, values,
                                       tolerance)) {
                pixelsX.push(ex);
                pixelsY.push(ey - 1);

            }
            if (ey < bitmapInfo->height - 1 &&
                isPixelValid(pixels[(ey + 1) * bitmapInfo->width + ex], oldColor, values,
                             tolerance)) {
                pixelsX.push(ex);
                pixelsY.push(ey + 1);
            }
            ex++;
        }
    }
}
