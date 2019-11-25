//
//  RCTConvert+DYPageType.h
//  RNDynamicYield
//
//  Created by Agustinus Kurniawan on 12/7/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "RCTConvert.h"
#import "DYSDK/DYPageContext.h"

NS_ASSUME_NONNULL_BEGIN

@interface RCTConvert (contextType)

@end

NS_ASSUME_NONNULL_END

@implementation RCTConvert (contextType)
RCT_ENUM_CONVERTER(contextType, (@{ @"DY_TYPE_HOMEPAGE" : @(DY_TYPE_HOMEPAGE),
                                    @"DY_TYPE_CATEGORY" : @(DY_TYPE_CATEGORY),
                                    @"DY_TYPE_PRODUCT" : @(DY_TYPE_PRODUCT),
                                    @"DY_TYPE_CART" : @(DY_TYPE_CART),
                                    @"DY_TYPE_POST" : @(DY_TYPE_POST),
                                    @"DY_TYPE_EMAIL" : @(DY_TYPE_EMAIL),
                                    @"DY_TYPE_SEARCH" : @(DY_TYPE_SEARCH),
                                    @"DY_TYPE_OTHER" : @(DY_TYPE_OTHER)
                                    }),
                   DY_TYPE_HOMEPAGE, integerValue)
@end
