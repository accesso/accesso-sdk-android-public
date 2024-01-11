# accesso SDK for Android 
#### Welcome to the accesso Software Development Kit for Android
##### https://accessomobilesdk.com 
#

The accesso SDK for Android gives modular access to the full suite of products the accesso team has to offer. Easily place products and features, ala carte, into your own 3rd party Android mobile application. 

## Whats Included?
- AccessoExperiencePromoter: The Experience Promoter Messaging feature.
- AccessoEntitlements: The Entitlements feature for ticketing

## Contribution / Developer Notes
- If you'd like to contribute to the project, please view our [CONTRIBUTION.md](CONTRIBUTION.md) guide.


## Installation

**The Accesso SDK for Android requires Android 12+.**

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
