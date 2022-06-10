package co.mvvm_dagger_iteo.ui.views

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
            val txt_brand_model = mView.findViewById<TextView>(R.id.txt_brand_model)
            val txt_color = mView.findViewById<TextView>(R.id.txt_color)
            val txt_year = mView.findViewById<TextView>(R.id.txt_year)
            val txt_action = mView.findViewById<TextView>(R.id.txt_action)

            fun bind(mCar:Car){
                txt_brand_model.text = mCar.brand
                txt_color.text = mCar.color
                txt_year.text = mCar.year
                if(mCar.synced)  txt_action.visibility = VISIBLE
                else txt_action.visibility = GONE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       return ItemViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.row_car,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}