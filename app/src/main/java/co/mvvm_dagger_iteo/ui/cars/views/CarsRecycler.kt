package co.mvvm_dagger_iteo.ui.cars.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.domain.Car

class CarsRecycler(val data:List<Car>,val callBack:(mCar:Car,fromParent:Boolean)->Unit):RecyclerView.Adapter<CarsRecycler.Companion.ItemViewHolder>() {

    companion object {
        class ItemViewHolder(mView: View):RecyclerView.ViewHolder(mView){
             val lnrRowContainer = mView.findViewById<LinearLayout>(R.id.lnr_row_container)
            private val txtBrandModel = mView.findViewById<TextView>(R.id.tag_brand_model)
            private val frmColor = mView.findViewById<FrameLayout>(R.id.frm_color)
            private val tag_registration = mView.findViewById<TextView>(R.id.tag_registration)
             val txtAction = mView.findViewById<TextView>(R.id.tag_action)

            fun bind(mCar:Car,position: Int){
                txtBrandModel.text = "${mCar.brand} (${mCar.model})"
                if(!mCar.color.isNullOrEmpty() && mCar.color.contains("#"))
                    frmColor.setBackgroundColor(Color.parseColor(mCar.color))
                    tag_registration.text = mCar.registration
                if(mCar.synced)  txtAction.visibility = GONE
                else txtAction.visibility = VISIBLE

                if(position%2==0)
                    lnrRowContainer.setBackgroundResource(R.drawable.row_item)
                else lnrRowContainer.background = null

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       return ItemViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.row_car,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position],position)
        holder.lnrRowContainer.setOnClickListener {
            callBack(data[position],true)
        }
        holder.txtAction.setOnClickListener {
            callBack(data[position],false)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}