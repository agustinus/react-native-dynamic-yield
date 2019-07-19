
//
//  RNDynamicYield.h
//  RNDynamicYield
//
//  Created by Agustinus Kurniawan on 12/7/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#if __has_include("RCTEventEmitter.h")
#import "RCTEventEmitter.h"
#else
#import <React/RCTEventEmitter.h>
#endif

#import "DYApi.h"

@interface RNDynamicYield : RCTEventEmitter <RCTBridgeModule, DYDelegateProtocol>

@end
