//
//  SuperIDRN.m
//  SuperID-RN
//
//  Created by YourtionGuo on 6/29/16.
//  Copyright © 2016 Yourtion. All rights reserved.
//

#import "SuperIDRN.h"
#import "RCTLog.h"
#import "RCTConvert.h"


@implementation SuperIDRN
{
    BOOL _isRegisted;
}

RCT_EXPORT_MODULE();

- (instancetype)init
{
    self = [super init];
    if (self) {
        _isRegisted = NO;
        [SuperID sharedInstance].delegate = self;
    }
    return self;
}

RCT_EXPORT_METHOD(addEvent:(NSString *)name location:(NSString *)location)
{
    RCTLogInfo(@"Pretending to create an event %@ at %@", name, location);
}

RCT_EXPORT_METHOD(setDebugMode:(BOOL)debug)
{
    [SuperID setDebugMode:debug];
}

RCT_REMAP_METHOD(getVersion,
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([SuperID getSDKVersion]);
}

RCT_EXPORT_METHOD(registerApp:(NSString *)appId location:(NSString *)appSecret)
{
    [[SuperID sharedInstance] registerAppWithAppID:appId withAppSecret:appSecret];
    _isRegisted = YES;
}

@end
