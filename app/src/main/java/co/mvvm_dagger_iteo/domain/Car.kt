package co.mvvm_dagger_iteo.domain
import co.mvvm_dagger_iteo.R
import  co.mvvm_dagger_iteo.data.models.Car

class Car(private val mCar:Car) {
    constructor(brand: String,
                color: String,
                lat: Double,
                lng: Double,
                model: String,
                ownerId: String,
                registration: String,
                year: String
    ):this(Car(brand,color,lat,lng,model,ownerId,registration,year))
    val id = mCar._id
    val brand = mCar.brand
    val color =  mCar.color
    val lat =  mCar.lat
    val lng =  mCar.lng
    val model =  mCar.model
    val ownerId =  mCar.ownerId
    val registration =  mCar.registration
    val year =  mCar.year
    val synced =  mCar.synced

    fun getDataObj():Car = mCar

    fun validateModel():HashMap<String,Int>{
        val ret = HashMap<String,Int>()
        when{
            brand.isEmpty()-> ret["brand"] = R.string.er_brand_field_required
            color.isEmpty()-> ret["color"] = R.string.er_brand_field_required
            lat<=0-> ret["lat"] = R.string.er_lat_field_required
            lng<=0-> ret["lng"] = R.string.er_lng_field_required
            model.isEmpty()-> ret["model"] = R.string.er_model_field_required
            ownerId.isEmpty()-> ret["ownerId"] = R.string.er_owner_field_required
            registration.isEmpty()-> ret["registration"] = R.string.er_owner_field_required
            year.isEmpty()-> ret["year"] = R.string.er_year_field_required
        }
        return ret
    }
}