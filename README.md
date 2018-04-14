OpenCamera
============
This is a simple android app to monitor one or more ipcameras (supporting Onvif).
The app required dropbox authorization, and will use that as a storage and source to sync between devices.
You will be able to add/delete cameras, view camera on streaming, capture screenshots and videos (uploading them on dropbox), and also media viewing.

[![screen](https://github.com/eltonkola/opencamera/blob/master/screenshots/device-2018-04-13-211131.png?raw=true)](https://github.com/eltonkola/opencamera)


This is a weekend project, for <a href="https://onvif-challenge.bemyapp.com">https://onvif-challenge.bemyapp.com</a>

ON apks folder you can download the precompiled binaries for your platform

TODO
-------
- encrypt data on configuration file (json file saved on your dropbox account)
- replace video player with a ffmpg based video player to reduce app size
- ?? more features ??

License
-------
    Copyright 2015-2017 Elton Kola

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


Attribution
-----------
* Uses [AppCompat-v7/Design/Constraint-layout/Recyclerview/Cardview](http://developer.android.com/tools/support-library/features.html#v7-appcompat) licensed under [Apache 2.0][Apache 2.0]
* Uses [Material Design icons][Design Icons] licensed under [Apache 2.0][Apache 2.0]

* Uses [RX Java2 / RxAndroid] (https://github.com/ReactiveX/RxAndroid) licensed under [Apache 2.0][Apache 2.0]
* Uses [Rx Permissions2] (https://github.com/tbruyelle/RxPermissions) licensed under [Apache 2.0][Apache 2.0]
* Uses [Gson] (https://github.com/google/gson) licensed under [Apache 2.0][Apache 2.0]
* Uses [Swipelayout] (https://github.com/daimajia/AndroidSwipeLayout) licensed under [Apache 2.0][Apache 2.0]
* Uses [Picasso] (http://square.github.io/picasso/) licensed under [Apache 2.0][Apache 2.0]
* Uses [Okhttp3] (https://github.com/square/okhttp) licensed under [Apache 2.0][Apache 2.0]
* Uses [OnvifCamera] (https://github.com/rvi/ONVIFCameraAndroid)  licensed under [Apache 2.0][Apache 2.0]
* Uses [Arkitekt] (https://github.com/eltonkola/arkitekt) licensed under [Apache 2.0][Apache 2.0]
* Uses [VlcLib] (https://github.com/pedroSG94/vlc-example-streamplayer) licensed under [Apache 2.0][Apache 2.0]
* Uses [Dropbox Core Sdk] (https://github.com/dropbox/dropbox-sdk-java) licensed under [Apache 2.0][Apache 2.0]
* Uses [Android Ffmpeg] (https://github.com/bravobit/FFmpeg-Android) licensed under [Apache 2.0][Apache 2.0]
* Uses [Awesomebar] (https://github.com/florent37/AwesomeBar) licensed under [Apache 2.0][Apache 2.0]

 [Apache 2.0]: http://www.apache.org/licenses/LICENSE-2.0
 [Design Icons]: https://github.com/google/material-design-icons
 