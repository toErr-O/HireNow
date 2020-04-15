# HireNow
A small ride sharing android app. It was developed as a course project for CSE 618 - Mobile Apps Development Lab.

<img src = "https://user-images.githubusercontent.com/52358417/79186028-5788af80-7e3a-11ea-9c24-e14db1f6e944.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186036-59eb0980-7e3a-11ea-9eed-e6e6f9d3c29a.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186037-5b1c3680-7e3a-11ea-966f-6cfd01e9e5c7.jpg" width ="200" />

<img src = "https://user-images.githubusercontent.com/52358417/79186039-5c4d6380-7e3a-11ea-9aa1-9c20460c1abd.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186045-5eafbd80-7e3a-11ea-9b3c-946f0be5858d.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186046-60798100-7e3a-11ea-8e2b-facbef1761ab.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186047-61aaae00-7e3a-11ea-99c8-25ae5ad0f995.jpg" width ="200" />

<img src = "https://user-images.githubusercontent.com/52358417/79186051-62dbdb00-7e3a-11ea-9a02-81813b9a2bb7.jpg" width ="200" /> <img src = "https://user-images.githubusercontent.com/52358417/79186052-63747180-7e3a-11ea-9eed-32d4416ed26d.jpg" width ="200" />

# Project Overview:
A Passenger can request for ride share. For requesting a ride share, a passenger can choose his destination place, either by searching place on the search bar or picking up place from the map. After picking up place, route from the passenger's current location to destination along with required time and fare details will be shown. Afterwards passenger can confirm the ride request.

Once a ride request is made, the request will go to the dashboard and will be placed with all the other ride requests. If anyone wants to share ride, he can look here for a suitable request to pick up. Once a driver picks up a request, an automatic call will be sent to the passenger's phone from the driver over cellular network and the request will be removed from the dashboard.

# Technical Details:
## Firebase:
    1. Authentication: Sign-in Method using Phone have been used for registration
    2. Database: Realtime Database is used for storing all the data related to user.

## Google Maps API:
    1. Places SDK for Android
    2. Directions API
    3. Distance Matrix API
    4. Maps SDK for Android
    5. Identity Toolkit API
    6. Token Service API

# Current Bugs
    1. Search bar is not working. Probably Places SDK have some issues.
    2. On the first Login after installing the app, current location can't be detected, it needs at least two login.
    3. Didn't manage to work Distance Matrix API for calculating the distance along a path from the source to destination.
    
# Future Work
    1. Fixing the existing Bugs.
    2. Adding some functionality.
    3. Making the projects a bit more real-world worth
This project was developed in a short period of time for submission purpose. Have plan of working on it in leisure.

# Credits
Have used the internet extensively while developing the project. So, similarities may be found.

# Contibution 
Any kind of contribution to the project will be warmly welcomed.
