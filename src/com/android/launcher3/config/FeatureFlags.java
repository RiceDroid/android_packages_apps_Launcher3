/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.launcher3.config;

import android.content.Context;

import com.android.launcher3.BuildConfig;
import com.android.launcher3.Utilities;
import com.android.launcher3.uioverrides.DeviceFlag;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a set of flags used to control various launcher behaviors.
 *
 * <p>All the flags should be defined here with appropriate default values.
 */
public final class FeatureFlags {

    private static final List<DebugFlag> sDebugFlags = new ArrayList<>();

    public static final String FLAGS_PREF_NAME = "featureFlags";

    private FeatureFlags() { }

    public static boolean showFlagTogglerUi(Context context) {
        return Utilities.IS_DEBUG_DEVICE && Utilities.isDevelopersOptionsEnabled(context);
    }

    /**
     * True when the build has come from Android Studio and is being used for local debugging.
     */
    public static final boolean IS_STUDIO_BUILD = BuildConfig.DEBUG;

    /**
     * Enable moving the QSB on the 0th screen of the workspace. This is not a configuration feature
     * and should be modified at a project level.
     */
    public static final boolean QSB_ON_FIRST_SCREEN = true;

    /**
     * Feature flag to handle define config changes dynamically instead of killing the process.
     *
     *
     * To add a new flag that can be toggled through the flags UI:
     *
     * Declare a new ToggleableFlag below. Give it a unique key (e.g. "QSB_ON_FIRST_SCREEN"),
     *    and set a default value for the flag. This will be the default value on Debug builds.
     */
    // When enabled the promise icon is visible in all apps while installation an app.
    public static final BooleanFlag PROMISE_APPS_IN_ALL_APPS = getDebugFlag(
            "PROMISE_APPS_IN_ALL_APPS", false, "Add promise icon in all-apps");

    // When enabled a promise icon is added to the home screen when install session is active.
    public static final BooleanFlag PROMISE_APPS_NEW_INSTALLS = getDebugFlag(
            "PROMISE_APPS_NEW_INSTALLS", true,
            "Adds a promise icon to the home screen for new install sessions.");

    public static final BooleanFlag APPLY_CONFIG_AT_RUNTIME = getDebugFlag(
            "APPLY_CONFIG_AT_RUNTIME", true, "Apply display changes dynamically");

    public static final BooleanFlag QUICKSTEP_SPRINGS = getDebugFlag(
            "QUICKSTEP_SPRINGS", true, "Enable springs for quickstep animations");

    public static final BooleanFlag UNSTABLE_SPRINGS = getDebugFlag(
            "UNSTABLE_SPRINGS", false, "Enable unstable springs for quickstep animations");

    public static final BooleanFlag ADAPTIVE_ICON_WINDOW_ANIM = getDebugFlag(
            "ADAPTIVE_ICON_WINDOW_ANIM", true, "Use adaptive icons for window animations.");

    public static final BooleanFlag ENABLE_QUICKSTEP_LIVE_TILE = getDebugFlag(
            "ENABLE_QUICKSTEP_LIVE_TILE", false, "Enable live tile in Quickstep overview");

    public static final BooleanFlag ENABLE_HINTS_IN_OVERVIEW = getDebugFlag(
            "ENABLE_HINTS_IN_OVERVIEW", false, "Show chip hints and gleams on the overview screen");

    public static final BooleanFlag FAKE_LANDSCAPE_UI = getDebugFlag(
            "FAKE_LANDSCAPE_UI", false, "Rotate launcher UI instead of using transposed layout");

    public static final BooleanFlag FOLDER_NAME_SUGGEST = getDebugFlag(
            "FOLDER_NAME_SUGGEST", true, "Suggests folder names instead of blank text.");

    public static final BooleanFlag APP_SEARCH_IMPROVEMENTS = new DeviceFlag(
            "APP_SEARCH_IMPROVEMENTS", false,
            "Adds localized title and keyword search and ranking");

    public static final BooleanFlag ENABLE_PREDICTION_DISMISS = new DeviceFlag(
            "ENABLE_PREDICTION_DISMISS", false, "Allow option to dimiss apps from predicted list");

    public static final BooleanFlag ENABLE_QUICK_CAPTURE_GESTURE = getDebugFlag(
            "ENABLE_QUICK_CAPTURE_GESTURE", true, "Swipe from right to left to quick capture");

    public static final BooleanFlag ASSISTANT_GIVES_LAUNCHER_FOCUS = getDebugFlag(
            "ASSISTANT_GIVES_LAUNCHER_FOCUS", false,
            "Allow Launcher to handle nav bar gestures while Assistant is running over it");

    public static final BooleanFlag ENABLE_HYBRID_HOTSEAT = new DeviceFlag(
            "ENABLE_HYBRID_HOTSEAT", false, "Fill gaps in hotseat with predicted apps");

    public static final BooleanFlag ENABLE_DEEP_SHORTCUT_ICON_CACHE = getDebugFlag(
            "ENABLE_DEEP_SHORTCUT_ICON_CACHE", true, "R/W deep shortcut in IconCache");

    public static final BooleanFlag MULTI_DB_GRID_MIRATION_ALGO = getDebugFlag(
            "MULTI_DB_GRID_MIRATION_ALGO", false, "Use the multi-db grid migration algorithm");

    public static final BooleanFlag ENABLE_LAUNCHER_PREVIEW_IN_GRID_PICKER = getDebugFlag(
            "ENABLE_LAUNCHER_PREVIEW_IN_GRID_PICKER", false,
            "Show launcher preview in grid picker");

    public static final BooleanFlag ENABLE_OVERVIEW_ACTIONS = getDebugFlag(
            "ENABLE_OVERVIEW_ACTIONS", false, "Show app actions instead of the shelf in Overview."
            + " As part of this decoupling, also distinguish swipe up from nav bar vs above it.");

    public static final BooleanFlag ENABLE_DATABASE_RESTORE = getDebugFlag(
            "ENABLE_DATABASE_RESTORE", true,
            "Enable database restore when new restore session is created");

    public static final BooleanFlag ENABLE_UNIVERSAL_SMARTSPACE = getDebugFlag(
            "ENABLE_UNIVERSAL_SMARTSPACE", false,
            "Replace Smartspace with a version rendered by System UI.");

    public static void initialize(Context context) {
        synchronized (sDebugFlags) {
            for (DebugFlag flag : sDebugFlags) {
                flag.initialize(context);
            }
        }
    }

    static List<DebugFlag> getDebugFlags() {
        synchronized (sDebugFlags) {
            return new ArrayList<>(sDebugFlags);
        }
    }

    public static void dump(PrintWriter pw) {
        pw.println("FeatureFlags:");
        synchronized (sDebugFlags) {
            for (DebugFlag flag : sDebugFlags) {
                pw.println("  " + flag.key + "=" + flag.get());
            }
        }
    }

    public static class BooleanFlag {

        public final String key;
        public boolean defaultValue;

        public BooleanFlag(String key, boolean defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public boolean get() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return appendProps(new StringBuilder()
                    .append(getClass().getSimpleName()).append('{'))
                    .append('}').toString();
        }

        protected StringBuilder appendProps(StringBuilder src) {
            return src.append("key=").append(key).append(", defaultValue=").append(defaultValue);
        }

        public void addChangeListener(Context context, Runnable r) { }
    }

    public static class DebugFlag extends BooleanFlag {

        public final String description;
        private boolean mCurrentValue;

        public DebugFlag(String key, boolean defaultValue, String description) {
            super(key, defaultValue);
            this.description = description;
            mCurrentValue = this.defaultValue;
            synchronized (sDebugFlags) {
                sDebugFlags.add(this);
            }
        }

        @Override
        public boolean get() {
            return mCurrentValue;
        }

        public void initialize(Context context) {
            mCurrentValue = context.getSharedPreferences(FLAGS_PREF_NAME, Context.MODE_PRIVATE)
                    .getBoolean(key, defaultValue);
        }

        @Override
        protected StringBuilder appendProps(StringBuilder src) {
            return super.appendProps(src).append(", mCurrentValue=").append(mCurrentValue)
                    .append(", description=").append(description);
        }
    }

    private static BooleanFlag getDebugFlag(String key, boolean defaultValue, String description) {
        return Utilities.IS_DEBUG_DEVICE
                ? new DebugFlag(key, defaultValue, description)
                : new BooleanFlag(key, defaultValue);
    }
}