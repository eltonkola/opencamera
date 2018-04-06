package com.aldroid.opencamera.dropbox

import com.aldroid.coinhivesdk.model.IpCamera
import com.aldroid.coinhivesdk.model.IpCameraData
import com.aldroid.coinhivesdk.model.IpCameraInfo
import com.aldroid.opencamera.MainApp
import com.aldroid.opencamera.util.Utils
import com.rvirin.onvif.onvifcamera.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory

class RichCameraListManager (val cameraListManager : CameraListManager) {

    val cameraLits: BehaviorSubject<List<IpCameraData>> = BehaviorSubject.createDefault(arrayListOf())

    val cameraCache = HashMap<IpCamera, IpCameraData>()

    init {

        cameraListManager.cameraLits.subscribe({cameraList : List<IpCamera> ->

            if (cameraList.size == 0) {
                //no data, do nothing
            } else {
                //send the list first, and load hte data next
                sendUpdate(cameraList)
            }

            //load all data for each camera
            val cameraCalls = cameraList.map { camera ->
                loadCameraData(camera)
            }

            Utils.log("nr of calls to load: ${cameraCalls.size}")

            Observable.concat(cameraCalls.map { it.toObservable() })
                    //.toList()
                    .subscribe({ cameraData ->
                        Utils.log("final data we got: ${cameraData}")

                        cameraCache.put(cameraData.ipCamera, cameraData)

                        sendUpdate(cameraList)
                    }, {
                        Utils.log("error loading camera data: ${it.message}")
                    })

        }, {
            Utils.log("error loading camera list from dropbox: ${it.message}")
        })

    }

    private fun sendUpdate(list : List<IpCamera>){
        cameraLits.onNext(list.map {
            val cachedValue = cameraCache.get(it)
            if (cachedValue != null) {
                cachedValue
            } else {
                val data = IpCameraData(it)
                cameraCache.put(it, data)
                data
            }
        })
    }


    fun loadCameraData(camera: IpCamera): Single<IpCameraData> {
        return Single.create<IpCameraData> { emmiter ->
            currentDevice = OnvifDevice(camera.ip, camera.username, camera.password)
            currentDevice.listener = object : OnvifListener {
                val cameraData = IpCameraData(camera, online = false)
                override fun requestPerformed(response: OnvifResponse) {
                    if (!response.success) {
                        Utils.log("camera error")
                        emmiter.onSuccess(cameraData)
                    } else if (response.request.type == OnvifRequest.Type.GetServices) {
                        Utils.log("camera get service")
                        currentDevice.getDeviceInformation()
                    } else if (response.request.type == OnvifRequest.Type.GetDeviceInformation) {
                        Utils.log("camera information")
                        cameraData.info = readDeviceInfo(response.result!!)
                        Utils.log("Info: " + response.result)
                        Utils.log("Info parsed: " + cameraData.info.toString())
                        currentDevice.getProfiles()
                    } else if (response.request.type == OnvifRequest.Type.GetProfiles) {
                        Utils.log("camera profiles")
                        currentDevice.getStreamURI()
                    } else if (response.request.type == OnvifRequest.Type.GetStreamURI) {
                        Utils.log("camera stream url")
                        currentDevice.rtspURI?.let { uri ->
                            cameraData.streamingUrl = uri
                            cameraData.online = true
                            emmiter.onSuccess(cameraData)
                        } ?: run {
                            emmiter.onSuccess(cameraData)
                        }
                    }
                }
            }
            Utils.log("camera calling getServices")
            currentDevice.getServices()
        }
    }

    fun readDeviceInfo(xmlData: String) : IpCameraInfo {
        try {
            val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ByteArrayInputStream(xmlData.toByteArray()))
            xmlDoc.documentElement.normalize()
            val manufacturer = xmlDoc.getElementsByTagName("tds:Manufacturer").item(0).textContent
            val model = xmlDoc.getElementsByTagName("tds:Model").item(0).textContent
            val firmwareversion = xmlDoc.getElementsByTagName("tds:FirmwareVersion").item(0).textContent
            val serialnumber = xmlDoc.getElementsByTagName("tds:SerialNumber").item(0).textContent
            val hardware = xmlDoc.getElementsByTagName("tds:HardwareId").item(0).textContent

            return IpCameraInfo(manufacturer, model, firmwareversion, serialnumber, hardware)
        } catch (error: Exception){
            error.printStackTrace()
            Utils.log("error parsing: " + error.message)
            return IpCameraInfo()
        }
    }


}