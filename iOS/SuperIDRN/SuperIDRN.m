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

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addEvent:(NSString *)name location:(NSString *)location)
{
    RCTLogInfo(@"Pretending to create an event %@ at %@", name, location);
}

@end
