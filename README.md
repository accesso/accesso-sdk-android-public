# accesso SDK for Android 
#### Welcome to the accesso Software Development Kit for Android
##### https://accessomobilesdk.com 
#
[![Tests](https://github.com/accesso/accesso-sdk-android/actions/workflows/test_all_modules.yml/badge.svg)](https://github.com/accesso/accesso-sdk-android/actions/workflows/test_all_modules.yml)
[![Release](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml/badge.svg?branch=main)](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml)
[![Documentation](https://github.com/accesso/accesso-sdk-android/actions/workflows/documentation_deploy.yml/badge.svg)](https://github.com/accesso/accesso-sdk-android/actions/workflows/documentation_deploy.yml)

The accesso SDK for Android gives modular access to the full suite of products the accesso team offers. Easily place products and features, ala carte, into your own 3rd party Android mobile application. 

## Whats Included?
- AccessoExperiencePromoter: The Experience Promoter Messaging feature.
- AccessoEntitlements: The Entitlements feature for ticketing.
- AccessoQueueing: The advanced line management system enhancing guest experiences.
- AccessoCore: The core component of the SDK, ensuring code consistency, promoting reuse, and standardizing approaches across all modules.

## Installation

**The Accesso SDK for Android requires Android 12+.**

## Adding the SDK as a Dependency

To add the SDK as a dependency, modify your `build.gradle` file with the following configurations:

### Specify the Repository

```gradle
repositories {
    maven {
        url 'https://maven.pkg.github.com/accesso/accesso-sdk-android-public'
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME_GITHUB")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN_GITHUB")
        }
    }
}
```

### Add the Dependency

```
dependencies {
    implementation 'com.accesso:accessocore:1.0.0'
    implementation 'com.accesso:accessoexperiencepromoter:1.0.0'
}
```

## License
2023 © accesso™
**All Rights Reserved**
