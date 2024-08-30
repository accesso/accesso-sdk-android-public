package com.accesso.queueingsample

interface Destination {
    val route: String
    val title: String
}

object AttractionListDestination: Destination {
    override val route: String
        get() = "attractionList"
    override val title: String
        get() = "Attractions"
}

object AttractionDetailsDestination: Destination {
    override val route: String
        get() = "attractionDetails"
    override val title: String
        get() = "Attraction Details"
}

object LandingPageDestination: Destination {
    override val route: String
        get() = "landingPage"
    override val title: String
        get() = "Queueing"
}

object ReserveActionDestination: Destination {
    override val route: String
        get() = "ReserveActionPage"
    override val title: String
        get() = "Reserve Action"
}

object SingInDestination: Destination {
    override val route: String
        get() = "SingInPage"
    override val title: String
        get() = "SingIn"
}
