//
//  ViewController.m
//  SuperID-RN
//
//  Created by YourtionGuo on 6/29/16.
//  Copyright © 2016 Yourtion. All rights reserved.
//

#import "ViewController.h"
#import <React/RCTRootView.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    NSURL *jsCodeLocation = [NSURL URLWithString:@"http://192.168.31.186:8081/index.ios.bundle?platform=ios"];
//    NSURL *jsCodeLocation = [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
    RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
                                                        moduleName: @"SimpleApp"
                                                 initialProperties:nil
                                                     launchOptions:nil];
    [self.view addSubview:rootView];
    rootView.frame = self.view.bounds;

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
