package com.accesso.sdk_android_experience_promoter_sample.navigation

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
interface Destination {
    val route: String
    val title: String
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object MessageListDestination: Destination {
    override val route: String
        get() = "messageList"
    override val title: String
        get() = "Message Inbox"
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object MessageDetailsDestination: Destination {
    override val route: String
        get() = "messageDetails"
    override val title: String
        get() = "Message Details"
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object LandingPageDestination: Destination {
    override val route: String
        get() = "landingPage"
    override val title: String
        get() = "Experience Promoter"
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object BeaconListDestination: Destination {
    override val route: String
        get() = "beacons"
    override val title: String
        get() = "Beacons"
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object PointOfInterestListDestination: Destination {
    override val route: String
        get() = "pointOfInterestList"
    override val title: String
        get() = "Points of Interest"
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object PointOfInterestDetailsDestination: Destination {
    override val route: String
        get() = "pointOfInterestDetails"
    override val title: String
        get() = "Point of Interest"
}