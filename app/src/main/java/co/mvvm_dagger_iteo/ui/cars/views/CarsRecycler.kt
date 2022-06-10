package co.mvvm_dagger_iteo.ui.cars.views

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.mvvm_dagger_iteo.R
import co.mvvm_dagger_iteo.domain.Car

class CarsRecycler(val data:List<Car>):RecyclerView.Adapter<CarsRecycler.Companion.ItemViewHolder>() {

    companion object {
        class ItemViewHolder(mView: View):RecyclerView.ViewHolder(mView){
            private val consRowContainer = mView.findViewById<TextView>(R.id.cons_row_container)
            private val txtBrandModel = mView.findViewById<TextView>(R.id.txt_brand_model)
            private val txtColor = mView.findViewById<TextView>(R.id.txt_color)
            private val txtYear = mView.findViewById<TextView>(R.id.txt_year)
            private val txtAction = mView.findViewById<TextView>(R.id.txt_action)

            fun bind(mCar:Car,position: Int){
                txtBrandModel.text = mCar.brand
                txtColor.text = mCar.color
                txtYear.text = mCar.year
                if(mCar.synced)  txtAction.visibility = VISIBLE
                else txtAction.visibility = GONE

                if(position%2==0)
                    consRowContainer.setBackgroundResource(R.drawable.row_item)
                else consRowContainer.background = null

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       return ItemViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.row_car,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}