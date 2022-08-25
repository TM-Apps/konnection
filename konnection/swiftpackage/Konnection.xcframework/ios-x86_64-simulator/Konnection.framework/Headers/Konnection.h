#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class KonnectionNetworkConnection, KonnectionIpInfo, KonnectionIpInfoMobileIpInfo, KonnectionIpInfoWifiIpInfo, KonnectionKotlinEnumCompanion, KonnectionKotlinEnum<E>, KonnectionKotlinArray<T>, KonnectionIPv6TestIpResolverCompanion, KonnectionMyExternalIpResolverCompanion, KonnectionKotlinThrowable, KonnectionKotlinException, KonnectionKotlinRuntimeException, KonnectionKotlinIllegalStateException;

@protocol KonnectionIpResolver, KonnectionKotlinx_coroutines_coreFlow, KonnectionKotlinComparable, KonnectionKotlinx_coroutines_coreFlowCollector, KonnectionKotlinIterator;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface KonnectionBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end;

@interface KonnectionBase (KonnectionBaseCopying) <NSCopying>
@end;

__attribute__((swift_name("KotlinMutableSet")))
@interface KonnectionMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end;

__attribute__((swift_name("KotlinMutableDictionary")))
@interface KonnectionMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end;

@interface NSError (NSErrorKonnectionKotlinException)
@property (readonly) id _Nullable kotlinException;
@end;

__attribute__((swift_name("KotlinNumber")))
@interface KonnectionNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end;

__attribute__((swift_name("KotlinByte")))
@interface KonnectionByte : KonnectionNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end;

__attribute__((swift_name("KotlinUByte")))
@interface KonnectionUByte : KonnectionNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end;

__attribute__((swift_name("KotlinShort")))
@interface KonnectionShort : KonnectionNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end;

__attribute__((swift_name("KotlinUShort")))
@interface KonnectionUShort : KonnectionNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end;

__attribute__((swift_name("KotlinInt")))
@interface KonnectionInt : KonnectionNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end;

__attribute__((swift_name("KotlinUInt")))
@interface KonnectionUInt : KonnectionNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end;

__attribute__((swift_name("KotlinLong")))
@interface KonnectionLong : KonnectionNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end;

__attribute__((swift_name("KotlinULong")))
@interface KonnectionULong : KonnectionNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end;

__attribute__((swift_name("KotlinFloat")))
@interface KonnectionFloat : KonnectionNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end;

__attribute__((swift_name("KotlinDouble")))
@interface KonnectionDouble : KonnectionNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end;

__attribute__((swift_name("KotlinBoolean")))
@interface KonnectionBoolean : KonnectionNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end;

__attribute__((swift_name("IpInfo")))
@interface KonnectionIpInfo : KonnectionBase
@property (readonly) KonnectionNetworkConnection *connection __attribute__((swift_name("connection")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IpInfo.MobileIpInfo")))
@interface KonnectionIpInfoMobileIpInfo : KonnectionIpInfo
- (instancetype)initWithHostIpv4:(NSString * _Nullable)hostIpv4 externalIpV4:(NSString * _Nullable)externalIpV4 __attribute__((swift_name("init(hostIpv4:externalIpV4:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()"))) __attribute__((deprecated("use corresponding property instead")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()"))) __attribute__((deprecated("use corresponding property instead")));
- (KonnectionIpInfoMobileIpInfo *)doCopyHostIpv4:(NSString * _Nullable)hostIpv4 externalIpV4:(NSString * _Nullable)externalIpV4 __attribute__((swift_name("doCopy(hostIpv4:externalIpV4:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable externalIpV4 __attribute__((swift_name("externalIpV4")));
@property (readonly) NSString * _Nullable hostIpv4 __attribute__((swift_name("hostIpv4")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IpInfo.WifiIpInfo")))
@interface KonnectionIpInfoWifiIpInfo : KonnectionIpInfo
- (instancetype)initWithIpv4:(NSString * _Nullable)ipv4 ipv6:(NSString * _Nullable)ipv6 __attribute__((swift_name("init(ipv4:ipv6:)"))) __attribute__((objc_designated_initializer));
- (NSString * _Nullable)component1 __attribute__((swift_name("component1()"))) __attribute__((deprecated("use corresponding property instead")));
- (NSString * _Nullable)component2 __attribute__((swift_name("component2()"))) __attribute__((deprecated("use corresponding property instead")));
- (KonnectionIpInfoWifiIpInfo *)doCopyIpv4:(NSString * _Nullable)ipv4 ipv6:(NSString * _Nullable)ipv6 __attribute__((swift_name("doCopy(ipv4:ipv6:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable ipv4 __attribute__((swift_name("ipv4")));
@property (readonly) NSString * _Nullable ipv6 __attribute__((swift_name("ipv6")));
@end;

__attribute__((swift_name("IpResolver")))
@protocol KonnectionIpResolver
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getWithCompletionHandler:(void (^)(NSString * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("get(completionHandler:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Konnection")))
@interface KonnectionKonnection : KonnectionBase
- (instancetype)initWithEnableDebugLog:(BOOL)enableDebugLog ipResolvers:(NSArray<id<KonnectionIpResolver>> *)ipResolvers __attribute__((swift_name("init(enableDebugLog:ipResolvers:)"))) __attribute__((objc_designated_initializer));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getCurrentIpInfoWithCompletionHandler:(void (^)(KonnectionIpInfo * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getCurrentIpInfo(completionHandler:)")));
- (KonnectionNetworkConnection * _Nullable)getCurrentNetworkConnection __attribute__((swift_name("getCurrentNetworkConnection()")));
- (BOOL)isConnected __attribute__((swift_name("isConnected()")));
- (id<KonnectionKotlinx_coroutines_coreFlow>)observeHasConnection __attribute__((swift_name("observeHasConnection()")));
- (id<KonnectionKotlinx_coroutines_coreFlow>)observeNetworkConnection __attribute__((swift_name("observeNetworkConnection()")));
- (void)stop __attribute__((swift_name("stop()")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KonnectionWrapper")))
@interface KonnectionKonnectionWrapper : KonnectionBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getCurrentIpInfoWithCompletionHandler:(void (^)(KonnectionIpInfo * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("getCurrentIpInfo(completionHandler:)")));
- (BOOL)hasNetworkConnection __attribute__((swift_name("hasNetworkConnection()")));
- (void)networkConnectionObservationCallback:(void (^)(KonnectionNetworkConnection * _Nullable))callback __attribute__((swift_name("networkConnectionObservation(callback:)")));
- (void)stop __attribute__((swift_name("stop()")));
@end;

__attribute__((swift_name("KotlinComparable")))
@protocol KonnectionKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end;

__attribute__((swift_name("KotlinEnum")))
@interface KonnectionKotlinEnum<E> : KonnectionBase <KonnectionKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) KonnectionKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NetworkConnection")))
@interface KonnectionNetworkConnection : KonnectionKotlinEnum<KonnectionNetworkConnection *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) KonnectionNetworkConnection *wifi __attribute__((swift_name("wifi")));
@property (class, readonly) KonnectionNetworkConnection *mobile __attribute__((swift_name("mobile")));
+ (KonnectionKotlinArray<KonnectionNetworkConnection *> *)values __attribute__((swift_name("values()")));
@end;

__attribute__((swift_name("BaseTriggerEvent")))
@interface KonnectionBaseTriggerEvent : KonnectionBase
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
@end;

__attribute__((swift_name("IfaddrsInteractor")))
@protocol KonnectionIfaddrsInteractor
@required
- (NSString * _Nullable)getNetInterface:(NSString *)netInterface saFamily:(int32_t)saFamily __attribute__((swift_name("get(netInterface:saFamily:)")));
@end;

__attribute__((swift_name("ReachabilityInteractor")))
@protocol KonnectionReachabilityInteractor
@required
- (KonnectionUInt * _Nullable)getReachabilityFlagsReachabilityRef:(void *)reachabilityRef __attribute__((swift_name("getReachabilityFlags(reachabilityRef:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IPv6TestIpResolver")))
@interface KonnectionIPv6TestIpResolver : KonnectionBase <KonnectionIpResolver>
- (instancetype)initWithEnableDebugLog:(BOOL)enableDebugLog __attribute__((swift_name("init(enableDebugLog:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) KonnectionIPv6TestIpResolverCompanion *companion __attribute__((swift_name("companion")));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getWithCompletionHandler:(void (^)(NSString * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("get(completionHandler:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("IPv6TestIpResolver.Companion")))
@interface KonnectionIPv6TestIpResolverCompanion : KonnectionBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) KonnectionIPv6TestIpResolverCompanion *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MyExternalIpResolver")))
@interface KonnectionMyExternalIpResolver : KonnectionBase <KonnectionIpResolver>
- (instancetype)initWithEnableDebugLog:(BOOL)enableDebugLog __attribute__((swift_name("init(enableDebugLog:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) KonnectionMyExternalIpResolverCompanion *companion __attribute__((swift_name("companion")));

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)getWithCompletionHandler:(void (^)(NSString * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("get(completionHandler:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MyExternalIpResolver.Companion")))
@interface KonnectionMyExternalIpResolverCompanion : KonnectionBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) KonnectionMyExternalIpResolverCompanion *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("UtilsKt")))
@interface KonnectionUtilsKt : KonnectionBase
+ (NSString * _Nullable)getUrlContentUrl:(NSString *)url __attribute__((swift_name("getUrlContent(url:)")));
+ (void)logErrorTag:(NSString *)tag message:(NSString *)message error:(KonnectionKotlinThrowable *)error __attribute__((swift_name("logError(tag:message:error:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SyncKt")))
@interface KonnectionSyncKt : KonnectionBase
+ (void)syncedLock:(id)lock closure:(void (^)(void))closure __attribute__((swift_name("synced(lock:closure:)")));
@end;

__attribute__((swift_name("KotlinThrowable")))
@interface KonnectionKotlinThrowable : KonnectionBase
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (KonnectionKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) KonnectionKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end;

__attribute__((swift_name("KotlinException")))
@interface KonnectionKotlinException : KonnectionKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinRuntimeException")))
@interface KonnectionKotlinRuntimeException : KonnectionKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinIllegalStateException")))
@interface KonnectionKotlinIllegalStateException : KonnectionKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("KotlinCancellationException")))
@interface KonnectionKotlinCancellationException : KonnectionKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(KonnectionKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol KonnectionKotlinx_coroutines_coreFlow
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<KonnectionKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface KonnectionKotlinEnumCompanion : KonnectionBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) KonnectionKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end;

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface KonnectionKotlinArray<T> : KonnectionBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(KonnectionInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<KonnectionKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end;

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol KonnectionKotlinx_coroutines_coreFlowCollector
@required

/**
 @note This method converts instances of CancellationException to errors.
 Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end;

__attribute__((swift_name("KotlinIterator")))
@protocol KonnectionKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end;

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
