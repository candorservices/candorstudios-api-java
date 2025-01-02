# Candor Studios API - Java
Java Library for Candor Studios' Freelancer API. Please see https://candorstudios.notion.site/Freelancer-API-Docs-14c9e5d3cd0f809b9aa7cc4f251c8d3e?pvs=74 for more documentation on the API and how it works.
## Getting Started
You can include the library in your project using Jitpack:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
Dependency:

<dependency>
    <groupId>com.github.candorservices</groupId>
    <artifactId>candor-java</artifactId>
    <version>1.0</version>
</dependency>
```
Then, initialize the `CandorApi` class using your API key found at https://candorstudios.net/app/freelancers/api-dashboard.
```java
CandorApi api = new CandorApi("API_KEY_HERE");
```
## Verifying a license
To verify a license key, first copy the relevant product ID from the dashboard. Then, you can use the following code. You should obtain the license key either via a config file or Candor's Remote configs. 
License key validation is signed by the API to protect against them being tampered with during transit, but this method will handle verifying that signature for you. If validation fails, you will either receive a false boolean in `onCompletion` or you'll receive an `onError`, so you should be prepared to handle both.
```java
api.verifyLicense("LICENSE_KEY_HERE", "PRODUCT_ID_HERE")
```
You can either handle the response asynchronously using `onCompletion` or `onError`, or you can handle it synchronously using `awaitCompleted`.
## Retrieving Config Values
Similar to license keys, you'll first need to copy the ID of the relevant config. Then, use the following:
```java
api.retrieveConfig("CONFIG_ID")
```
Again, you can either handle this asynchronously or synchronously.
## Retrieving Your Reviews
Finally, you can retrieve your reviews from the API using:
```java
api.retrieveReviews("YOUR_USER_ID_HERE")
```

# Need support?
If you have any questions about how to use this library, please email `contact@candorstudios.net` or create a support ticket in Discord.
