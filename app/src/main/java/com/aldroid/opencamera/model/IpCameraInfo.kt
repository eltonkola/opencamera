package com.aldroid.coinhivesdk.model

class IpCameraInfo(val manufacturer : String = "?",
                   val model: String = "?",
                   val firmwareversion: String = "?",
                   val serialnumber: String = "?",
                   val hardware: String = "?"){

    override fun toString(): String {
        return "Manufacturer : ${manufacturer} \n " +
                "Model : ${model} \n" +
                "Firmware Version : ${firmwareversion} \n" +
                "Serial Number : ${serialnumber} \n" +
                "Hardware : ${hardware} \n"
    }
}
