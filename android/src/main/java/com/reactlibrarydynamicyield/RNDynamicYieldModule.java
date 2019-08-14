
package com.reactlibrarydynamicyield;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.content.Context;
import android.app.Application;
import android.view.View;

import com.dynamicyield.dyapi.DYApi;
import com.dynamicyield.engine.DYPageContext;
import com.dynamicyield.listener.itf.DYListenerItf;
import com.dynamicyield.state.DYExperimentsState;
import com.dynamicyield.state.DYInitState;
import com.dynamicyield.engine.DYCustomSettings;
import com.dynamicyield.userdata.external.DYUserData;
import com.dynamicyield.engine.DYRecommendationListenerItf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

import android.util.Log;

public class RNDynamicYieldModule extends ReactContextBaseJavaModule implements DYListenerItf, DYRecommendationListenerItf {

    private final ReactApplicationContext reactContext;
    private Application application;

    private static final String DY_TYPE_HOMEPAGE = "DY_TYPE_HOMEPAGE";
    private static final String DY_TYPE_CATEGORY = "DY_TYPE_CATEGORY";
    private static final String DY_TYPE_PRODUCT = "DY_TYPE_PRODUCT";
    private static final String DY_TYPE_CART = "DY_TYPE_CART";
    private static final String DY_TYPE_POST = "DY_TYPE_POST";
    private static final String DY_TYPE_OTHER = "DY_TYPE_OTHER";

    private static final String DY_EVENT_EXPERIMENTS_READY = "DY_EVENT_EXPERIMENTS_READY";
    private static final String DY_EVENT_RECOMMENDATION = "kRecommendation";
    private static final String DY_EVENT_USER_AFFINITY_SCORE = "kUserAffinityScore";
    private static final String DY_EVENT_DYNAMIC_VARIABLE = "kDynamicVariable";

    private static final String DY_EXP_STATE_READY = "READY";
    private static final String DY_EXP_STATE_NOT_READY = "NOT READY";

    private static final String TAG = "RbzLogs";

    public RNDynamicYieldModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.application = (Application) reactContext.getApplicationContext();
    }

    //***** DY Listener Implementation *****//

    @Override
    public void experimentsReadyWithState(DYExperimentsState dyExperimentsState) {

        WritableMap params = Arguments.createMap();
        // Log.d(TAG, "State: " + dyExperimentsState);
        //Call DY if the SDK have the latest experiments
        if (dyExperimentsState == DYExperimentsState.DY_READY_NO_UPDATE_NEEDED
                || dyExperimentsState == DYExperimentsState.DY_READY_AND_UPDATED) {
            params.putString("state", DY_EXP_STATE_READY);
            sendEvent(reactContext, DY_EVENT_EXPERIMENTS_READY, params);
        } else {
            params.putString("state", DY_EXP_STATE_NOT_READY);
            sendEvent(reactContext, DY_EVENT_EXPERIMENTS_READY, params);
        }

    }

    @Override
    public void onSmartObjectLoadResult(String s, String s1, View view) {

    }

    @Override
    public void onSmartActionReturned(String s, JSONObject jsonObject) {

    }

    @Override
    public void onRecommendationResult(JSONArray jsonArray, String widgetID) {
        WritableMap params = Arguments.createMap();
        params.putArray("recommendation", RNUtil.jsonArrayToWritableArray(jsonArray));
        params.putString("widgetID", widgetID);
        sendEvent(reactContext, DY_EVENT_RECOMMENDATION, params);
    }

    //***** DY Listener Implementation - END *****//

    @Override
    public String getName() {
        return "RNDynamicYield";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DY_TYPE_HOMEPAGE, DYPageContext.HOMEPAGE);
        constants.put(DY_TYPE_CATEGORY, DYPageContext.CATEGORY);
        constants.put(DY_TYPE_PRODUCT, DYPageContext.PRODUCT);
        constants.put(DY_TYPE_CART, DYPageContext.CART);
        constants.put(DY_TYPE_POST, DYPageContext.POST);
        constants.put(DY_TYPE_OTHER, DYPageContext.OTHER);

        constants.put("DY_EVENT_EXPERIMENTS_READY", DY_EVENT_EXPERIMENTS_READY);
        constants.put("DY_EVENT_USER_AFFINITY_SCORE", DY_EVENT_USER_AFFINITY_SCORE);
        constants.put("DY_EVENT_RECOMMENDATION", DY_EVENT_RECOMMENDATION);
        constants.put("DY_EVENT_DYNAMIC_VARIABLE", DY_EVENT_DYNAMIC_VARIABLE);

        constants.put("DY_EXP_STATE_READY", DY_EXP_STATE_READY);
        constants.put("DY_EXP_STATE_NOT_READY", DY_EXP_STATE_READY);
        return constants;
    }

    private void sendEvent(ReactContext reactContext, String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
// Basic Methods
    @ReactMethod
    public void enableDeveloperLogs(Boolean enable) {
        DYApi.getInstance().enableDeveloperLogs(enable);
    }

    @ReactMethod
    public void setSecretKey(String secretKey, Boolean isEUServer) {
        if (isEUServer) {
            DYCustomSettings settings = new DYCustomSettings();
            settings.useEuropeanServer(true);
            DYApi.setCustomSettings(settings);
        }
        DYApi.setContextAndSecret(application.getApplicationContext(), secretKey);
        DYApi.getInstance().setListener(this);
    }
// User Data
    @ReactMethod
    public void getUserAffinityProfile(Boolean normalizedScores) {
        JSONObject userAffinityScore = DYApi.getInstance().getUserAffinityProfile(normalizedScores);
        if (userAffinityScore != null) {
            WritableMap params = Arguments.createMap();
            params.putMap("affinity", RNUtil.jsonToWritableMap(userAffinityScore));
            sendEvent(reactContext, DY_EVENT_USER_AFFINITY_SCORE, params);
        }
    }

    @ReactMethod
    public void identifyUser(String uid) {
        DYUserData userData = new DYUserData();
        userData.setExternalUserID(uid);
        DYApi.getInstance().identifyUser(userData);
    }

    @ReactMethod
    public void consentOptIn() {
        DYApi.getInstance().consentOptIn();
    }

    @ReactMethod
    public void consentOptOut() {
        DYApi.getInstance().consentOptOut();
    }

// Analytics
    @ReactMethod
    public void trackEvent(String name, ReadableMap props) {
        DYApi.getInstance().trackEvent(name, RNUtil.readableMapToJson(props));
    }

    @ReactMethod
    public void pageView(String uniqueId, int contextType, String lang, ReadableArray data) {
        DYPageContext context = new DYPageContext(lang, contextType, RNUtil.convertArrayToJson(data));
        DYApi.getInstance().trackPageView(uniqueId, context);
    }

// Product Recommendations
    @ReactMethod
    public void sendRecommendationRequest(String widgetID, int contextType, String lang, ReadableArray data) {
        DYPageContext context = new DYPageContext(lang, contextType, RNUtil.convertArrayToJson(data));
        DYApi.getInstance().sendRecommendationsRequest(widgetID, context, false, this);
    }

    @ReactMethod
    public void trackRecomItemsRealImpression(String widgetID, ReadableArray itemIDs) {
        DYApi.getInstance().trackRecomItemRealImpression(widgetID, RNUtil.toArrayOfString(itemIDs));
    }

    @ReactMethod
    public void trackRecomItemClick(String widgetID, String itemID) {
        DYApi.getInstance().trackRecomItemClick(widgetID, itemID);
    }

// Variable Sets
    @ReactMethod
    public void getDynamicVariable(String varName, String defaultValue) {
        String smartVar = (String) DYApi.getInstance().getSmartVariable(varName, defaultValue);

        WritableMap params = Arguments.createMap();
        params.putString("value", smartVar);
        sendEvent(reactContext, DY_EVENT_DYNAMIC_VARIABLE, params);
    } 
}
