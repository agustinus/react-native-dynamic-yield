
//
//  RNDynamicYield.h
//  RNDynamicYield
//
//  Created by Agustinus Kurniawan on 12/7/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

#import "DYSDK/DYApi.h"

@interface RNDynamicYield : RCTEventEmitter <RCTBridgeModule, DYDelegateProtocol>

@end
