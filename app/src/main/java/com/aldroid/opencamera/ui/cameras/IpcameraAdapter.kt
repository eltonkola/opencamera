package com.aldroid.muzikashqipx.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.aldroid.coinhivesdk.model.IpCameraData
import com.aldroid.opencamera.R
import java.text.NumberFormat
import java.util.*


class IpCameraDataAdapter(val mData: List<IpCameraData>,
                        val onClick: IpCameraDataAdapter.OnClick) : RecyclerView.Adapter<IpCameraDataAdapter.ViewHolder>() {

    interface OnClick {
        fun onClick(element: IpCameraData)
        fun onDelete(elemen: IpCameraData)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, i: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.row_ipcamera, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mData[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int {
        if (mData == null) {
            return 0
        }
        return mData.size
    }

    class ViewHolder(val v: View) : RecyclerView.ViewHolder(v) {

        val image: ImageView
        val title: TextView
        val description: TextView
        val line3 : TextView
        val but_delete : Button
        val view_container: ViewGroup
        val loading : ProgressBar

        init {
            image = v.findViewById(R.id.image)
            title = v.findViewById(R.id.title)
            description = v.findViewById(R.id.description)
            but_delete = v.findViewById(R.id.but_delete)
            view_container = v.findViewById(R.id.view_container)
            loading = v.findViewById(R.id.loading)
            line3 = v.findViewById(R.id.line3)
        }

        fun bind(camera: IpCameraData, eventz: IpCameraDataAdapter.OnClick) {

            title.text = camera.ipCamera.ip
            description.text = "Hw: ${camera.info.hardware} - Man: ${camera.info.manufacturer}"
            line3.text = "Mod: ${camera.info.model} - SN: ${camera.info.serialnumber} -Ver: ${camera.info.firmwareversion}"

            camera.online?.let {
                loading.visibility =  View.GONE
                image.visibility =  View.VISIBLE
                if(it){
                    image.setImageResource(R.drawable.ic_play_circle_filled_black_24dp)
                }else{
                    image.setImageResource(R.drawable.ic_extension_black_24dp)
                }
            } ?: run{
                loading.visibility =  View.VISIBLE
                image.visibility =  View.GONE
            }


            view_container.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    eventz.onClick(camera)
                }
            })

            but_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    eventz.onDelete(camera)
                }
            })

        }

    }

}
