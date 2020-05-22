# HireNow
A ridesharing android application.

<img src = "https://user-images.githubusercontent.com/52358417/82532070-5f86ee00-9b62-11ea-81a2-9499b46b8b03.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82532079-631a7500-9b62-11ea-814a-5e2e0be33d78.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82532082-644ba200-9b62-11ea-9d32-158adcd84703.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82532085-657ccf00-9b62-11ea-8ebf-b75bc2d7078c.jpg" width ="200" />

<img src = "https://user-images.githubusercontent.com/52358417/82533332-dd4bf900-9b64-11ea-9439-91002ea4be74.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82533335-df15bc80-9b64-11ea-8e04-078acf1659fb.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82533338-e0df8000-9b64-11ea-8b77-1be4d31da96e.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/82533339-e210ad00-9b64-11ea-8a68-7c3ee98800a9.jpg" width ="200" />


## Project Details:
A rider can request for rideshare using the application. In the app, the rider selects the drop-off location using the place picker or "Where to go?" search box. Then reviewing the price and drop-off time rider confirms the pickup.

Drivers can see and choose to accept the rider’s ride request in the "Available Pickup Request(s)" window. A rider gets an automatic cellular call when a driver accepts his/her ride request.

## How to use
Import the project in Android studio. Connect your app with the Firebase using the following method in Android studio.
     
     Tools>Firebase>Authentication>Connect>Sync
Get your Google Maps API Key and replace it with the similar one in AndroidManifest.xml & google_maps_api.xml.

## Current Issues
    1. Place Auto Complete had stopped working due to the use of the deprecated Google Play Services version of 
       the Places SDK.
    2. Distance Matrix API & Directions API are not working properly.

## Contribution 
Contribution to the project will be warmly welcomed.


## Credits
Place Auto Complete : https://github.com/googlearchive/android-play-places/tree/master/PlaceCompleteAdapter

Distance Matrix API : https://github.com/vastavch/GoogleMapsDistanceMatrixAPI_Demo 

Directions API : https://github.com/Vysh01/android-maps-directions 
