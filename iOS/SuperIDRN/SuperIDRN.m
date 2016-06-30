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
    BOOL _isRuning;
    RCTPromiseResolveBlock _resolve;
    RCTPromiseRejectBlock _reject;
}

RCT_EXPORT_MODULE();

- (instancetype)init
{
    self = [super init];
    if (self) {
        _isRegisted = NO;
        _isRuning = NO;
    }
    return self;
}

-(UIViewController *)rootController {
    return UIApplication.sharedApplication.delegate.window.rootViewController;
}

-(BOOL)checkStatus:(RCTPromiseRejectBlock)reject {
    if (!_isRegisted) {
        NSError *err = [NSError errorWithDomain:@"SuperID" code:-1 userInfo:nil];
        reject(@"not register", @"Please RegisterApp First", err);
        return NO;
    }
    if (_isRuning) {
        NSError *err = [NSError errorWithDomain:@"SuperID" code:-2 userInfo:nil];
        reject(@"meathd is runing", @"Please wait", err);
        return NO;
    }
    return YES;
}

-(void)cleanRuning {
    _resolve = nil;
    _reject = nil;
    _isRuning = NO;
}

RCT_EXPORT_METHOD(debug:(BOOL)debug)
{
    [SuperID setDebugMode:debug];
}

RCT_REMAP_METHOD(version,
                 getVersionResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    resolve([SuperID getSDKVersion]);
}

RCT_EXPORT_METHOD(registe:(NSString *)appId secret:(NSString *)appSecret)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        [[SuperID sharedInstance] registerAppWithAppID:appId withAppSecret:appSecret];
        [SuperID sharedInstance].delegate = self;
    });
    _isRegisted = YES;
}

RCT_REMAP_METHOD(login,
                 obtainLoginViewResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    if (![self checkStatus:reject]) return;
    
    _isRuning = YES;
    
    NSError *error;
    id loginView = [[SuperID sharedInstance] obtainLoginViewControllerWithError:&error];
    if (error) {
        [self cleanRuning];
        return reject([NSString stringWithFormat:@"%ld", (long)error.code], error.description, error);
    }
    
    _resolve = resolve;
    _reject = reject;
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self rootController] presentViewController:loginView animated:YES completion:nil];
    });
    
}

RCT_REMAP_METHOD(verify,
                 obtainVerifyViewWithRetryCount:(nonnull NSNumber *)count resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    if (![self checkStatus:reject]) return;
    
    _isRuning = YES;
    
    NSError *error;
    id verifyView = [[SuperID sharedInstance] obtainFaceVerifyViewControllerWithRetryCount:count error:&error];
    if (error) {
        [self cleanRuning];
        return reject([NSString stringWithFormat:@"%ld", (long)error.code], error.description, error);
    }
    
    _resolve = resolve;
    _reject = reject;
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [[self rootController] presentViewController:verifyView animated:YES completion:nil];
    });
    
}

#pragma make - SuperID Delegate

/**
 *  应用用户使用SuperID登录应用操作完成调用的协议方法，开发者可通过继承该协议方法获取用户信息。
 *
 *  @param userInfo 当前一登用户的账户信息
 *  @param uid      与一登账号绑定的用户Uid，如三方开发者未更新Uid或绑定时传入，默认为随机Uid
 *  @param error    登录成功为nil，登录失败不为nil。开发者可根据该错误信息描述进行对应处理，详见开发者文档和Demo
 */
- (void)superID:(SuperID *)sender userDidFinishLoginWithUserInfo:(NSDictionary *)userInfo withOpenId:(NSString *)openId error:(NSError *)error {
    if (_isRuning && _resolve && _reject) {
        if (error) {
            _reject([NSString stringWithFormat:@"%ld", (long)error.code], error.description, error);
        } else {
            _resolve(@{@"openId": openId, @"userInfo": userInfo});
        }
        [self cleanRuning];
    }
}

/**
 *  应用用户取消当前应用账号与一登账号的授权关联成功协议方法
 *
 *  @param error  解除授权成功为nil，解除授权失败不为nil。
 */
- (void)superID:(SuperID *)sender userDidFinishCancelAuthorization:(NSError *)error {
    
}

/**
 *  用户在授权状态下使用一登人脸信息获取成功的协议方法
 *
 *  @param featureInfo 用户的人脸信息内容
 *  @param error       获取成功为nil， 获取不成功不为nil。错误信息通知，开发者可根据错误描述判断错误情况。详见Demo或开发者文档
 */
- (void)superID:(SuperID *)sender userDidFinishGetFaceFeatureWithFeatureInfo:(NSDictionary *) featureInfo error:(NSError *)error {
    
}

/**
 *  查询用户与一登账号授权关联状态的协议方法
 *
 *  @param state  SIDUserAuthorizationState的类型参数，用于状态进行定位
 */
- (void)superID:(SuperID *)sender queryCurrentUserAuthorizationStateResponse:(SIDUserAuthorizationState)state {
    
}

/**
 *  更新用户账户信息到一登账号的协议方法
 *  具体状态的处理可参考Demo
 *  @param state SIDUserUpdateResponseState类型参数，用户状态定位
 */
- (void)superID:(SuperID *)sender updateAppUserInfoStateResponse:(SIDUserUpdateResponseState)state {
    
}

/**
 *  用户验证结果回调
 *
 *  @param state  SIDFACEVerifyState类型参数，用于成功或失败的判断
 */
- (void)superID:(SuperID *)sender faceVerifyResponse:(SIDFACEVerifyState)state {
    if (_isRuning && _resolve && _reject) {
        if (state == SIDFaceVerifySucceed) {
            _resolve(@0);
        } else {
            _resolve(@1);
        }
        [self cleanRuning];
    }
}


@end
