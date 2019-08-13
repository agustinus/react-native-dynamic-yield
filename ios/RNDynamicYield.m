//
//  RNDynamicYield.m
//  RNDynamicYield
//
//  Created by Agustinus Kurniawan on 12/7/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RNDynamicYield.h"
#import "RCTConvert+DYPageType.h"


// Notification/Event Names
NSString *const kExperimentsReady = @"RNDynamicYield/experimentsReady";
NSString *const kRecommendation = @"RNDynamicYield/recommendation";
NSString *const kUserAffinityScore = @"RNDynamicYield/userAffinityScore";
NSString *const kDynamicVariable = @"RNDynamicYield/dynamicVariable";

NSString *const kExpStateReady = @"READY";
NSString *const kExpStateNotReady = @"NOT_READY";

@implementation RNDynamicYield

- (instancetype)init {
    self = [super init];
    if (self) {
    }
    return self;
}

- (void) experimentsReadyWithState: (ExperimentsState) state {
    if (state == DY_READY_NO_UPDATE_NEEDED || state == DY_READY_AND_UPDATED) {
        [self sendEventWithName:kExperimentsReady body:@{@"state": kExpStateReady}];
    } else {
        [self sendEventWithName:kExperimentsReady body:@{@"state": kExpStateNotReady}];
    }
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSDictionary *)constantsToExport
{
    return @{ @"DY_TYPE_HOMEPAGE" : @(DY_TYPE_HOMEPAGE),
              @"DY_TYPE_CATEGORY" : @(DY_TYPE_CATEGORY),
              @"DY_TYPE_PRODUCT" : @(DY_TYPE_PRODUCT),
              @"DY_TYPE_CART" : @(DY_TYPE_CART),
              @"DY_TYPE_POST" : @(DY_TYPE_POST),
              @"DY_TYPE_EMAIL" : @(DY_TYPE_EMAIL),
              @"DY_TYPE_SEARCH" : @(DY_TYPE_SEARCH),
              @"DY_TYPE_OTHER" : @(DY_TYPE_OTHER),
              
              @"DY_EVENT_EXPERIMENTS_READY": kExperimentsReady,
              @"DY_EVENT_RECOMMENDATION": kRecommendation,
              @"DY_EVENT_USER_AFFINITY_SCORE": kUserAffinityScore,
              @"DY_EVENT_DYNAMIC_VARIABLE": kDynamicVariable,
              
              @"DY_EXP_STATE_READY": kExpStateReady,
              @"DY_EXP_STATE_NOT_READY": kExpStateNotReady,
              };
}

RCT_EXPORT_MODULE()

- (NSArray<NSString *> *)supportedEvents
{
    return @[kExperimentsReady, kRecommendation, kUserAffinityScore, kDynamicVariable];
}

//Basic Methods
RCT_EXPORT_METHOD(enableDeveloperLogs:(BOOL)enable)
{
    [[DYApi getInstance] enableDeveloperLogs:enable];
}

RCT_EXPORT_METHOD(setSecretKey:(NSString *)secretKey isUsingEUServer:(BOOL)euServer)
{
    if (euServer) {
        [[DYApi getInstance] setUseEuropeanServer:euServer];
    }
    [[DYApi getInstance] setSecretKey:secretKey];
    [[DYApi getInstance] setDelegate:self];
}

//User Data
RCT_EXPORT_METHOD(getUserAffinityProfile:(BOOL)normalizedScores)
{
    NSDictionary* userAffinityScore = [[DYApi getInstance] getUserAffinityProfile:normalizedScores];
    if (userAffinityScore == nil) {
        [self sendEventWithName:kUserAffinityScore body:@{@"affinity": userAffinityScore}];
    }
}

RCT_EXPORT_METHOD(identifyUser:(NSString *)uid)
{
    DYUserData* user = [DYUserData create];
    [user setExternalUserID:uid];
    [[DYApi getInstance] identifyUser:user];
}

RCT_EXPORT_METHOD(consentOptIn)
{
    [[DYApi getInstance] consentOptin];
}

RCT_EXPORT_METHOD(consentOptOut)
{
    [[DYApi getInstance] consentOptout];
}

//Analytics
RCT_EXPORT_METHOD(trackEvent:(NSString *)name prop:(NSDictionary *)props)
{
    [[DYApi getInstance] trackEvent:name prop:props];
}

RCT_EXPORT_METHOD(pageView:(NSString *)uniqueId type:(contextType)contextType language:(NSString *)lang data:(NSArray * _Nullable)data)
{
    DYPageContext* context = [[DYPageContext alloc] init];
    [context setLanguage:lang];
    [context setType:contextType];
    if (data) {
        [context setData:data];
    }
    
    [[DYApi getInstance] pageView:uniqueId context:context];
}

//Product Recommendations
RCT_EXPORT_METHOD(sendRecommendationRequest:(NSString *)widgetID type:(contextType)contextType language:(NSString *)lang data:(NSArray * _Nullable)data)
{
    DYPageContext* context = [[DYPageContext alloc] init];
    [context setLanguage:lang];
    [context setType:contextType];
    if (data) {
        [context setData:data];
    }
    
    [[DYApi getInstance] sendRecommendationRequest:widgetID withContext:context completionHandler:^(NSArray * _Nullable recommendations, NSString * _Nonnull widgetID) {
        if (recommendations != nil) {
            [self sendEventWithName:kRecommendation body:@{@"recommendation": recommendations, @"widgetID": widgetID}];
        }
    }];
}

RCT_EXPORT_METHOD(trackRecomItemsRealImpression:(NSString *)widgetID itemIDs:(NSArray *)itemIDs)
{
    [[DYApi getInstance] TrackRecomItemsRealImpression:widgetID andItemID:itemIDs];
}

RCT_EXPORT_METHOD(trackRecomItemClick:(NSString *)widgetID itemID:(NSString *)itemID)
{
    [[DYApi getInstance] TrackRecomItemClick:widgetID andItemID:itemID];
}

//Variable Sets
RCT_EXPORT_METHOD(getDynamicVariable:(NSString *)varName defaultValue:(NSString *)defaultValue)
{
    NSString* smartVar = [[DYApi getInstance] getSmartVariable:varName defaultValue:defaultValue];
    [self sendEventWithName:kDynamicVariable body:@{@"value": smartVar}];

}

@end


